package com.gmail.trentech.pjp.utils;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;

public class Utils {

	public static Location<World> getRandomLocation(World world) {
		TeleportHelper teleportHelper = Main.getGame().getTeleportHelper();

		ThreadLocalRandom random = ThreadLocalRandom.current();

		Location<World> location = world.getSpawnLocation();

		long radius = new ConfigManager().getConfig().getNode("options", "random_spawn_radius").getLong();

		for (int i = 0; i < 49; i++) {
			int x = (int) (random.nextDouble() * ((radius * 2) + 1) - radius);
			int y = random.nextInt(64, 200 + 1);
			int z = (int) (random.nextDouble() * ((radius * 2) + 1) - radius);

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

			location = unsafeLocation;
			break;
		}
		return location;
	}

	public static Consumer<CommandSource> unsafeTeleport(Location<World> location) {
		return (CommandSource src) -> {
			Player player = (Player) src;

			player.setLocation(location);
		};
	}
}
