package com.gmail.trentech.pjp;

import co.aikar.timings.Timing;

public class Timings {

	private final Main plugin;
	private final Timing interactBlockEvent, constructPortalEvent, displaceEntityEventMoveItem, displaceEntityEventMoveLiving, displaceEntityEventMovePlayer;

	public Timings(Main plugin) {
		this.plugin = plugin;
		this.interactBlockEvent = timing("onInteractBlockEvent");
		this.constructPortalEvent = timing("onConstructPortalEvent");
		this.displaceEntityEventMoveItem = timing("onDisplaceEntityEventMoveItem");
		this.displaceEntityEventMoveLiving = timing("onDisplaceEntityEventMoveLiving");
		this.displaceEntityEventMovePlayer = timing("onDisplaceEntityEventMovePlayer");
	}

	private Timing timing(String key) {
		return co.aikar.timings.Timings.of(this.plugin, key);
	}

	public Timing onInteractBlockEvent() {
		return interactBlockEvent;
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
}
