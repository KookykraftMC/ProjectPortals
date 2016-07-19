package com.gmail.trentech.pjp.listeners;

import com.gmail.trentech.pjp.Main;

import co.aikar.timings.Timing;

public class Timings {

	private final Timing changeSignEvent, interactBlockEventSecondary, changeBlockEventModify, changeBlockEventPlace,
	changeBlockEventBreak, constructPortalEvent, displaceEntityEventMoveItem, displaceEntityEventMoveLiving, displaceEntityEventMovePlayer,
	displaceEntityEventTeleport, teleportEvent, teleportEventServer, teleportEventLocal, destructEntityEventDeath;

	public Timings() {
		this.changeSignEvent = timing("onChangeSignEvent");
		this.interactBlockEventSecondary = timing("onInteractBlockEventSecondary");
		this.changeBlockEventModify = timing("onChangeBlockEventModify");
		this.changeBlockEventPlace = timing("onChangeBlockEventPlace");
		this.changeBlockEventBreak = timing("onChangeBlockEventBreak");
		this.constructPortalEvent = timing("onConstructPortalEvent");
		this.displaceEntityEventMoveItem = timing("onDisplaceEntityEventMoveItem");
		this.displaceEntityEventMoveLiving = timing("onDisplaceEntityEventMoveLiving");
		this.displaceEntityEventMovePlayer = timing("onDisplaceEntityEventMovePlayer");
		this.displaceEntityEventTeleport = timing("onDisplaceEntityEventTeleport");
		this.teleportEvent = timing("onTeleportEvent");
		this.teleportEventServer = timing("onTeleportEventServer");
		this.teleportEventLocal = timing("onTeleportEventLocal");
		this.destructEntityEventDeath = timing("onDestructEntityEventDeath");
	}

	private Timing timing(String key) {
		return co.aikar.timings.Timings.of(Main.getPlugin(), key);
	}

	public Timing onChangeSignEvent() {
		return changeSignEvent;
	}

	public Timing onInteractBlockEventSecondary() {
		return interactBlockEventSecondary;
	}

	public Timing onConstructPortalEvent() {
		return constructPortalEvent;
	}

	public Timing onDisplaceEntityEventMoveItem() {
		return displaceEntityEventMoveItem;
	}

	public Timing onDisplaceEntityEventMoveLiving() {
		return displaceEntityEventMoveLiving;
	}

	public Timing onDisplaceEntityEventMovePlayer() {
		return displaceEntityEventMovePlayer;
	}
	
	public Timing onDisplaceEntityEventTeleport() {
		return displaceEntityEventTeleport;
	}
	
	public Timing onTeleportEvent() {
		return teleportEvent;
	}
	
	public Timing onTeleportEventLocal() {
		return teleportEventLocal;
	}
	
	public Timing onTeleportEventServer() {
		return teleportEventServer;
	}
	public Timing onChangeBlockEventModify() {
		return changeBlockEventModify;
	}
	
	public Timing onChangeBlockEventPlace() {
		return changeBlockEventPlace;
	}
	
	public Timing onChangeBlockEventBreak() {
		return changeBlockEventBreak;
	}
	
	public Timing onDestructEntityEventDeath() {
		return destructEntityEventDeath;
	}
}
