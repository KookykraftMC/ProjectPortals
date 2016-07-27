package com.gmail.trentech.pjp.commands.portal;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.utils.Help;

public class CMDPrice implements CommandExecutor {

	public CMDPrice() {
		Help help = new Help("pprice", "price", " Charge players for using portals. 0 to disable");
		help.setSyntax(" /portal price <name> <price>\n /p pr <name> <price>");
		help.setExample(" /portal price MyPortal 50\n /portal price MyPortal 0");
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

		portal.setPrice(price);
		portal.update();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set price of portal ", name, " to $", price));

		return CommandResult.success();
	}

	private Text getUsage() {
		Text usage = Text.of(TextColors.RED, "Usage: /portal price <name>");

		usage = Text.join(usage, Text.builder().color(TextColors.RED).onHover(TextActions.showText(Text.of("Enter the cost to use portal or 0 to disable"))).append(Text.of(" <price>")).build());

		return usage;
	}
}
