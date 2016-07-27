package com.gmail.trentech.pjp.portal;

import java.util.Optional;

import com.gmail.trentech.pjp.effects.Particle;
import com.gmail.trentech.pjp.effects.ParticleColor;
import com.gmail.trentech.pjp.utils.Rotation;

public class PortalProperties {

	private final String name;
	private final String destination;
	private final Rotation rotation;
	private final double price;
	private final boolean bungee;
	private final Particle particle;
	private final Optional<ParticleColor> color;

	public PortalProperties(String name, String destination, Rotation rotation, Particle particle, Optional<ParticleColor> color, double price, boolean bungee) {
		this.name = name;
		this.destination = destination;
		this.rotation = rotation;
		this.price = price;
		this.bungee = bungee;
		this.particle = particle;
		this.color = color;
	}

	public String getDestination() {
		return destination;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public double getPrice() {
		return price;
	}

	public boolean isBungee() {
		return bungee;
	}

	public Particle getParticle() {
		return particle;
	}

	public Optional<ParticleColor> getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
}
