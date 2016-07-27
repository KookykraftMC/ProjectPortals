package com.gmail.trentech.pjp.commands.warp;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjp.data.object.Warp;
import com.gmail.trentech.pjp.utils.Help;

public class CMDRemove implements CommandExecutor {

	public CMDRemove() {
		Help help = new Help("wremove", "remove", " Remove an existing  warp point");
		help.setSyntax(" /warp remove <name>\n /w r <name>");
		help.setExample(" /warp remove OldSpawn");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/home remove <name>"));
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get().toLowerCase();

		Optional<Warp> optionalWarp = Warp.get(name);

		if (!optionalWarp.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, name, " does not exist"));
			return CommandResult.empty();
		}
		Warp warp = optionalWarp.get();
		warp.remove();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Warp ", name, " removed"));

		return CommandResult.success();
	}
}
