package com.gmail.trentech.pjp.commands.home;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.Keys;
import com.gmail.trentech.pjp.data.mutable.HomeData;
import com.gmail.trentech.pjp.data.object.Home;
import com.gmail.trentech.pjp.utils.Help;
import com.gmail.trentech.pjp.utils.Rotation;

public class CMDCreate implements CommandExecutor {

	public CMDCreate() {
		Help help = new Help("hcreate", "create", " Create a new home");
		help.setSyntax(" /home create <name>\n /h c <name>");
		help.setExample(" /home create MyHome");
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
			src.sendMessage(Text.of(TextColors.RED, "Usage: /home create <name>"));
			return CommandResult.empty();
		}
		String homeName = args.<String> getOne("name").get().toLowerCase();

		Map<String, Home> homeList = new HashMap<>();

		Optional<Map<String, Home>> optionalHomeList = player.get(Keys.HOMES);

		if (optionalHomeList.isPresent()) {
			homeList = optionalHomeList.get();
		}

		int defaultAmount = Main.getConfigManager().getConfig().getNode("options", "homes").getInt();

		int amount = homeList.size();

		int extra = 0;
		for (int i = 1; i <= 100; i++) {
			if (player.hasPermission("pjp.homes." + i)) {
				extra = i;
				break;
			}
		}

		if (!player.hasPermission("pjp.homes.unlimited")) {
			if (amount >= (defaultAmount + extra)) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "You have reached the maximum number of homes you can have"));
				return CommandResult.empty();
			}
			amount++;
		}

		if (homeList.containsKey(homeName)) {
			src.sendMessage(Text.of(TextColors.DARK_RED, homeName, " already exists."));
			return CommandResult.empty();
		}

		Location<World> location = player.getLocation();

		String destination = location.getExtent().getName() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();

		homeList.put(homeName, new Home(destination, Rotation.getClosest(player.getRotation().getFloorY())));

		DataTransactionResult result = player.offer(new HomeData(homeList));
		if (!result.isSuccessful()) {
			System.out.println("FAILED");
		} else {
			player.sendMessage(Text.of(TextColors.DARK_GREEN, "Home ", homeName, " create"));
		}

		return CommandResult.success();
	}
}
