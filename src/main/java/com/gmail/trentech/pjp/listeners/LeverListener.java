package com.gmail.trentech.pjp.listeners;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Lever;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.events.TeleportEvent;
import com.gmail.trentech.pjp.events.TeleportEvent.Local;
import com.gmail.trentech.pjp.events.TeleportEvent.Server;
import com.gmail.trentech.pjp.utils.ConfigManager;

import flavor.pie.spongycord.SpongyCord;

public class LeverListener {

	public static ConcurrentHashMap<UUID, Lever> builders = new ConcurrentHashMap<>();

	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Modify event, @First Player player) {
		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			BlockSnapshot snapshot = transaction.getFinal();
			BlockType blockType = snapshot.getState().getType();

			if (!blockType.equals(BlockTypes.LEVER)) {
				continue;
			}

			Location<World> location = snapshot.getLocation().get();

			Optional<Lever> optionalLever = Lever.get(location);

			if (!optionalLever.isPresent()) {
				continue;
			}

			Lever lever = optionalLever.get();

			if (new ConfigManager().getConfig().getNode("options", "advanced_permissions").getBoolean()) {
				if (!player.hasPermission("pjp.lever." + location.getExtent().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ())) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use this lever portal"));
					event.setCancelled(true);
					return;
				}
			} else {
				if (!player.hasPermission("pjp.lever.interact")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to interact with lever portals"));
					event.setCancelled(true);
					return;
				}
			}

			if (lever.isBungee()) {
				Consumer<String> consumer = (server) -> {
					Server teleportEvent = new TeleportEvent.Server(player, server, lever.getServer(), lever.getPrice(), Cause.of(NamedCause.source(lever)));

					if (!Main.getGame().getEventManager().post(teleportEvent)) {
						SpongyCord.API.connectPlayer(player, teleportEvent.getDestination());

						player.setLocation(player.getWorld().getSpawnLocation());
					}
				};

				SpongyCord.API.getServerName(consumer, player);
			} else {
				Optional<Location<World>> optionalSpawnLocation = lever.getDestination();

				if (!optionalSpawnLocation.isPresent()) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "World does not exist"));
					continue;
				}
				Location<World> spawnLocation = optionalSpawnLocation.get();

				Local teleportEvent = new TeleportEvent.Local(player, player.getLocation(), spawnLocation, lever.getPrice(), Cause.of(NamedCause.source(lever)));

				if (!Main.getGame().getEventManager().post(teleportEvent)) {
					spawnLocation = teleportEvent.getDestination();

					Vector3d rotation = lever.getRotation().toVector3d();

					player.setLocationAndRotation(spawnLocation, rotation);
				}
			}
		}
	}

	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Break event, @First Player player) {
		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			Location<World> location = transaction.getFinal().getLocation().get();

			Optional<Lever> optionalLever = Lever.get(location);

			if (!optionalLever.isPresent()) {
				continue;
			}
			Lever lever = optionalLever.get();

			if (!player.hasPermission("pjp.lever.break")) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to break lever portals"));
				event.setCancelled(true);
			} else {
				lever.remove();
				player.sendMessage(Text.of(TextColors.DARK_GREEN, "Broke lever portal"));
			}
		}
		return;
	}

	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Place event, @First Player player) {
		if (!builders.containsKey(player.getUniqueId())) {
			return;
		}

		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			BlockType blockType = transaction.getFinal().getState().getType();

			if (!blockType.equals(BlockTypes.LEVER)) {
				continue;
			}

			Location<World> location = transaction.getFinal().getLocation().get();

			if (!player.hasPermission("pjp.lever.place")) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to place lever portals"));
				builders.remove(player.getUniqueId());
				event.setCancelled(true);
				return;
			}

			Lever lever = builders.get(player.getUniqueId());
			lever.setLocation(location);
			lever.create();

			Particle particle = Particles.getDefaultEffect("creation");
			particle.spawnParticle(location, false, Particles.getDefaultColor("creation", particle.isColorable()));

			player.sendMessage(Text.of(TextColors.DARK_GREEN, "New button lever created"));

			builders.remove(player.getUniqueId());
		}
	}
}
