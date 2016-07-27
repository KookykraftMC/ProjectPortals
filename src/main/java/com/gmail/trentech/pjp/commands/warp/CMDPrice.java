package com.gmail.trentech.pjp.commands.warp;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjp.data.object.Warp;
import com.gmail.trentech.pjp.utils.Help;

public class CMDPrice implements CommandExecutor {

	public CMDPrice() {
		Help help = new Help("wprice", "price", " Charge players for using warps. 0 to disable");
		help.setSyntax(" /warp price <name> <price>\n /w p <name> <price>");
		help.setExample(" /warp price Lobby 50\n /warp price Lobby 0");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get().toLowerCase();

		if (!Warp.get(name).isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, name, " does not exist"));
			return CommandResult.empty();
		}
		Warp warp = Warp.get(name).get();

		if (!args.hasAny("price")) {
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}

		double price;
		try {
			price = Double.parseDouble(args.<String> getOne("price").get());
		} catch (Exception e) {
			src.sendMessage(Text.of(TextColors.RED, "Incorrect price"));
			src.sendMessage(getUsage());
			return CommandResult.empty();
		}

		warp.setPrice(price);
		warp.update();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set price of warp ", name, " to $", price));

		return CommandResult.success();
	}

	private Text getUsage() {
		Text usage = Text.of(TextColors.RED, "Usage: /warp price <name>");

		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter the cost to use portal or 0 to disable"))).append(Text.of(" <price>")).build());

		return usage;
	}
}
