package com.gmail.trentech.pjp.commands.warp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Warp;
import com.gmail.trentech.pjp.utils.Help;

public class CMDList implements CommandExecutor {

	public CMDList() {
		Help help = new Help("wlist", "list", " List all warp points");
		help.setSyntax(" /warp list\n /w l");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		ConcurrentHashMap<String, Warp> warps = Warp.all();

		for (Entry<String, Warp> entry : warps.entrySet()) {
			String name = entry.getKey();
			Warp warp = entry.getValue();

			if (!src.hasPermission("pjp.warps." + name)) {
				continue;
			}

			Optional<Location<World>> optionalLocation = warp.getDestination();

			if (!optionalLocation.isPresent()) {
				continue;
			}

			double price = warp.getPrice();
			if (price == 0) {
				list.add(Text.of(TextColors.GREEN, "Name: ", TextColors.WHITE, name));
			} else {
				list.add(Text.of(TextColors.GREEN, "Name: ", TextColors.WHITE, name, TextColors.GREEN, " Price: ", TextColors.WHITE, "$", warp.getPrice()));
			}

		}

		if (list.isEmpty()) {
			list.add(Text.of(TextColors.YELLOW, " No warp points"));
		}

		if (src instanceof Player) {
			PaginationList.Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Warps")).build());

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
