package com.gmail.trentech.pjp.utils;

import java.io.File;
import java.io.IOException;

import com.gmail.trentech.pjp.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigManager {

	private File file;
	private CommentedConfigurationNode config;
	private ConfigurationLoader<CommentedConfigurationNode> loader;

	public ConfigManager(String folder, String configName) {
		folder = "config" + File.separator + "projectportals" + File.separator + folder;
		if (!new File(folder).isDirectory()) {
			new File(folder).mkdirs();
		}
		file = new File(folder, configName);

		create();
		load();
	}

	public ConfigManager(String configName) {
		String folder = "config" + File.separator + "projectportals";
		if (!new File(folder).isDirectory()) {
			new File(folder).mkdirs();
		}
		file = new File(folder, configName);

		create();
		load();
	}

	public ConfigManager() {
		String folder = "config" + File.separator + "projectportals";
		if (!new File(folder).isDirectory()) {
			new File(folder).mkdirs();
		}
		file = new File(folder, "config.conf");

		create();
		load();
	}

	public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
		return loader;
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	public void init() {
		if (file.getName().equalsIgnoreCase("config.conf")) {
			if (config.getNode("options", "portal", "size").isVirtual()) {
				config.getNode("options", "portal", "size").setValue(100).setComment("Maximum number of blocks a portal can use");
			}
			if (config.getNode("options", "portal", "legacy_builder").isVirtual()) {
				config.getNode("options", "portal", "legacy_builder").setValue(true).setComment("Use legacy portal builder");
			}
			if (config.getNode("options", "portal", "teleport_item").isVirtual()) {
				config.getNode("options", "portal", "teleport_item").setValue(true).setComment("Toggle if portals can teleport items");
			}
			if (config.getNode("options", "portal", "teleport_mob").isVirtual()) {
				config.getNode("options", "portal", "teleport_mob").setValue(true).setComment("Toggle if portals can teleport mobs");
			}
			if (config.getNode("options", "homes").isVirtual()) {
				config.getNode("options", "homes").setValue(5).setComment("Default number of homes a player can have");
			}
			if (config.getNode("options", "particles").isVirtual()) {
				config.getNode("options", "particles").setComment("Particle effect settings");
				config.getNode("options", "particles", "enable").setValue(true).setComment("Enable particle effects");
				config.getNode("options", "particles", "portal", "type").setValue("PORTAL2").setComment("Default particle type for portals");
				config.getNode("options", "particles", "portal", "color").setValue("NONE").setComment("Default Color of Particle if supported, otherwise set \"NONE\"");
				config.getNode("options", "particles", "teleport", "type").setValue("REDSTONE").setComment("Default particle type when teleporting");
				config.getNode("options", "particles", "teleport", "color").setValue("RAINBOW").setComment("Default Color of Particle if supported, otherwise set \"NONE\"");
				config.getNode("options", "particles", "creation", "type").setValue("SPELL_WITCH").setComment("Default particle type when creating any kind of portal");
				config.getNode("options", "particles", "creation", "color").setValue("NONE").setComment("Default Color of Particle if supported, otherwise set \"NONE\"");
			}
			if (config.getNode("options", "random_spawn_radius").isVirtual()) {
				config.getNode("options", "random_spawn_radius").setValue(5000).setComment("World radius for random spawn portals.");
			}
			if (config.getNode("options", "teleport_message").isVirtual()) {
				config.getNode("options", "teleport_message").setComment("Set message that displays when player teleports.");
				config.getNode("options", "teleport_message", "title").setValue("&2%WORLD%");
				config.getNode("options", "teleport_message", "sub_title").setValue("&bx: %X%, y: %Y%, z: %Z%");
			}
			if (config.getNode("options", "advanced_permissions").isVirtual()) {
				config.getNode("options", "advanced_permissions").setValue(false).setComment("Require permission node for each portal. ex. 'pjp.portal.<name>', 'pjp.button.<world_x_y_z>'. If false use 'pjp.portal.interact' instead");
			}
			// UPDATE CONFIG
			if (!config.getNode("settings", "commands").isVirtual()) {
				config.getNode("settings").removeChild("commands");
			}

			if (config.getNode("settings", "modules").isVirtual()) {
				config.getNode("settings", "modules").setComment("Toggle on and off specific features");
			}
			if (config.getNode("settings", "modules", "portals").isVirtual()) {
				config.getNode("settings", "modules", "portals").setValue(true);
			}
			if (config.getNode("settings", "modules", "buttons").isVirtual()) {
				config.getNode("settings", "modules", "buttons").setValue(true);
			}
			if (config.getNode("settings", "modules", "doors").isVirtual()) {
				config.getNode("settings", "modules", "doors").setValue(true);
			}
			if (config.getNode("settings", "modules", "plates").isVirtual()) {
				config.getNode("settings", "modules", "plates").setValue(true);
			}
			if (config.getNode("settings", "modules", "levers").isVirtual()) {
				config.getNode("settings", "modules", "levers").setValue(true);
			}
			if (config.getNode("settings", "modules", "signs").isVirtual()) {
				config.getNode("settings", "modules", "signs").setValue(true);
			}
			if (config.getNode("settings", "modules", "warps").isVirtual()) {
				config.getNode("settings", "modules", "warps").setValue(true);
			}
			if (config.getNode("settings", "modules", "homes").isVirtual()) {
				config.getNode("settings", "modules", "homes").setValue(true);
			}
			save();
		}
	}

	private void create() {
		if (!file.exists()) {
			try {
				Main.getLog().info("Creating new " + file.getName() + " file...");
				file.createNewFile();
			} catch (IOException e) {
				Main.getLog().error("Failed to create new config file");
				e.printStackTrace();
			}
		}
	}

	private void load() {
		loader = HoconConfigurationLoader.builder().setFile(file).build();
		try {
			config = loader.load();
		} catch (IOException e) {
			Main.getLog().error("Failed to load config");
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			loader.save(config);
		} catch (IOException e) {
			Main.getLog().error("Failed to save config");
			e.printStackTrace();
		}
	}
}
