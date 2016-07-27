package com.gmail.trentech.pjp.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.translator.ConfigurateTranslator;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Button;
import com.gmail.trentech.pjp.data.object.Door;
import com.gmail.trentech.pjp.data.object.Lever;
import com.gmail.trentech.pjp.data.object.Plate;
import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.data.object.Warp;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class Serializer {

	public static String serialize(Warp warp) {
		ConfigurationNode node = ConfigurateTranslator.instance().translateData(warp.toContainer());

		StringWriter stringWriter = new StringWriter();
		try {
			HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	public static Warp deserializeWarp(String item) {
		ConfigurationNode node = null;
		try {
			node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(item))).build().load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfigurateTranslator translator = ConfigurateTranslator.instance();
		DataManager manager = Main.getGame().getDataManager();

		DataView dataView = translator.translateFrom(node);

		return manager.deserialize(Warp.class, dataView).get();
	}

	public static String serialize(Plate plate) {
		ConfigurationNode node = ConfigurateTranslator.instance().translateData(plate.toContainer());

		StringWriter stringWriter = new StringWriter();
		try {
			HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	public static Plate deserializePlate(String item) {
		ConfigurationNode node = null;
		try {
			node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(item))).build().load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfigurateTranslator translator = ConfigurateTranslator.instance();
		DataManager manager = Main.getGame().getDataManager();

		DataView dataView = translator.translateFrom(node);

		return manager.deserialize(Plate.class, dataView).get();
	}

	public static String serialize(Lever lever) {
		ConfigurationNode node = ConfigurateTranslator.instance().translateData(lever.toContainer());

		StringWriter stringWriter = new StringWriter();
		try {
			HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	public static Lever deserializeLever(String item) {
		ConfigurationNode node = null;
		try {
			node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(item))).build().load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfigurateTranslator translator = ConfigurateTranslator.instance();
		DataManager manager = Main.getGame().getDataManager();

		DataView dataView = translator.translateFrom(node);

		return manager.deserialize(Lever.class, dataView).get();
	}

	public static String serialize(Door door) {
		ConfigurationNode node = ConfigurateTranslator.instance().translateData(door.toContainer());

		StringWriter stringWriter = new StringWriter();
		try {
			HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	public static Door deserializeDoor(String item) {
		ConfigurationNode node = null;
		try {
			node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(item))).build().load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfigurateTranslator translator = ConfigurateTranslator.instance();
		DataManager manager = Main.getGame().getDataManager();

		DataView dataView = translator.translateFrom(node);

		return manager.deserialize(Door.class, dataView).get();
	}

	public static String serialize(Button button) {
		ConfigurationNode node = ConfigurateTranslator.instance().translateData(button.toContainer());

		StringWriter stringWriter = new StringWriter();
		try {
			HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	public static Button deserializeButton(String item) {
		ConfigurationNode node = null;
		try {
			node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(item))).build().load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfigurateTranslator translator = ConfigurateTranslator.instance();
		DataManager manager = Main.getGame().getDataManager();

		DataView dataView = translator.translateFrom(node);

		return manager.deserialize(Button.class, dataView).get();
	}

	public static String serialize(Portal portal) {
		ConfigurationNode node = ConfigurateTranslator.instance().translateData(portal.toContainer());

		StringWriter stringWriter = new StringWriter();
		try {
			HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(stringWriter)).build().save(node);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringWriter.toString();
	}

	public static Portal deserializePortal(String item) {
		ConfigurationNode node = null;
		try {
			node = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(new StringReader(item))).build().load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ConfigurateTranslator translator = ConfigurateTranslator.instance();
		DataManager manager = Main.getGame().getDataManager();

		DataView dataView = translator.translateFrom(node);

		return manager.deserialize(Portal.class, dataView).get();
	}
}
