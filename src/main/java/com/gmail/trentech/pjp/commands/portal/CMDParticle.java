package com.gmail.trentech.pjp.commands.portal;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.ParticleColor;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.utils.Help;

public class CMDParticle implements CommandExecutor {

	public CMDParticle() {
		Help help = new Help("particle", "particle", " change a portals particle effect. Color currently only available for REDSTONE");
		help.setSyntax(" /portal particle <name> <type> [color]\n /p p <name> <type> [color]");
		help.setExample(" /portal particle MyPortal CRIT\n /portal particle MyPortal REDSTONE BLUE");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get().toLowerCase();

		if (!Portal.get(name).isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, name, " does not exist"));
			return CommandResult.empty();
		}
		Portal portal = Portal.get(name).get();

		if (!args.hasAny("type")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}
		String type = args.<String> getOne("type").get().toUpperCase();

		Optional<Particle> optionalParticle = Particles.get(type);

		if (!optionalParticle.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Incorrect particle"));
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}
		Particle particle = optionalParticle.get();

		Optional<ParticleColor> color = Optional.empty();

		if (args.hasAny("color")) {
			if (particle.isColorable()) {
				color = ParticleColor.get(args.<String> getOne("color").get().toUpperCase());

				if (!color.isPresent()) {
					src.sendMessage(Text.of(TextColors.RED, "Incorrect color"));
					src.sendMessage(getUsage());
					return CommandResult.empty();
				}
			} else {
				src.sendMessage(Text.of(TextColors.YELLOW, "Colors currently only works with REDSTONE type"));
			}
		}

		portal.setParticle(particle);
		portal.setParticleColor(color);
		portal.update();

		return CommandResult.success();
	}

	private Text getUsage() {
		Text usage = Text.of(TextColors.RED, "Usage: /portal particle <name>");

		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("CLOUD\nCRIT\nCRIT_MAGIC\nENCHANTMENT_TABLE\nFLAME\nHEART\nNOTE\nPORTAL\nPORTAL2" + "\nREDSTONE\nSLIME\nSNOWBALL\nSNOW_SHOVEL\nSMOKE_LARGE\nSPELL\nSPELL_WITCH\nSUSPENDED_DEPTH" + "\nVILLAGER_HAPPY\nWATER_BUBBLE\nWATER_DROP\nWATER_SPLASH\nWATER_WAKE\nNONE"))).append(Text.of(" <type>")).build());
		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("REDSTONE ONLY\n", TextColors.DARK_GRAY, "BLACK\n", TextColors.GRAY, "GRAY\n", TextColors.WHITE, "WHITE\n", TextColors.BLUE, "BLUE\n", TextColors.GREEN, "GREEN\n", TextColors.GREEN, "LIME\n", TextColors.RED, "RED\n", TextColors.YELLOW, "YELLOW\n", TextColors.LIGHT_PURPLE, "MAGENTA\n", TextColors.DARK_PURPLE, "PURPLE\n", TextColors.DARK_AQUA, "DARK_CYAN\n", TextColors.DARK_GREEN, "DARK_GREEN\n", TextColors.DARK_PURPLE, "DARK_MAGENTA\n", TextColors.AQUA, "CYAN\n", TextColors.DARK_BLUE, "NAVY\n", TextColors.LIGHT_PURPLE, "PINK\n", TextColors.RED, "R", TextColors.YELLOW, "A", TextColors.GREEN, "I", TextColors.BLUE, "N", TextColors.DARK_PURPLE, "B", TextColors.RED, "O", TextColors.YELLOW, "W"))).append(Text.of(" [color]")).build());

		return usage;
	}
}
