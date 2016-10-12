package com.gmail.trentech.pjp.commands.warp;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.trentech.pjp.portal.Portal;
import com.gmail.trentech.pjp.portal.Portal.PortalType;
import com.gmail.trentech.pjp.rotation.Rotation;
import com.gmail.trentech.pjp.utils.Help;

import flavor.pie.spongycord.SpongyCord;

public class CMDCreate implements CommandExecutor {

	public CMDCreate() {
		Help help = new Help("wcreate", "create", " Use this command to create a warp that will teleport you to other worlds");
		help.setPermission("pjp.cmd.warp.create");
		help.setSyntax(" /warp create <name> [<destination> [-b] [-c <x,y,z>] [-d <direction>]] [-p <price>]\n /w <name> [<destination> [-b] [-c <x,y,z>] [-d <direction>]] [-p <price>]");
		help.setExample(" /warp create Lobby\n /warp create Lobby MyWorld\n /warp create Lobby MyWorld -c -100,65,254\n /warp create Random MyWorld -c random\n /warp create Lobby MyWorld -c -100,65,254 -d south\n /warp create Lobby MyWorld -d southeast\n /warp Lobby MyWorld -p 50\n /warp Lobby -p 50");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}
		Player player = (Player) src;

		String name = args.<String>getOne("name").get().toLowerCase();

		if (Portal.get(name, PortalType.WARP).isPresent()) {
			throw new CommandException(Text.of(TextColors.RED, name, " already exists"), false);
		}

		Optional<World> world = Optional.empty();
		Optional<Vector3d> vector3d = Optional.empty();
		AtomicReference<Rotation> rotation = new AtomicReference<>(Rotation.EAST);
		AtomicReference<Double> price = new AtomicReference<>(0.0);

		if (args.hasAny("price")) {
			price.set(args.<Double>getOne("price").get());
		}

		final boolean isBungee = args.hasAny("b");

		if (args.hasAny("destination")) {
			String destination = args.<String>getOne("destination").get();

			if (isBungee) {
				Consumer<List<String>> consumer1 = (list) -> {
					if (!list.contains(destination)) {
						try {
							throw new CommandException(Text.of(TextColors.RED, destination, " does not exist"), false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					Consumer<String> consumer2 = (s) -> {
						if (destination.equalsIgnoreCase(s)) {
							try {
								throw new CommandException(Text.of(TextColors.RED, "Destination cannot be the server you are currently on"), false);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						new Portal.Server(PortalType.WARP, destination, rotation.get(), price.get()).create(name);

						player.sendMessage(Text.of(TextColors.DARK_GREEN, "Warp ", name, " create"));
					};

					SpongyCord.API.getServerName(consumer2, player);
				};

				SpongyCord.API.getServerList(consumer1, player);

				return CommandResult.success();
			} else {
				if (!Sponge.getServer().getWorld(destination).isPresent()) {
					throw new CommandException(Text.of(TextColors.RED, destination, " is not loaded or does not exist"), false);
				}

				world = Sponge.getServer().getWorld(destination);

				if (!world.isPresent()) {
					throw new CommandException(Text.of(TextColors.RED, destination, " is not loaded or does not exist"), false);
				}

				if (args.hasAny("x,y,z")) {
					String[] coords = args.<String>getOne("x,y,z").get().split(",");

					if (coords[0].equalsIgnoreCase("random")) {
						vector3d = Optional.of(new Vector3d(0, 0, 0));
					} else {
						try {
							vector3d = Optional.of(new Vector3d(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2])));
						} catch (Exception e) {
							throw new CommandException(Text.of(TextColors.RED, coords.toString(), " is not valid"), true);
						}
					}
				}

				if (args.hasAny("direction")) {
					rotation.set(args.<Rotation>getOne("direction").get());
				}
			}
		} else {
			world = Optional.of(player.getWorld());
			vector3d = Optional.of(player.getLocation().getPosition());
			rotation.set(Rotation.getClosest(player.getRotation().getFloorY()));
		}

		new Portal.Local(PortalType.WARP, world.get(), vector3d, rotation.get(), price.get()).create(name);

		player.sendMessage(Text.of(TextColors.DARK_GREEN, "Warp ", name, " create"));

		return CommandResult.success();
	}

}
