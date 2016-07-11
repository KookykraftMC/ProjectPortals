package com.gmail.trentech.pjp.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.ParticleColor;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.events.ConstructPortalEvent;

public class PortalBuilder {

	private boolean valid = false;
	private boolean validTop = false;
	private List<Location<World>> frameList = new ArrayList<>();
	private List<Location<World>> fillList = new ArrayList<>();

	public PortalBuilder(Location<World> location, Direction direction) {
		frameList.add(location);

		scanTopOrBottom(location, direction, false);

		for (int i = 1; i < 50; ++i) {
			location = location.getRelative(Direction.UP);
			
			if (!scanLine(location, direction, i == 1)) {
				break;
			}
		}

		if (validTop) {
			scanTopOrBottom(location, direction, true);
		}
		valid = validTop;

		for (Location<World> loc : fillList) {
			List<Location<World>> list = new ArrayList<>();

			list.add(loc.getRelative(Direction.UP));
			list.add(loc.getRelative(Direction.DOWN));
			list.add(loc.getRelative(direction));
			list.add(loc.getRelative(direction.getOpposite()));

			for (Location<World> loc2 : list) {
				BlockState state = loc2.getBlock();

				if ((state.getType().equals(BlockTypes.AIR))) {
					if (!fillList.contains(loc2)) {
						valid = false;
						break;
					}
				}
			}
		}
	}

	public boolean spawnPortal(PortalProperties properties) {
		if (valid) {
			if (!Main.getGame().getEventManager().post(new ConstructPortalEvent(frameList, fillList, Cause.of(NamedCause.source(this))))) {
				BlockState block = BlockTypes.AIR.getDefaultState();

				Particle effect = Particles.getDefaultEffect("creation");
				Optional<ParticleColor> effectColor = Particles.getDefaultColor("creation", properties.getParticle().isColorable());

				for (Location<World> location : fillList) {
					effect.spawnParticle(location, false, effectColor);
					location.getExtent().setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), block, false, Cause.of(NamedCause.source(Main.getPlugin())));
				}

				new com.gmail.trentech.pjp.data.object.Portal(properties.getName(), properties.getDestination(), properties.getRotation(), frameList, fillList, properties.getParticle(), properties.getColor(), properties.getPrice(), properties.isBungee()).create();

				return true;
			}
		}

		return false;
	}

	private void scanTopOrBottom(Location<World> location, Direction direction, boolean top) {
		Location<World> loc = location;
		Direction vertical = Direction.UP;
		if (top) {
			vertical = Direction.DOWN;
		}

		for (int i = 1; i < 25; ++i) {
			BlockType blockType = loc.getBlock().getType();

			if (blockType.equals(BlockTypes.AIR)) {
				break;
			}

			frameList.add(loc);

			if (!loc.getRelative(vertical).getBlock().getType().equals(BlockTypes.AIR)) {
				break;
			}

			loc = loc.getRelative(direction);
		}

		loc = location;

		for (int i = 1; i < 25; ++i) {
			BlockType blockType = loc.getBlock().getType();

			if (blockType.equals(BlockTypes.AIR)) {
				break;
			}

			frameList.add(loc);

			if (!loc.getRelative(vertical).getBlock().getType().equals(BlockTypes.AIR)) {
				break;
			}

			loc = loc.getRelative(direction.getOpposite());
		}
	}

	private boolean scanDirection(Location<World> location, Direction direction, boolean first) {
		boolean valid = true;

		for (int i = 1; i < 25; ++i) {
			BlockState state = location.getBlock();

			if (state.getType().equals(BlockTypes.AIR)) {
				fillList.add(location);
				if (first) {
					if (location.getRelative(Direction.DOWN).getBlock().getType().equals(BlockTypes.AIR)) {
						validTop = false;
						return false;
					}
				} else if (location.getRelative(Direction.UP).getBlock().getType().equals(BlockTypes.AIR)) {
					valid = false;
				}

				location = location.getRelative(direction);
			} else {
				if (i != 1) {
					frameList.add(location);
					break;
				} else {
					return false;
				}
			}
		}
		validTop = valid;
		return true;
	}

	private boolean scanLine(Location<World> location, Direction direction, boolean first) {
		if (scanDirection(location, direction.getOpposite(), first)) {
			if (!validTop) {
				if (scanDirection(location, direction, first)) {
					validTop = false;
					return true;
				}
				validTop = false;
			} else {
				if (scanDirection(location, direction, first)) {
					return true;
				}
			}
		}
		return false;
	}
}