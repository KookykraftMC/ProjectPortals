package com.gmail.trentech.pjp.commands.portal;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.particle.ParticleType.Colorable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.data.object.PortalBuilder;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.ParticleColor;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.listeners.PortalListener;
import com.gmail.trentech.pjp.portal.PortalProperties;
import com.gmail.trentech.pjp.utils.ConfigManager;
import com.gmail.trentech.pjp.utils.Help;
import com.gmail.trentech.pjp.utils.Rotation;

import flavor.pie.spongycord.SpongyCord;

public class CMDCreate implements CommandExecutor {

	public CMDCreate() {
		Help help = new Help("pcreate", "create", " Use this command to create a portal that will teleport you to other worlds");
		help.setSyntax(" /portal create <name> <destination> [-b] [-c <x,y,z>] [-d <direction>] [-e <particle[:color]>] [-p <price>]\n /p <name> <destination> [-b] [-c <x,y,z>] [-d <direction>] [-e <particle[:color]>] [-p <price>]");
		help.setExample(" /portal create MyPortal MyWorld\n /portal create MyPortal MyWorld -c -100,65,254\n /portal create MyPortal MyWorld -c random\n /portal create MyPortal MyWorld -c -100,65,254 -d south\n /portal create MyPortal MyWorld -d southeast\n /portal create MyPortal MyWorld -p 50\n /portal create MyPortal MyWorld -e REDSTONE:BLUE");
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

		if (name.equalsIgnoreCase("-c") || name.equalsIgnoreCase("-d") || name.equalsIgnoreCase("-p") || name.equalsIgnoreCase("-e") || name.equalsIgnoreCase("-b")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}

		if (Portal.get(name).isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, name, " already exists"));
			return CommandResult.empty();
		}

		if (!args.hasAny("destination")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}
		AtomicReference<String> destination = new AtomicReference<>(args.<String> getOne("destination").get());

		if (destination.get().equalsIgnoreCase("-c") || destination.get().equalsIgnoreCase("-d") || destination.get().equalsIgnoreCase("-p") || destination.get().equalsIgnoreCase("-e") || destination.get().equalsIgnoreCase("-b")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}

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

		AtomicReference<Particle> particle = new AtomicReference<>(Particles.getDefaultEffect("portal"));

		AtomicReference<Optional<ParticleColor>> color = new AtomicReference<>(Particles.getDefaultColor("portal", particle.get().isColorable()));

		if (args.hasAny("particle[:color]")) {
			String[] type = args.<String> getOne("particle[:color]").get().toUpperCase().split(":");

			Optional<Particle> optionalParticle = Particles.get(type[0]);

			if (!optionalParticle.isPresent()) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Incorrect particle"));
				src.sendMessage(getUsage());
				return CommandResult.empty();
			}
			particle.set(optionalParticle.get());

			if (type.length == 2) {
				if (particle.get().getType() instanceof Colorable) {
					color.set(ParticleColor.get(type[1]));

					if (!color.get().isPresent()) {
						src.sendMessage(Text.of(TextColors.RED, "Incorrect color"));
						src.sendMessage(getUsage());
						return CommandResult.empty();
					}
				} else {
					src.sendMessage(Text.of(TextColors.YELLOW, "Colors currently only works with REDSTONE type"));
				}
			}
		}

		AtomicReference<Rotation> rotation = new AtomicReference<>(Rotation.EAST);
		final boolean isBungee = args.hasAny("b");

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

					PortalListener.builders.put(player.getUniqueId(), new PortalBuilder(name, destination.get(), rotation.get(), particle.get(), color.get(), price.get(), isBungee));

					player.sendMessage(Text.builder().color(TextColors.DARK_GREEN).append(Text.of("Begin building your portal frame, followed by ")).onClick(TextActions.runCommand("/pjp:portal save")).append(Text.of(TextColors.YELLOW, TextStyles.UNDERLINE, "/portal save")).build());
				};

				SpongyCord.API.getServerName(consumer2, player);
			};

			SpongyCord.API.getServerList(consumer1, player);
		} else {
			if (!Main.getGame().getServer().getWorld(destination.get()).isPresent()) {
				src.sendMessage(Text.of(TextColors.DARK_RED, destination.get(), " is not loaded or does not exist"));
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
			
			if(new ConfigManager().getConfig().getNode("options", "portal", "legacy").getBoolean()) {
				PortalListener.builders.put(player.getUniqueId(), new PortalBuilder(name, destination.get(), rotation.get(), particle.get(), color.get(), price.get(), isBungee));
				player.sendMessage(Text.builder().color(TextColors.DARK_GREEN).append(Text.of("Begin building your portal frame, followed by ")).onClick(TextActions.runCommand("/pjp:portal save")).append(Text.of(TextColors.YELLOW, TextStyles.UNDERLINE, "/portal save")).build());
			} else {
				PortalListener.props.put(player.getUniqueId(), new PortalProperties(name, destination.get(), rotation.get(), particle.get(), color.get(), price.get(), isBungee));
				player.sendMessage(Text.of(TextColors.DARK_GREEN, "Right click bottom with empty hand similar to vanilla nether portals "));
			}
		}

		return CommandResult.success();
	}

	private Text getUsage() {
		Text usage = Text.of(TextColors.RED, "Usage: /portal create <name>");

		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter a world or bungee server"))).append(Text.of(" <destination>")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Use this flag if <destination> is a bungee server"))).append(Text.of(" [-b]")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter x y z coordinates or \"random\""))).append(Text.of(" [-c <x,y,z>]")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("NORTH\nNORTHEAST\nEAST\nSOUTHEAST\nSOUTH\nSOUTHWEST\nWEST\nNORTHWEST"))).append(Text.of(" [-d <direction>]")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("CLOUD\nCRIT\nCRIT_MAGIC\nENCHANTMENT_TABLE\nFLAME\nHEART\nNOTE\nPORTAL\nPORTAL2" + "\nREDSTONE\nSLIME\nSNOWBALL\nSNOW_SHOVEL\nSMOKE_LARGE\nSPELL\nSPELL_WITCH\nSUSPENDED_DEPTH\nVILLAGER_HAPPY\nWATER_BUBBLE\nWATER_DROP\nWATER_SPLASH\nWATER_WAKE\nNONE"))).append(Text.of(" [-e <particle")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("REDSTONE ONLY\n", TextColors.DARK_GRAY, "BLACK\n", TextColors.GRAY, "GRAY\n", TextColors.WHITE, "WHITE\n", TextColors.BLUE, "BLUE\n", TextColors.GREEN, "GREEN\n", TextColors.GREEN, "LIME\n", TextColors.RED, "RED\n", TextColors.YELLOW, "YELLOW\n", TextColors.LIGHT_PURPLE, "MAGENTA\n", TextColors.DARK_PURPLE, "PURPLE\n", TextColors.DARK_AQUA, "DARK_CYAN\n", TextColors.DARK_GREEN, "DARK_GREEN\n", TextColors.DARK_PURPLE, "DARK_MAGENTA\n", TextColors.AQUA, "CYAN\n", TextColors.DARK_BLUE, "NAVY\n", TextColors.LIGHT_PURPLE, "PINK\n", TextColors.RED, "R", TextColors.YELLOW, "A", TextColors.GREEN, "I", TextColors.BLUE, "N", TextColors.DARK_PURPLE, "B", TextColors.RED, "O", TextColors.YELLOW, "W"))).append(Text.of("[:color]>]")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter the cost to use portal or 0 to disable"))).append(Text.of(" [-p price]")).build());

		return usage;
	}
}
