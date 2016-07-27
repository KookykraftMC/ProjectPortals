package com.gmail.trentech.pjp.commands;

import org.spongepowered.api.entity.living.player.Player;

import com.gmail.trentech.pjp.data.mutable.SignPortalData;
import com.gmail.trentech.pjp.listeners.ButtonListener;
import com.gmail.trentech.pjp.listeners.DoorListener;
import com.gmail.trentech.pjp.listeners.LeverListener;
import com.gmail.trentech.pjp.listeners.PlateListener;
import com.gmail.trentech.pjp.listeners.SignListener;
import com.gmail.trentech.pjp.utils.Help;
import com.gmail.trentech.pjp.utils.Rotation;

public class CMDObj {

	public static class Button extends CMDObjBase {

		public Button() {
			super("button");

			Help help = new Help("button", "button", " Use this command to create a button that will teleport you to other worlds");
			help.setSyntax(" /button <destination>  [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]\n /b <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]");
			help.setExample(" /button MyWorld\n /button MyWorld -c -100,65,254\n /button MyWorld -c random\n /button MyWorld -c -100,65,254 -d south\n /button MyWorld -d southeast\n /button MyWorld -p 50");
			help.save();
		}

		@Override
		protected void init(Player player, String destination, Rotation rotation, Double price, boolean isBungee) {
			ButtonListener.builders.put(player.getUniqueId(), new com.gmail.trentech.pjp.data.object.Button(destination, rotation, price, isBungee));
		}
	}

	public static class Door extends CMDObjBase {

		public Door() {
			super("door");

			Help help = new Help("door", "door", " Use this command to create a door that will teleport you to other worlds");
			help.setSyntax(" /door <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]\n /d <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]");
			help.setExample(" /door MyWorld\n /door MyWorld -c -100,65,254\n /door MyWorld -c random\n /door MyWorld -c -100,65,254 -d south\n /door MyWorld -d southeast\n /door MyWorld -p 50");
			help.save();
		}

		@Override
		protected void init(Player player, String destination, Rotation rotation, Double price, boolean isBungee) {
			DoorListener.builders.put(player.getUniqueId(), new com.gmail.trentech.pjp.data.object.Door(destination, rotation, price, isBungee));
		}
	}

	public static class Lever extends CMDObjBase {

		public Lever() {
			super("lever");

			Help help = new Help("lever", "lever", " Use this command to create a lever that will teleport you to other worlds");
			help.setSyntax(" /lever <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]\n /l <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]");
			help.setExample(" /lever MyWorld\n /lever MyWorld -c -100,65,254\n /lever MyWorld -c random\n /lever MyWorld -c -100,65,254 -d south\n /lever MyWorld -d southeast\n /lever MyWorld -p 50");
			help.save();
		}

		@Override
		protected void init(Player player, String destination, Rotation rotation, Double price, boolean isBungee) {
			LeverListener.builders.put(player.getUniqueId(), new com.gmail.trentech.pjp.data.object.Lever(destination, rotation, price, isBungee));
		}
	}

	public static class Plate extends CMDObjBase {

		public Plate() {
			super("pressure plate");

			Help help = new Help("plate", "plate", " Use this command to create a pressure plate that will teleport you to other worlds");
			help.setSyntax(" /plate <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]\n /pp <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]");
			help.setExample(" /plate MyWorld\n /plate MyWorld -c -100,65,254\n /plate MyWorld -c random\n /plate MyWorld -c -100,65,254 -d south\n /plate MyWorld -d southeast\n /plate MyWorld -p 50");
			help.save();
		}

		@Override
		protected void init(Player player, String destination, Rotation rotation, Double price, boolean isBungee) {
			PlateListener.builders.put(player.getUniqueId(), new com.gmail.trentech.pjp.data.object.Plate(destination, rotation, price, isBungee));
		}
	}

	public static class Sign extends CMDObjBase {

		public Sign() {
			super("sign");

			Help help = new Help("sign", "sign", " Use this command to create a sign that will teleport you to other worlds");
			help.setSyntax(" /sign <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]\n /s <destination> [-b] [-c <x,y,z>] [-d <direction>] [-p <price>]");
			help.setExample(" /sign MyWorld\n /sign MyWorld -c -100,65,254\n /sign MyWorld -c random\n /sign MyWorld -c -100,65,254 -d south\n /sign MyWorld -d southeast\n /sign MyWorld -p 50");
			help.save();
		}

		@Override
		protected void init(Player player, String destination, Rotation rotation, Double price, boolean isBungee) {
			SignListener.builders.put(player.getUniqueId(), new SignPortalData(new com.gmail.trentech.pjp.data.object.Sign(destination, rotation, price, isBungee)));
		}
	}
}