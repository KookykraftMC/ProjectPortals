package com.gmail.trentech.pjp.utils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.trentech.pjp.Main;

public class Teleport {

	private static ConcurrentHashMap<UUID, Vector3d> cache = new ConcurrentHashMap<>();
	private static ThreadLocalRandom random = ThreadLocalRandom.current();

	public static void cacheRandom(World world) {
		Sponge.getScheduler().createTaskBuilder().execute(c -> {
			TeleportHelper teleportHelper = Sponge.getGame().getTeleportHelper();

			Location<World> spawnLocation = world.getSpawnLocation();

			int radius = Main.getConfigManager().getConfig().getNode("options", "random_spawn_radius").getInt();
			
			for(int i = 0;i < 49; i++) {
				int x = (random.nextInt(radius * 2) - radius) + spawnLocation.getBlockX();
				int y = random.nextInt(59, 200 + 1);
				int z = (random.nextInt(radius * 2) - radius) + spawnLocation.getBlockZ();		

				Optional<Location<World>> optionalLocation = teleportHelper.getSafeLocation(world.getLocation(x, y, z));

				if (!optionalLocation.isPresent()) {
					continue;
				}
				Location<World> unsafeLocation = optionalLocation.get();

				BlockType blockType = unsafeLocation.getBlockType();

				if (!blockType.equals(BlockTypes.AIR) || !unsafeLocation.getRelative(Direction.UP).getBlockType().equals(BlockTypes.AIR)) {
					continue;
				}

				BlockType floorBlockType = unsafeLocation.getRelative(Direction.DOWN).getBlockType();

				if (floorBlockType.equals(BlockTypes.WATER) || floorBlockType.equals(BlockTypes.LAVA) || floorBlockType.equals(BlockTypes.FLOWING_WATER) || floorBlockType.equals(BlockTypes.FLOWING_LAVA) || floorBlockType.equals(BlockTypes.FIRE)) {
					continue;
				}

				cache.put(world.getUniqueId(), unsafeLocation.getPosition());
				break;
			}
		}).submit(Main.getPlugin());
	}
	
	public static Location<World> getRandomLocation(World world) {
		Location<World> location = world.getLocation(cache.get(world.getUniqueId()));
		cacheRandom(world);
		return location;
	}

	public static Consumer<CommandSource> unsafe(Location<World> location) {
		return (CommandSource src) -> {
			Player player = (Player) src;
			player.setLocation(location);
		};
	}
}
