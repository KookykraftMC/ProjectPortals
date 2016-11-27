package com.gmail.trentech.pjp.effects;

import java.util.Optional;

import org.spongepowered.api.effect.particle.ParticleTypes;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public enum Particles {

	PORTAL(new Particle("PORTAL", ParticleTypes.PORTAL, 10)),
	FLAME(new Particle("FLAME", ParticleTypes.FLAME, 40)),
	CLOUD(new Particle("CLOUD", ParticleTypes.CLOUD, 60)),
	HEART(new Particle("HEART", ParticleTypes.HEART, 120)),
	LARGE_SMOKE(new Particle("LARGE_SMOKE", ParticleTypes.LARGE_SMOKE, 60)),
	ENCHANTING_GLYPHS(new Particle("ENCHANTING_GLYPHS", ParticleTypes.ENCHANTING_GLYPHS, 10)),
	HAPPY_VILLAGER(new Particle("HAPPY_VILLAGER", ParticleTypes.HAPPY_VILLAGER, 50)),
	WITCH_SPELL(new Particle("WITCH_SPELL", ParticleTypes.WITCH_SPELL, 15)),
	NOTE(new Particle("NOTE", ParticleTypes.NOTE, 80)),
	REDSTONE_DUST(new Particle("REDSTONE_DUST", ParticleTypes.REDSTONE_DUST, 15)),
	SPELL(new Particle("SPELL", ParticleTypes.SPELL, 15)),
	WATER_BUBBLE(new Particle("WATER_BUBBLE", ParticleTypes.WATER_BUBBLE, 5)),
	MAGIC_CRITICAL_HIT(new Particle("MAGIC_CRITICAL_HIT", ParticleTypes.MAGIC_CRITICAL_HIT, 15)),
	CRITICAL_HIT(new Particle("CRIT", ParticleTypes.CRITICAL_HIT, 15)),
	SNOWBALL(new Particle("SNOWBALL", ParticleTypes.SNOWBALL, 15)),
	SLIME(new Particle("SLIME", ParticleTypes.SLIME, 15)),
	SNOW_SHOVEL(new Particle("SNOW_SHOVEL", ParticleTypes.SNOW_SHOVEL, 10)),
	SUSPENDED_DEPTH(new Particle("SUSPENDED_DEPTH", ParticleTypes.SUSPENDED_DEPTH, 5)),
	ANGRY_VILLAGER(new Particle("ANGRY_VILLAGER", ParticleTypes.ANGRY_VILLAGER, 40)),
	WATER_SPLASH(new Particle("WATER_SPLASH", ParticleTypes.WATER_SPLASH, 7)),
	WATER_WAKE(new Particle("WATER_WAKE", ParticleTypes.WATER_WAKE, 7)),
	WATER_DROP(new Particle("WATER_DROP", ParticleTypes.WATER_DROP, 7)),
	PORTAL2(new Particle("PORTAL2", ParticleTypes.PORTAL, 30)),
	NONE(new Particle("NONE", null, 0));

	private final Particle particle;

	Particles(Particle particle) {
		this.particle = particle;
	}

	public Particle getParticle() {
		return particle;
	}

	public static Optional<Particle> get(String name) {
		Optional<Particle> optional = Optional.empty();

		Particles[] particles = Particles.values();

		for (Particles particle : particles) {
			if (particle.particle.getName().equals(name)) {
				optional = Optional.of(particle.particle);
				break;
			}
		}

		return optional;
	}

	public static Particle getDefaultEffect(String key) {
		ConfigurationNode node = ConfigManager.get().getConfig().getNode("options", "particles", key);

		if (node.isVirtual()) {
			Main.instance().getLog().warn("Can't find config node for " + key);
			return get("BARRIER").get();
		}
		String type = node.getNode("type").getString().toUpperCase();

		Optional<Particle> optionalParticle = get(type);

		if (!optionalParticle.isPresent()) {
			Main.instance().getLog().warn("Can't find particle type for " + type);
			return get("BARRIER").get();
		}

		return optionalParticle.get();
	}

	public static Optional<ParticleColor> getDefaultColor(String key, boolean colorable) {
		if (!colorable) {
			return Optional.empty();
		}

		ConfigurationNode node = ConfigManager.get().getConfig().getNode("options", "particles", key);

		if (node.isVirtual()) {
			Main.instance().getLog().warn("Can't find config node for " + key);
			return Optional.empty();
		}
		String color = node.getNode("color").getString().toUpperCase();

		return ParticleColor.get(color);
	}
}
