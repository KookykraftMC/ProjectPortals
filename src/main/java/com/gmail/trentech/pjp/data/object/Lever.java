package com.gmail.trentech.pjp.data.object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.utils.Rotation;
import com.gmail.trentech.pjp.utils.Serializer;

public class Lever extends PortalBase {

	private static ConcurrentHashMap<String, Lever> cache = new ConcurrentHashMap<>();

	public Lever(String destination, Rotation rotation, double price, boolean bungee) {
		super(destination, rotation, price, bungee);
	}

	public Lever(String name, String destination, Rotation rotation, double price, boolean bungee) {
		super(name, destination, rotation, price, bungee);
	}

	public static Optional<Lever> get(Location<World> location) {
		String name = location.getExtent().getName() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();

		if (cache.containsKey(name)) {
			return Optional.of(cache.get(name));
		}

		return Optional.empty();
	}

	public void create() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("INSERT into Levers (Name, Lever) VALUES (?, ?)");

			statement.setString(1, name);
			statement.setString(2, Serializer.serialize(this));

			statement.executeUpdate();

			connection.close();

			cache.put(name, this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("UPDATE Levers SET Lever = ? WHERE Name = ?");

			statement.setString(1, Serializer.serialize(this));
			statement.setString(2, name);

			statement.executeUpdate();

			connection.close();

			cache.put(name, this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void remove() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("DELETE from Levers WHERE Name = ?");

			statement.setString(1, name);
			statement.executeUpdate();

			connection.close();

			cache.remove(name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM Levers");

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String name = result.getString("Name");

				Lever lever = Serializer.deserializeLever(result.getString("Lever"));
				lever.setName(name);

				cache.put(name, lever);
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
