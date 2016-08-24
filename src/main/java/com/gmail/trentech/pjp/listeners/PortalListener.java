package com.gmail.trentech.pjp.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.effects.ParticleColor;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.events.ConstructPortalEvent;
import com.gmail.trentech.pjp.events.TeleportEvent;
import com.gmail.trentech.pjp.events.TeleportEvent.Local;
import com.gmail.trentech.pjp.events.TeleportEvent.Server;
import com.gmail.trentech.pjp.portal.PortalProperties;
import com.gmail.trentech.pjp.utils.PlayerDirection;
import com.gmail.trentech.pjp.utils.Rotation;

import flavor.pie.spongycord.SpongyCord;
import ninja.leaping.configurate.ConfigurationNode;

public class PortalListener {

	public static ConcurrentHashMap<UUID, PortalProperties> props = new ConcurrentHashMap<>();

	private Timings timings;

	public PortalListener(Timings timings) {
		this.timings = timings;
	}
	
	@Listener
	public void onTabCompleteEvent(TabCompleteEvent event, @First Player player) {
		String rawMessage = event.getRawMessage();
		
		String[] args = rawMessage.split(" ");
		
		if(args.length <= 1) {
			return;
		}

		if(!args[0].equalsIgnoreCase("portal") && !args[0].equalsIgnoreCase("p")) {
			return;
		}
		
		List<String> list = event.getTabCompletions();
		
		if(args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("c")) {
			if(args[args.length - 1].equalsIgnoreCase("-d") || args[args.length - 2].equalsIgnoreCase("-d")) {
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
			} else if(args[args.length - 1].equalsIgnoreCase("-e") || args[args.length - 2].equalsIgnoreCase("-e")) {
				for (Particles particle : Particles.values()) {
					String id = particle.name();
					
					if(args[args.length - 2].equalsIgnoreCase("-e")) {
						if(id.contains(args[args.length - 1].toUpperCase()) && !id.equalsIgnoreCase(args[args.length - 1])) {
							list.add(id);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(id);
					}
				}
			}
		} else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("r")) {
			for(WorldProperties world : Sponge.getServer().getAllWorldProperties()) {
				String name = world.getWorldName();
				
				if(args.length == 3) {
					if(name.contains(args[2].toLowerCase()) && !name.equalsIgnoreCase(args[2])) {
						list.add(name);
					}
				} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
					list.add(name);
				}
			}
		} else if(args[1].equalsIgnoreCase("particle") || args[1].equalsIgnoreCase("p")) {
			if(args.length == 2 || args.length == 3) {
				for(WorldProperties world : Sponge.getServer().getAllWorldProperties()) {
					String name = world.getWorldName();
					
					if(args.length == 3) {
						if(name.contains(args[2].toLowerCase()) && !name.equalsIgnoreCase(args[2])) {
							list.add(name);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(name);
					}
				}
			}
			if(args.length == 3 || args.length == 4) {
				for(Particles particle : Particles.values()) {
					String name = particle.name();
					
					if(args.length == 4) {
						if(name.contains(args[3].toUpperCase()) && !name.equalsIgnoreCase(args[3])) {
							list.add(name);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(name);
					}
				}
			}
			if(args.length == 4 || args.length == 5) {
				for(ParticleColor color : ParticleColor.values()) {
					String name = color.getName();
					
					if(args.length == 5) {
						if(name.contains(args[4].toUpperCase()) && !name.equalsIgnoreCase(args[4])) {
							list.add(name);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(name);
					}
				}
			}
		} else if(args[1].equalsIgnoreCase("price") || args[1].equalsIgnoreCase("pr")) {
			if(args.length == 2 || args.length == 3) {
				for(WorldProperties world : Sponge.getServer().getAllWorldProperties()) {
					String name = world.getWorldName();
					
					if(args.length == 3) {
						if(name.contains(args[2].toLowerCase()) && !name.equalsIgnoreCase(args[2])) {
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
	@Exclude(value = { ChangeBlockEvent.Place.class })
	public void onInteractBlockEventSecondary(InteractBlockEvent.Secondary event, @First Player player) {
		timings.onInteractBlockEventSecondary().startTiming();

		try {
			if (!props.containsKey(player.getUniqueId())) {
				return;
			}
			PortalProperties properties = props.get(player.getUniqueId());

			if (player.getItemInHand().isPresent()) {
				player.sendMessage(Text.of(TextColors.YELLOW, "Hand must be empty"));
				return;
			}

			Optional<Location<World>> optionalLocation = event.getTargetBlock().getLocation();

			if (!optionalLocation.isPresent()) {
				return;
			}
			Location<World> location = optionalLocation.get();

			Direction direction = PlayerDirection.getClosest(player.getRotation().getFloorY()).getDirection();

			com.gmail.trentech.pjp.portal.PortalBuilder builder = new com.gmail.trentech.pjp.portal.PortalBuilder(location, direction);

			if (!builder.spawnPortal(properties)) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid portal shape"));
				return;
			}

			Main.getGame().getScheduler().createTaskBuilder().delayTicks(20).execute(t -> {
				props.remove(player.getUniqueId());
			}).submit(Main.getPlugin());

			player.sendMessage(Text.of(TextColors.DARK_GREEN, "Portal ", properties.getName(), " created successfully"));
		} finally {
			timings.onInteractBlockEventSecondary().stopTiming();
		}
	}

	@Listener
	public void onConstructPortalEvent(ConstructPortalEvent event, @First Player player) {
		timings.onConstructPortalEvent().startTiming();

		try {
			List<Location<World>> locations = event.getLocations();

			for (Location<World> location : event.getLocations()) {
				if (Portal.get(location).isPresent()) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "Portals cannot over lap other portals"));
					event.setCancelled(true);
					return;
				}
			}

			ConfigurationNode config = Main.getConfigManager().getConfig();

			int size = config.getNode("options", "portal", "size").getInt();
			if (locations.size() > size) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Portals cannot be larger than ", size, " blocks"));
				event.setCancelled(true);
				return;
			}

			if (locations.size() < 9) {
				player.sendMessage(Text.of(TextColors.DARK_RED, "Portal too small"));
				event.setCancelled(true);
				return;
			}
		} finally {
			timings.onConstructPortalEvent().stopTiming();
		}
	}

	@Listener
	public void onDisplaceEntityEventMoveItem(DisplaceEntityEvent.Move event) {
		Entity entity = event.getTargetEntity();

		if (!(entity instanceof Item)) {
			return;
		}

		timings.onDisplaceEntityEventMoveItem().startTiming();

		try {
			Location<World> location = entity.getLocation();

			Optional<Portal> optionalPortal = Portal.get(location);

			if (!optionalPortal.isPresent()) {
				return;
			}
			Portal portal = optionalPortal.get();

			if (portal.isBungee()) {
				return;
			}

			if (!Main.getConfigManager().getConfig().getNode("options", "portal", "teleport_item").getBoolean()) {
				return;
			}

			Optional<Location<World>> optionalSpawnLocation = portal.getDestination();

			if (!optionalSpawnLocation.isPresent()) {
				return;
			}
			Location<World> spawnLocation = optionalSpawnLocation.get();

			entity.setLocationAndRotation(spawnLocation, portal.getRotation().toVector3d());
		} finally {
			timings.onDisplaceEntityEventMoveItem().stopTiming();
		}
	}

	@Listener
	public void onDisplaceEntityEventMoveLiving(DisplaceEntityEvent.Move event) {
		Entity entity = event.getTargetEntity();

		if (!(entity instanceof Living) || entity instanceof Player) {
			return;
		}

		timings.onDisplaceEntityEventMoveLiving().startTiming();

		try {
			Location<World> location = entity.getLocation();

			Optional<Portal> optionalPortal = Portal.get(location);

			if (!optionalPortal.isPresent()) {
				return;
			}
			Portal portal = optionalPortal.get();

			if (portal.isBungee()) {
				return;
			}

			if (!Main.getConfigManager().getConfig().getNode("options", "portal", "teleport_mob").getBoolean()) {
				return;
			}

			Optional<Location<World>> optionalSpawnLocation = portal.getDestination();

			if (!optionalSpawnLocation.isPresent()) {
				return;
			}
			Location<World> spawnLocation = optionalSpawnLocation.get();

			entity.setLocationAndRotation(spawnLocation, portal.getRotation().toVector3d());
		} finally {
			timings.onDisplaceEntityEventMoveLiving().stopTiming();
		}
	}

	private static List<UUID> cache = new ArrayList<>();

	@Listener
	public void onDisplaceEntityEventMovePlayer(DisplaceEntityEvent.Move event) {
		Entity entity = event.getTargetEntity();

		if (!(entity instanceof Player)) {
			return;
		}
		Player player = (Player) entity;

		timings.onDisplaceEntityEventMovePlayer().startTiming();

		try {
			Location<World> location = event.getFromTransform().getLocation();

			Optional<Portal> optionalPortal = Portal.get(location);

			if (!optionalPortal.isPresent()) {
				return;
			}
			Portal portal = optionalPortal.get();

			if (Main.getConfigManager().getConfig().getNode("options", "advanced_permissions").getBoolean()) {
				if (!player.hasPermission("pjp.portal." + portal.getName())) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use this portal"));
					return;
				}
			} else {
				if (!player.hasPermission("pjp.portal.interact")) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use portals"));
					return;
				}
			}

			if (portal.isBungee()) {
				UUID uuid = player.getUniqueId();

				if (cache.contains(uuid)) {
					return;
				}

				Consumer<String> consumer = (server) -> {
					Server teleportEvent = new TeleportEvent.Server(player, server, portal.getServer(), portal.getPrice(), Cause.of(NamedCause.source(portal)));

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
				Optional<Location<World>> optionalSpawnLocation = portal.getDestination();

				if (!optionalSpawnLocation.isPresent()) {
					player.sendMessage(Text.of(TextColors.DARK_RED, "Spawn location does not exist or world is not loaded"));
					return;
				}
				Location<World> spawnLocation = optionalSpawnLocation.get();

				Local teleportEvent = new TeleportEvent.Local(player, player.getLocation(), spawnLocation, portal.getPrice(), Cause.of(NamedCause.source(portal)));

				if (!Main.getGame().getEventManager().post(teleportEvent)) {
					spawnLocation = teleportEvent.getDestination();

					Vector3d rotation = portal.getRotation().toVector3d();

					player.setLocationAndRotation(spawnLocation, rotation);
				}
			}
		} finally {
			timings.onDisplaceEntityEventMovePlayer().stopTiming();
		}
	}

	@Listener
	public void onChangeBlockEventPlace(ChangeBlockEvent.Place event, @First Player player) {
		timings.onChangeBlockEventPlace().startTiming();

		try {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				Location<World> location = transaction.getFinal().getLocation().get();

				if (!Portal.get(location).isPresent()) {
					continue;
				}

				event.setCancelled(true);
				break;
			}
		} finally {
			timings.onChangeBlockEventPlace().stopTiming();
		}
	}

	@Listener
	public void onChangeBlockEventBreak(ChangeBlockEvent.Break event, @First Player player) {
		timings.onChangeBlockEventBreak().startTiming();

		try {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				Location<World> location = transaction.getFinal().getLocation().get();

				if (!Portal.get(location).isPresent()) {
					continue;
				}

				event.setCancelled(true);
				break;
			}
		} finally {
			timings.onChangeBlockEventBreak().stopTiming();
		}
	}
}
