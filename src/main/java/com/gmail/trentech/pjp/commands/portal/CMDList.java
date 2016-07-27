package com.gmail.trentech.pjp.commands.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.utils.Help;

public class CMDList implements CommandExecutor {

	public CMDList() {
		Help help = new Help("plist", "list", " List all portals");
		help.setSyntax(" /portal list\n /p l");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		ConcurrentHashMap<String, Portal> portals = Portal.all();

		for (Entry<String, Portal> entry : portals.entrySet()) {
			String name = entry.getKey();
			Portal portal = entry.getValue();

			Location<World> location = portal.getFrame().get(0);
			String worldName = location.getExtent().getName();

			double price = portal.getPrice();
			if (price == 0) {
				list.add(Text.of(TextColors.GREEN, "Name: ", TextColors.WHITE, name, TextColors.GREEN, " Location: ", TextColors.WHITE, worldName, " ", location.getBlockX(), " ", location.getBlockY(), " ", location.getBlockZ()));
			} else {
				list.add(Text.of(TextColors.GREEN, "Name: ", TextColors.WHITE, name, TextColors.GREEN, " Location: ", TextColors.WHITE, worldName, " ", location.getBlockX(), " ", location.getBlockY(), " ", location.getBlockZ(), TextColors.GREEN, " Price: ", TextColors.WHITE, "$", portal.getPrice()));
			}
		}

		if (list.isEmpty()) {
			list.add(Text.of(TextColors.YELLOW, " No portals"));
		}

		if (src instanceof Player) {
			Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Portals")).build());

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
