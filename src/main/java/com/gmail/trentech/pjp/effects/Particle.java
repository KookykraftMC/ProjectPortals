package com.gmail.trentech.pjp.effects;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.effect.particle.ColoredParticle;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleType.Colorable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.utils.ConfigManager;

public class Particle {

	private final String name;
	private final ParticleType type;
	private final long time;

	private static ThreadLocalRandom random = ThreadLocalRandom.current();

	protected Particle(String name, ParticleType type, long time) {
		this.name = name;
		this.type = type;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public ParticleType getType() {
		return type;
	}

	private long getTime() {
		return time;
	}

	public boolean isColorable() {
		if (getType() == null) {
			return false;
		}

		if (getType() instanceof Colorable) {
			return true;
		}
		return false;
	}

	public void spawnParticle(Location<World> location, boolean player, Optional<ParticleColor> color) {
		if (getType() == null) {
			return;
		}

		if (new ConfigManager().getConfig().getNode("options", "particles", "enable").getBoolean()) {
			spawnNonRepeat(location, player, color);
		}
	}

	public void createTask(String name, List<Location<World>> locations, Optional<ParticleColor> color) {
		if (getType() == null) {
			return;
		}

		if (new ConfigManager().getConfig().getNode("options", "particles", "enable").getBoolean()) {
			spawnRepeat(name, locations, color);
		}
	}

	private void spawnNonRepeat(Location<World> location, boolean player, Optional<ParticleColor> color) {
		if (getType() == null) {
			return;
		}

		ParticleEffect particle = ParticleEffect.builder().type(getType()).build();

		for (int i = 0; i < 9; i++) {
			if (isColorable() && color.isPresent()) {
				particle = ColoredParticle.builder().color(color.get().getColor()).type((Colorable) getType()).build();
			}

			if (player) {
				location.getExtent().spawnParticles(particle, location.getPosition().add(random.nextDouble() - .5, random.nextDouble() - .5, random.nextDouble() - .5));
				location.getExtent().spawnParticles(particle, location.getPosition().add(random.nextDouble() - .5, random.nextDouble() - .5, random.nextDouble() - .5));
			} else {
				location.getExtent().spawnParticles(particle, location.getPosition().add(random.nextDouble(), random.nextDouble(), random.nextDouble()));
				location.getExtent().spawnParticles(particle, location.getPosition().add(random.nextDouble(), random.nextDouble(), random.nextDouble()));
			}
		}
	}

	private void spawnRepeat(String name, List<Location<World>> locations, Optional<ParticleColor> color) {
		if (getType() == null) {
			return;
		}

		if (getName().equals("PORTAL2")) {
			Portal portal = Portal.get(locations.get(0)).get();

			Main.getGame().getScheduler().createTaskBuilder().intervalTicks(getTime()).name(name).execute(t -> {
				portal.update(false);
			}).submit(Main.getPlugin());
		} else {
			Main.getGame().getScheduler().createTaskBuilder().interval(getTime(), TimeUnit.MILLISECONDS).name(name).execute(t -> {
				ParticleEffect particle = ParticleEffect.builder().type(getType()).build();

				for (Location<World> location : locations) {
					if (isColorable() && color.isPresent()) {
						particle = ColoredParticle.builder().color(color.get().getColor()).type((Colorable) getType()).build();
					}

					location.getExtent().spawnParticles(particle, location.getPosition().add(random.nextDouble(), random.nextDouble(), random.nextDouble()));
					location.getExtent().spawnParticles(particle, location.getPosition().add(random.nextDouble(), random.nextDouble(), random.nextDouble()));
				}
			}).submit(Main.getPlugin());
		}
	}
}
