package com.gmail.trentech.pjp.commands.warp;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Warp;
import com.gmail.trentech.pjp.utils.Help;
import com.gmail.trentech.pjp.utils.Rotation;

import flavor.pie.spongycord.SpongyCord;

public class CMDCreate implements CommandExecutor {

	public CMDCreate() {
		Help help = new Help("wcreate", "create", " Use this command to create a warp that will teleport you to other worlds");
		help.setSyntax(" /warp create <name> [<destination> [-b] [-c <x,y,z>] [-d <direction>]] [-p <price>]\n /w <name> [<destination> [-b] [-c <x,y,z>] [-d <direction>]] [-p <price>]");
		help.setExample(" /warp create Lobby\n /warp create Lobby MyWorld\n /warp create Lobby MyWorld -c -100,65,254\n /warp create Random MyWorld -c random\n /warp create Lobby MyWorld -c -100,65,254 -d south\n /warp create Lobby MyWorld -d southeast\n /warp Lobby MyWorld -p 50\n /warp Lobby -p 50");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Must be a player"));
			return CommandResult.empty();
		}
		Player player = (Player) src;

		if (!args.hasAny("name")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get().toLowerCase();

		if (name.equalsIgnoreCase("-c") || name.equalsIgnoreCase("-d") || name.equalsIgnoreCase("-p") || name.equalsIgnoreCase("-b")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}

		if (Warp.get(name).isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, name, " already exists"));
			return CommandResult.empty();
		}

		AtomicReference<String> destination = new AtomicReference<>(player.getWorld().getName());

		AtomicReference<Double> price = new AtomicReference<>(0.0);

		if (args.hasAny("price")) {
			try {
				price.set(Double.parseDouble(args.<String> getOne("price").get()));
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.RED, "Incorrect price"));
				src.sendMessage(getUsage());
				return CommandResult.empty();
			}
		}

		AtomicReference<Rotation> rotation = new AtomicReference<>(Rotation.EAST);
		final boolean isBungee = args.hasAny("b");

		if (args.hasAny("destination")) {
			destination.set(args.<String> getOne("destination").get());

			if (destination.get().equalsIgnoreCase("-c") || destination.get().equalsIgnoreCase("-d") || destination.get().equalsIgnoreCase("-p") || destination.get().equalsIgnoreCase("-b")) {
				src.sendMessage(getUsage());
				return CommandResult.empty();
			}

			if (isBungee) {
				Consumer<List<String>> consumer1 = (list) -> {
					if (!list.contains(destination.get())) {
						player.sendMessage(Text.of(TextColors.DARK_RED, destination.get(), " does not exist"));
						return;
					}

					Consumer<String> consumer2 = (s) -> {
						if (destination.get().equalsIgnoreCase(s)) {
							player.sendMessage(Text.of(TextColors.DARK_RED, "Destination cannot be the server you are currently on"));
							return;
						}

						new Warp(name, destination.get(), rotation.get(), price.get(), isBungee).create();

						player.sendMessage(Text.of(TextColors.DARK_GREEN, "Warp ", name, " create"));
					};

					SpongyCord.API.getServerName(consumer2, player);
				};

				SpongyCord.API.getServerList(consumer1, player);

				return CommandResult.success();
			} else {
				if (!Main.getGame().getServer().getWorld(destination.get()).isPresent()) {
					src.sendMessage(Text.of(TextColors.DARK_RED, destination, " is not loaded or does not exist"));
					return CommandResult.empty();
				}
				destination.set(destination.get() + ":spawn");

				if (args.hasAny("x,y,z")) {
					String[] coords = args.<String> getOne("x,y,z").get().split(",");

					if (coords[0].equalsIgnoreCase("random")) {
						destination.set(destination.get().replace("spawn", "random"));
					} else {
						int x;
						int y;
						int z;

						try {
							x = Integer.parseInt(coords[0]);
							y = Integer.parseInt(coords[1]);
							z = Integer.parseInt(coords[2]);
						} catch (Exception e) {
							src.sendMessage(Text.of(TextColors.RED, "Incorrect coordinates"));
							src.sendMessage(getUsage());
							return CommandResult.empty();
						}
						destination.set(destination.get().replace("spawn", x + "." + y + "." + z));
					}
				}

				if (args.hasAny("direction")) {
					String direction = args.<String> getOne("direction").get();

					Optional<Rotation> optionalRotation = Rotation.get(direction);

					if (!optionalRotation.isPresent()) {
						src.sendMessage(Text.of(TextColors.RED, "Incorrect direction"));
						src.sendMessage(getUsage());
						return CommandResult.empty();
					}

					rotation.set(optionalRotation.get());
				}
			}
		} else {
			Location<World> location = player.getLocation();
			destination.set(destination.get() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ());
			rotation.set(Rotation.getClosest(player.getRotation().getFloorY()));
		}

		new Warp(name, destination.get(), rotation.get(), price.get(), isBungee).create();

		player.sendMessage(Text.of(TextColors.DARK_GREEN, "Warp ", name, " create"));

		return CommandResult.success();
	}

	private Text getUsage() {
		Text usage = Text.of(TextColors.RED, "Usage: /warp create <name>");

		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter a world or bungee server"))).append(Text.of(" [<destination>")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Use this flag if <destination> is a bungee server"))).append(Text.of(" [-b]")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter x y z coordinates or \"random\""))).append(Text.of(" [-c <x,y,z>]")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("NORTH\nNORTHEAST\nEAST\nSOUTHEAST\nSOUTH\nSOUTHWEST\nWEST\nNORTHWEST"))).append(Text.of(" [-d <direction>]]")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter the cost to use portal or 0 to disable"))).append(Text.of(" [-p price]")).build());

		return usage;
	}
}
