package com.gmail.trentech.pjp.commands.portal;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.utils.Help;

public class CMDRemove implements CommandExecutor {

	public CMDRemove() {
		Help help = new Help("premove", "remove", " Remove an existing portal");
		help.setSyntax(" /portal remove <name>\n /p r <name>");
		help.setExample(" /portal remove MyPortal");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/portal remove <name>"));
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get().toLowerCase();

		Optional<Portal> optionalPortal = Portal.get(name);

		if (!optionalPortal.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, name, " does not exist"));
			return CommandResult.empty();
		}
		Portal portal = optionalPortal.get();
		portal.remove();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Portal ", name, " removed"));

		return CommandResult.success();
	}
}
