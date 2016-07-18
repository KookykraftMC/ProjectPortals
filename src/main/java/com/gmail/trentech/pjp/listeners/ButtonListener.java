package com.gmail.trentech.pjp.listeners;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
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
import com.gmail.trentech.pjp.data.object.Button;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.events.TeleportEvent;
import com.gmail.trentech.pjp.events.TeleportEvent.Local;
import com.gmail.trentech.pjp.events.TeleportEvent.Server;
import com.gmail.trentech.pjp.utils.ConfigManager;

import flavor.pie.spongycord.SpongyCord;

public class ButtonListener {

	public static ConcurrentHashMap<UUID, Button> builders = new ConcurrentHashMap<>();
	
	private Timings timings;

	public ButtonListener(Timings timings) {
		this.timings = timings;
	}
	
	@Listener
	public void onChangeBlockEventModify(ChangeBlockEvent.Modify event, @First Player player) {
		timings.onChangeBlockEventModify().startTimingIfSync();
		
		try {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				BlockSnapshot snapshot = transaction.getFinal();
				BlockState block = snapshot.getExtendedState();
				BlockType blockType = block.getType();

				if (!blockType.equals(BlockTypes.STONE_BUTTON) && !blockType.equals(BlockTypes.WOODEN_BUTTON)) {
					continue;
				}

				if (!block.get(Keys.POWERED).isPresent()) {
					continue;
				}

				if (!block.get(Keys.POWERED).get()) {
					continue;
				}

				Location<World> location = snapshot.getLocation().get();

				Optional<Button> optionalButton = Button.get(location);

				if (!optionalButton.isPresent()) {
					continue;
				}

				Button button = optionalButton.get();

				if (new ConfigManager().getConfig().getNode("options", "advanced_permissions").getBoolean()) {
					if (!player.hasPermission("pjp.button." + location.getExtent().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ())) {
						player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use this button portal"));
						event.setCancelled(true);
						return;
					}
				} else {
					if (!player.hasPermission("pjp.button.interact")) {
						player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to interact with button portals"));
						event.setCancelled(true);
						return;
					}
				}

				if (button.isBungee()) {
					Consumer<String> consumer = (server) -> {
						Server teleportEvent = new TeleportEvent.Server(player, server, button.getServer(), button.getPrice(), Cause.of(NamedCause.source(button)));

						if (!Main.getGame().getEventManager().post(teleportEvent)) {
							SpongyCord.API.connectPlayer(player, teleportEvent.getDestination());

							player.setLocation(player.getWorld().getSpawnLocation());
						}
					};

					SpongyCord.API.getServerName(consumer, player);
				} else {
					Optional<Location<World>> optionalSpawnLocation = button.getDestination();

					if (!optionalSpawnLocation.isPresent()) {
						player.sendMessage(Text.of(TextColors.DARK_RED, "World does not exist"));
						continue;
					}
					Location<World> spawnLocation = optionalSpawnLocation.get();

					Local teleportEvent = new TeleportEvent.Local(player, player.getLocation(), spawnLocation, button.getPrice(), Cause.of(NamedCause.source(button)));

					if (!Main.getGame().getEventManager().post(teleportEvent)) {
						spawnLocation = teleportEvent.getDestination();

						Vector3d rotation = button.getRotation().toVector3d();

						player.setLocationAndRotation(spawnLocation, rotation);
					}
				}
			}
		} finally {
			timings.onChangeBlockEventModify().stopTimingIfSync();
		}
	}

	@Listener
	public void onChangeBlockEventBreak(ChangeBlockEvent.Break event, @First Player player) {
		timings.onChangeBlockEventBreak().startTimingIfSync();
		
		try {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				Location<World> location = transaction.getFinal().getLocation().get();

				Optional<Button> optionalButton = Button.get(location);

				if (!optionalButton.isPresent()) {
					continue;
				}
				Button button = optionalButton.get();

				if (!player.hasPermission("pjp.button.break")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to break button portals"));
					event.setCancelled(true);
				} else {
					button.remove();
					player.sendMessage(Text.of(TextColors.DARK_GREEN, "Broke button portal"));

				}
			}
		} finally {
			timings.onChangeBlockEventBreak().stopTimingIfSync();
		}
	}

	@Listener
	public void onChangeBlockEventPlace(ChangeBlockEvent.Place event, @First Player player) {
		timings.onChangeBlockEventPlace().startTimingIfSync();
		
		try {
			if (!builders.containsKey(player.getUniqueId())) {
				return;
			}

			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				BlockType blockType = transaction.getFinal().getState().getType();

				if (!blockType.equals(BlockTypes.STONE_BUTTON) && !blockType.equals(BlockTypes.WOODEN_BUTTON)) {
					continue;
				}

				Location<World> location = transaction.getFinal().getLocation().get();

				if (!player.hasPermission("pjp.button.place")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to place button portals"));
					builders.remove(player.getUniqueId());
					return;
				}

				Button button = builders.get(player.getUniqueId());
				button.setLocation(location);
				button.create();

				Particle particle = Particles.getDefaultEffect("creation");
				particle.spawnParticle(location, false, Particles.getDefaultColor("creation", particle.isColorable()));

				player.sendMessage(Text.of(TextColors.DARK_GREEN, "New button portal created"));

				builders.remove(player.getUniqueId());
			}
		} finally {
			timings.onChangeBlockEventPlace().stopTimingIfSync();
		}
	}
}
