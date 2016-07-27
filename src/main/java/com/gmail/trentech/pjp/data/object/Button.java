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

public class Button extends PortalBase {

	private static ConcurrentHashMap<String, Button> cache = new ConcurrentHashMap<>();

	public Button(String destination, Rotation rotation, double price, boolean bungee) {
		super(destination, rotation, price, bungee);
	}

	public Button(String name, String destination, Rotation rotation, double price, boolean bungee) {
		super(name, destination, rotation, price, bungee);
	}

	public static Optional<Button> get(Location<World> location) {
		String name = location.getExtent().getName() + ":" + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();

		if (cache.containsKey(name)) {
			return Optional.of(cache.get(name));
		}

		return Optional.empty();
	}

	public void create() {
		try {
			Connection connection = getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("INSERT into Buttons (Name, Button) VALUES (?, ?)");

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

			PreparedStatement statement = connection.prepareStatement("UPDATE Buttons SET Button = ? WHERE Name = ?");

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

			PreparedStatement statement = connection.prepareStatement("DELETE from Buttons WHERE Name = ?");

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

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM Buttons");

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String name = result.getString("Name");

				Button button = Serializer.deserializeButton(result.getString("Button"));
				button.setName(name);

				cache.put(name, button);
			}

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
