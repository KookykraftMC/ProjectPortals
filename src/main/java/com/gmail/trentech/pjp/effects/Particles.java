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
	SMOKE_LARGE(new Particle("SMOKE_LARGE", ParticleTypes.SMOKE_LARGE, 60)), 
	ENCHANTMENT_TABLE(new Particle("ENCHANTMENT_TABLE", ParticleTypes.ENCHANTMENT_TABLE, 10)), 
	VILLAGER_HAPPY(new Particle("VILLAGER_HAPPY", ParticleTypes.VILLAGER_HAPPY, 50)), 
	SPELL_WITCH(new Particle("SPELL_WITCH", ParticleTypes.SPELL_WITCH, 15)), 
	NOTE(new Particle("NOTE", ParticleTypes.NOTE, 80)), 
	REDSTONE(new Particle("REDSTONE", ParticleTypes.REDSTONE, 15)), 
	SPELL(new Particle("SPELL", ParticleTypes.SPELL, 15)), 
	WATER_BUBBLE(new Particle("WATER_BUBBLE", ParticleTypes.WATER_BUBBLE, 5)), 
	CRIT_MAGIC(new Particle("CRIT_MAGIC", ParticleTypes.CRIT_MAGIC, 15)), 
	CRIT(new Particle("CRIT", ParticleTypes.CRIT, 15)), 
	SNOWBALL(new Particle("SNOWBALL", ParticleTypes.SNOWBALL, 15)), 
	SLIME(new Particle("SLIME", ParticleTypes.SLIME, 15)), 
	SNOW_SHOVEL(new Particle("SNOW_SHOVEL", ParticleTypes.SNOW_SHOVEL, 10)), 
	SUSPENDED_DEPTH(new Particle("SUSPENDED_DEPTH", ParticleTypes.SUSPENDED_DEPTH, 5)), 
	VILLAGER_ANGRY(new Particle("VILLAGER_ANGRY", ParticleTypes.VILLAGER_ANGRY, 40)), 
	WATER_SPLASH(new Particle("WATER_SPLASH", ParticleTypes.WATER_SPLASH, 7)), 
	WATER_WAKE(new Particle("WATER_WAKE", ParticleTypes.WATER_WAKE, 7)), 
	WATER_DROP(new Particle("WATER_DROP", ParticleTypes.WATER_DROP, 7)), 
	PORTAL2(new Particle("PORTAL2", ParticleTypes.PORTAL, 30)), 
	NONE(new Particle("NONE", null, 0));

	private final Particle particle;

	private Particles(Particle particle) {
		this.particle = particle;
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
		ConfigurationNode node = new ConfigManager().getConfig().getNode("options", "particles", key);

		if (node.isVirtual()) {
			Main.getLog().warn("Can't find config node for " + key);
			return get("BARRIER").get();
		}
		String type = node.getNode("type").getString().toUpperCase();

		Optional<Particle> optionalParticle = get(type);

		if (!optionalParticle.isPresent()) {
			Main.getLog().warn("Can't find particle type for " + type);
			return get("BARRIER").get();
		}
		Particle particle = optionalParticle.get();

		return particle;
	}

	public static Optional<ParticleColor> getDefaultColor(String key, boolean colorable) {
		if (!colorable) {
			return Optional.empty();
		}

		ConfigurationNode node = new ConfigManager().getConfig().getNode("options", "particles", key);

		if (node.isVirtual()) {
			Main.getLog().warn("Can't find config node for " + key);
			return Optional.empty();
		}
		String color = node.getNode("color").getString().toUpperCase();

		return ParticleColor.get(color);
	}
}
