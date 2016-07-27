package com.gmail.trentech.pjp.commands;

import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.events.TeleportEvent;
import com.gmail.trentech.pjp.events.TeleportEvent.Local;
import com.gmail.trentech.pjp.utils.Help;

public class CMDBack implements CommandExecutor {

	public CommandSpec cmdBack = CommandSpec.builder().description(Text.of("Send player to last place they were")).permission("pjp.cmd.back").executor(this).build();

	public static ConcurrentHashMap<Player, Location<World>> players = new ConcurrentHashMap<>();

	public CMDBack() {
		Help help = new Help("back", "back", " Use this command to teleport you to the location you previously came from");
		help.setSyntax(" /back");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Must be a player"));
			return CommandResult.empty();
		}
		Player player = (Player) src;

		if (players.get(player) == null) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "No position to teleport to"));
			return CommandResult.empty();
		}
		Location<World> spawnLocation = players.get(player);

		Local teleportEvent = new TeleportEvent.Local(player, player.getLocation(), spawnLocation, 0, Cause.of(NamedCause.source("back")));

		if (!Main.getGame().getEventManager().post(teleportEvent)) {
			spawnLocation = teleportEvent.getDestination();
			player.setLocation(spawnLocation);
		}

		return CommandResult.success();
	}
}
