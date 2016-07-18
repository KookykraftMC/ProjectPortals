package com.gmail.trentech.pjp.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.ParticleColor;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.events.ConstructPortalEvent;
import com.gmail.trentech.pjp.utils.Rotation;

public class LegacyBuilder {

	private final String name;
	private final String destination;
	private final Rotation rotation;
	private final double price;
	private final boolean bungee;
	private final Particle particle;
	private final Optional<ParticleColor> color;
	private boolean fill = false;
	private List<Location<World>> regionFrame;
	private List<Location<World>> regionFill;

	public LegacyBuilder(String name, String destination, Rotation rotation, Particle particle, Optional<ParticleColor> color, double price, boolean bungee) {
		this.name = name;
		this.destination = destination;
		this.rotation = rotation;
		this.price = price;
		this.bungee = bungee;
		this.particle = particle;
		this.color = color;
		this.regionFrame = new ArrayList<>();
		this.regionFill = new ArrayList<>();
	}

	public List<Location<World>> getRegionFill() {
		return regionFill;
	}

	public List<Location<World>> getRegionFrame() {
		return regionFrame;
	}

	public String getName() {
		return name;
	}

	public LegacyBuilder addFrame(Location<World> location) {
		regionFrame.add(location);
		return this;
	}

	public LegacyBuilder removeFrame(Location<World> location) {
		regionFrame.remove(location);
		return this;
	}

	public LegacyBuilder addFill(Location<World> location) {
		regionFill.add(location);
		return this;
	}

	public LegacyBuilder removeFill(Location<World> location) {
		regionFill.remove(location);
		return this;
	}

	public boolean isFill() {
		return fill;
	}

	public LegacyBuilder setFill(boolean fill) {
		this.fill = fill;
		return this;
	}

	public boolean build() {
		if (regionFrame.isEmpty() || regionFill.isEmpty()) {
			return false;
		}

		if (!Main.getGame().getEventManager().post(new ConstructPortalEvent(regionFrame, regionFill, Cause.of(NamedCause.source(this))))) {
			BlockState block = BlockTypes.AIR.getDefaultState();

			Particle particle = Particles.getDefaultEffect("creation");
			Optional<ParticleColor> color = Particles.getDefaultColor("creation", particle.isColorable());

			for (Location<World> location : regionFill) {
				particle.spawnParticle(location, false, color);
				location.getExtent().setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), block, false, Cause.of(NamedCause.source(Main.getPlugin())));
			}

			new Portal(getName(), destination, rotation, regionFrame, regionFill, this.particle, this.color, price, bungee).create();

			return true;
		}
		return false;
	}
}
