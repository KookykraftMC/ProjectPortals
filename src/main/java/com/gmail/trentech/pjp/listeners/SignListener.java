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
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.immutable.ImmutableSignPortalData;
import com.gmail.trentech.pjp.data.mutable.SignPortalData;
import com.gmail.trentech.pjp.data.object.Sign;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.events.TeleportEvent;
import com.gmail.trentech.pjp.events.TeleportEvent.Local;
import com.gmail.trentech.pjp.events.TeleportEvent.Server;
import com.gmail.trentech.pjp.utils.ConfigManager;

import flavor.pie.spongycord.SpongyCord;

public class SignListener {

	public static ConcurrentHashMap<UUID, SignPortalData> builders = new ConcurrentHashMap<>();

	private Timings timings;

	public SignListener(Timings timings) {
		this.timings = timings;
	}

	@Listener
	public void onChangeSignEvent(ChangeSignEvent event, @First Player player) {
		timings.onChangeSignEvent().startTiming();

		try {
			if (!builders.containsKey(player.getUniqueId())) {
				return;
			}
			SignPortalData portalData = builders.get(player.getUniqueId());

			if (!player.hasPermission("pjp.sign.place")) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to place sign portals"));
				event.setCancelled(true);
				return;
			}

			event.getTargetTile().offer(portalData);

			Particle particle = Particles.getDefaultEffect("creation");
			particle.spawnParticle(event.getTargetTile().getLocation(), false, Particles.getDefaultColor("creation", particle.isColorable()));

			player.sendMessage(Text.of(TextColors.DARK_GREEN, "New sign portal created"));

			builders.remove(player.getUniqueId());
		} finally {
			timings.onChangeSignEvent().stopTiming();
		}
	}

	@Listener
	public void onInteractBlockEventSecondary(InteractBlockEvent.Secondary event, @First Player player) {
		timings.onChangeSignEvent().startTiming();

		try {
			BlockSnapshot snapshot = event.getTargetBlock();
			if (!(snapshot.getState().getType().equals(BlockTypes.WALL_SIGN) || snapshot.getState().getType().equals(BlockTypes.STANDING_SIGN))) {
				return;
			}

			Location<World> location = snapshot.getLocation().get();

			Optional<SignPortalData> optionalSignPortalData = location.get(SignPortalData.class);

			if (!optionalSignPortalData.isPresent()) {
				return;
			}
			SignPortalData portalData = optionalSignPortalData.get();
			Sign sign = portalData.sign().get();

			if (new ConfigManager().getConfig().getNode("options", "advanced_permissions").getBoolean()) {
				if (!player.hasPermission("pjp.sign." + location.getExtent().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ())) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use this sign portal"));
					event.setCancelled(true);
					return;
				}
			} else {
				if (!player.hasPermission("pjp.sign.interact")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to interact with sign portals"));
					event.setCancelled(true);
					return;
				}
			}

			if (sign.isBungee()) {
				Consumer<String> consumer = (server) -> {
					Server teleportEvent = new TeleportEvent.Server(player, server, sign.getServer(), sign.getPrice(), Cause.of(NamedCause.source(sign)));

					if (!Main.getGame().getEventManager().post(teleportEvent)) {
						SpongyCord.API.connectPlayer(player, teleportEvent.getDestination());

						player.setLocation(player.getWorld().getSpawnLocation());
					}
				};

				SpongyCord.API.getServerName(consumer, player);
			} else {
				Optional<Location<World>> optionalSpawnLocation = sign.getDestination();

				if (!optionalSpawnLocation.isPresent()) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "Destination does not exist"));
					return;
				}
				Location<World> spawnLocation = optionalSpawnLocation.get();

				Local teleportEvent = new TeleportEvent.Local(player, player.getLocation(), spawnLocation, 0, Cause.of(NamedCause.source(sign)));

				if (!Main.getGame().getEventManager().post(teleportEvent)) {
					spawnLocation = teleportEvent.getDestination();

					Vector3d rotation = portalData.sign().get().getRotation().toVector3d();

					player.setLocationAndRotation(spawnLocation, rotation);
				}
			}
		} finally {
			timings.onChangeSignEvent().stopTiming();
		}
	}

	@Listener
	public void onChangeBlockEventBreak(ChangeBlockEvent.Break event, @First Player player) {
		timings.onChangeBlockEventBreak().startTiming();

		try {
			for (Transaction<BlockSnapshot> blockTransaction : event.getTransactions()) {
				BlockSnapshot snapshot = blockTransaction.getOriginal();

				BlockType blockType = snapshot.getState().getType();

				if (!blockType.equals(BlockTypes.WALL_SIGN) && !blockType.equals(BlockTypes.STANDING_SIGN)) {
					continue;
				}

				Optional<ImmutableSignPortalData> optionalSignPortalData = snapshot.get(ImmutableSignPortalData.class);

				if (!optionalSignPortalData.isPresent()) {
					continue;
				}

				if (!player.hasPermission("pjp.sign.break")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to break sign portal"));
					event.setCancelled(true);
				} else {
					player.sendMessage(Text.of(TextColors.DARK_GREEN, "Broke sign portal"));
				}
			}
		} finally {
			timings.onChangeBlockEventBreak().stopTiming();
		}
	}
}
