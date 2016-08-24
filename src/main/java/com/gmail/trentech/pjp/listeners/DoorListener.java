package com.gmail.trentech.pjp.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Door;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.events.TeleportEvent;
import com.gmail.trentech.pjp.events.TeleportEvent.Local;
import com.gmail.trentech.pjp.events.TeleportEvent.Server;
import com.gmail.trentech.pjp.utils.Rotation;

import flavor.pie.spongycord.SpongyCord;

public class DoorListener {

	public static ConcurrentHashMap<UUID, Door> builders = new ConcurrentHashMap<>();

	private Timings timings;

	public DoorListener(Timings timings) {
		this.timings = timings;
	}

	@Listener
	public void onTabCompleteEvent(TabCompleteEvent event, @First Player player) {
		String rawMessage = event.getRawMessage();
		
		String[] args = rawMessage.split(" ");
		
		List<String> list = event.getTabCompletions();
		
		if((args[0].equalsIgnoreCase("door") || args[0].equalsIgnoreCase("d"))) {			
			if(args.length > 1 && (args[args.length - 1].equalsIgnoreCase("-d") || args[args.length - 2].equalsIgnoreCase("-d"))) {
				for (Rotation rotation : Rotation.values()) {
					String id = rotation.getName();
					
					if(args[args.length - 2].equalsIgnoreCase("-d")) {
						if(id.contains(args[args.length - 1].toLowerCase()) && !id.equalsIgnoreCase(args[args.length - 1])) {
							list.add(id);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(id);
					}
				}
			} else if(args.length == 1 || args.length == 2) {
				for(WorldProperties world : Sponge.getServer().getAllWorldProperties()) {
					String name = world.getWorldName();
					
					if(args.length == 2) {
						if(name.contains(args[1].toLowerCase()) && !name.equalsIgnoreCase(args[1])) {
							list.add(name);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(name);
					}
				}
			}
		}
	}
	
	@Listener
	public void onChangeBlockEventBreak(ChangeBlockEvent.Break event, @First Player player) {
		timings.onChangeBlockEventBreak().startTiming();

		try {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				Location<World> location = transaction.getFinal().getLocation().get();

				Optional<Door> optionalDoor = Door.get(location);

				if (!optionalDoor.isPresent()) {
					continue;
				}
				Door door = optionalDoor.get();

				if (!player.hasPermission("pjp.door.break")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to break door portals"));
					event.setCancelled(true);
				} else {
					door.remove();
					player.sendMessage(Text.of(TextColors.DARK_GREEN, "Broke door portal"));
				}
			}
		} finally {
			timings.onChangeBlockEventBreak().stopTiming();
		}
	}

	@Listener
	public void onChangeBlockEventPlace(ChangeBlockEvent.Place event, @First Player player) {
		timings.onChangeBlockEventPlace().startTiming();

		try {
			if (!builders.containsKey(player.getUniqueId())) {
				return;
			}

			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				BlockType blockType = transaction.getFinal().getState().getType();

				if (!blockType.equals(BlockTypes.ACACIA_DOOR) && !blockType.equals(BlockTypes.BIRCH_DOOR) && !blockType.equals(BlockTypes.DARK_OAK_DOOR) && !blockType.equals(BlockTypes.IRON_DOOR) && !blockType.equals(BlockTypes.JUNGLE_DOOR) && !blockType.equals(BlockTypes.SPRUCE_DOOR) && !blockType.equals(BlockTypes.WOODEN_DOOR)) {
					continue;
				}

				Location<World> location = transaction.getFinal().getLocation().get();

				if (!player.hasPermission("pjp.door.place")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to place door portals"));
					builders.remove(player.getUniqueId());
					event.setCancelled(true);
					return;
				}

				Door door = builders.get(player.getUniqueId());
				door.setLocation(location);
				door.create();

				Particle particle = Particles.getDefaultEffect("creation");
				particle.spawnParticle(location, false, Particles.getDefaultColor("creation", particle.isColorable()));

				player.sendMessage(Text.of(TextColors.DARK_GREEN, "New door portal created"));

				builders.remove(player.getUniqueId());
				break;
			}
		} finally {
			timings.onChangeBlockEventPlace().stopTiming();
		}
	}

	private static List<UUID> cache = new ArrayList<>();

	@Listener
	public void onDisplaceEntityEventMovePlayer(DisplaceEntityEvent.Move event) {
		timings.onDisplaceEntityEventMovePlayer().startTiming();

		try {
			Entity entity = event.getTargetEntity();

			if (!(entity instanceof Player)) {
				return;
			}
			Player player = (Player) entity;

			Location<World> location = player.getLocation();

			Optional<Door> optionalDoor = Door.get(location);

			if (!optionalDoor.isPresent()) {
				return;
			}
			Door door = optionalDoor.get();

			if (Main.getConfigManager().getConfig().getNode("options", "advanced_permissions").getBoolean()) {
				if (!player.hasPermission("pjp.door." + location.getExtent().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ())) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use this door portal"));
					return;
				}
			} else {
				if (!player.hasPermission("pjp.door.interact")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "you do not have permission to interact with door portals"));
					return;
				}
			}

			if (door.isBungee()) {
				UUID uuid = player.getUniqueId();

				if (cache.contains(uuid)) {
					return;
				}

				Consumer<String> consumer = (server) -> {
					Server teleportEvent = new TeleportEvent.Server(player, server, door.getServer(), door.getPrice(), Cause.of(NamedCause.source(door)));

					if (!Main.getGame().getEventManager().post(teleportEvent)) {
						cache.add(uuid);

						SpongyCord.API.connectPlayer(player, teleportEvent.getDestination());

						player.setLocation(player.getWorld().getSpawnLocation());

						Main.getGame().getScheduler().createTaskBuilder().delayTicks(20).execute(c -> {
							cache.remove(uuid);
						}).submit(Main.getPlugin());
					}
				};

				SpongyCord.API.getServerName(consumer, player);
			} else {
				Optional<Location<World>> optionalSpawnLocation = door.getDestination();

				if (!optionalSpawnLocation.isPresent()) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "World does not exist"));
					return;
				}
				Location<World> spawnLocation = optionalSpawnLocation.get();

				Local teleportEvent = new TeleportEvent.Local(player, player.getLocation(), spawnLocation, door.getPrice(), Cause.of(NamedCause.source(door)));

				if (!Main.getGame().getEventManager().post(teleportEvent)) {
					spawnLocation = teleportEvent.getDestination();

					Vector3d rotation = door.getRotation().toVector3d();

					player.setLocationAndRotation(spawnLocation, rotation);
				}
			}
		} finally {
			timings.onDisplaceEntityEventMovePlayer().stopTiming();
		}
	}
}
