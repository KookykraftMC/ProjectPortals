package com.gmail.trentech.pjp.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.utils.ConfigManager;
import com.gmail.trentech.pjp.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDPjp implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		ConfigurationNode node = new ConfigManager().getConfig().getNode("settings", "modules");

		if (src.hasPermission("pjp.cmd.portal") && node.getNode("portals").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for list of sub commands "))).onClick(TextActions.runCommand("/pjp:portal")).append(Text.of(" /portal")).build());
		}
		if (src.hasPermission("pjp.cmd.plate") && node.getNode("plates").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for usage infomation "))).onClick(TextActions.executeCallback(Help.getHelp("plate"))).append(Text.of(" /plate")).build());
		}
		if (src.hasPermission("pjp.cmd.lever") && node.getNode("levers").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for usage infomation "))).onClick(TextActions.executeCallback(Help.getHelp("lever"))).append(Text.of(" /lever")).build());
		}
		if (src.hasPermission("pjp.cmd.sign") && node.getNode("signs").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for usage infomation "))).onClick(TextActions.executeCallback(Help.getHelp("sign"))).append(Text.of(" /sign")).build());
		}
		if (src.hasPermission("pjp.cmd.door") && node.getNode("doors").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for usage infomation "))).onClick(TextActions.executeCallback(Help.getHelp("door"))).append(Text.of(" /door")).build());
		}
		if (src.hasPermission("pjp.cmd.button") && node.getNode("buttons").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for usage infomation "))).onClick(TextActions.executeCallback(Help.getHelp("button"))).append(Text.of(" /button")).build());
		}
		if (src.hasPermission("pjp.cmd.home") && node.getNode("homes").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for list of sub commands "))).onClick(TextActions.runCommand("/pjp:home")).append(Text.of(" /home")).build());
		}
		if (src.hasPermission("pjp.cmd.warp") && node.getNode("warps").getBoolean()) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for list of sub commands "))).onClick(TextActions.runCommand("/pjp:warp")).append(Text.of(" /warp")).build());
		}
		if (src.hasPermission("pjp.cmd.back")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for usage infomation "))).onClick(TextActions.executeCallback(Help.getHelp("back"))).append(Text.of(" /back")).build());
		}

		if (src instanceof Player) {
			PaginationList.Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			for (Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}

}
