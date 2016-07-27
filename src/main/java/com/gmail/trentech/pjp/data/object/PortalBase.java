package com.gmail.trentech.pjp.data.object;

import java.util.Optional;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.DataQueries;
import com.gmail.trentech.pjp.utils.Rotation;
import com.gmail.trentech.pjp.utils.SQLUtils;
import com.gmail.trentech.pjp.utils.Utils;

public class PortalBase extends SQLUtils implements DataSerializable {

	protected String name;
	protected String destination;
	protected Rotation rotation;
	protected double price;
	protected boolean bungee;

	protected PortalBase(String destination, Rotation rotation, double price, boolean bungee) {
		this.destination = destination;
		this.rotation = rotation;
		this.price = price;
		this.bungee = bungee;
	}

	protected PortalBase(String name, String destination, Rotation rotation, double price, boolean bungee) {
		this.name = name;
		this.destination = destination;
		this.rotation = rotation;
		this.price = price;
		this.bungee = bungee;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public Optional<Location<World>> getLocation() {
		String[] args = name.split(":");

		Optional<World> optionalWorld = Main.getGame().getServer().getWorld(args[0]);

		if (!optionalWorld.isPresent()) {
			return Optional.empty();
		}
		World world = optionalWorld.get();

		String[] coords = args[1].split("\\.");

		int x = Integer.parseInt(coords[0]);
		int y = Integer.parseInt(coords[1]);
		int z = Integer.parseInt(coords[2]);

		return Optional.of(world.getLocation(x, y, z));
	}

	public void setLocation(Location<World> location) {
		String name = location.getExtent().getName() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}

	public Optional<Location<World>> getDestination() {
		String[] args = destination.split(":");

		Optional<World> optionalWorld = Main.getGame().getServer().getWorld(args[0]);

		if (!optionalWorld.isPresent()) {
			return Optional.empty();
		}
		World world = optionalWorld.get();

		if (args[1].equalsIgnoreCase("random")) {
			return Optional.of(Utils.getRandomLocation(world));
		} else if (args[1].equalsIgnoreCase("spawn")) {
			return Optional.of(world.getSpawnLocation());
		} else {
			String[] coords = args[1].split("\\.");

			int x = Integer.parseInt(coords[0]);
			int y = Integer.parseInt(coords[1]);
			int z = Integer.parseInt(coords[2]);

			return Optional.of(world.getLocation(x, y, z));
		}
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isBungee() {
		return bungee;
	}

	public String getServer() {
		return destination;
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return new MemoryDataContainer().set(DataQueries.DESTINATION, destination).set(DataQueries.ROTATION, rotation.getName()).set(DataQueries.PRICE, price).set(DataQueries.BUNGEE, bungee);
	}

}
