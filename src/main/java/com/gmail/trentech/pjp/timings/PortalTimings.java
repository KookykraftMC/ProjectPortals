package com.gmail.trentech.pjp.timings;

import com.gmail.trentech.pjp.Main;

import co.aikar.timings.Timing;

public class PortalTimings {

	private final Main plugin;
	private final Timing interactBlockEvent, constructPortalEvent, displaceEntityEventMoveItem, displaceEntityEventMoveLiving, displaceEntityEventMovePlayer, changeBlockEventPlace, changeBlockEventBreak;

	public PortalTimings(Main plugin) {
		this.plugin = plugin;
		this.interactBlockEvent = timing("onInteractBlockEvent");
		this.constructPortalEvent = timing("onConstructPortalEvent");
		this.displaceEntityEventMoveItem = timing("onDisplaceEntityEventMoveItem");
		this.displaceEntityEventMoveLiving = timing("onDisplaceEntityEventMoveLiving");
		this.displaceEntityEventMovePlayer = timing("onDisplaceEntityEventMovePlayer");
		this.changeBlockEventPlace = timing("onChangeBlockEventPlace");
		this.changeBlockEventBreak = timing("onChangeBlockEventBreak");
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
	
	public Timing onChangeBlockEventPlace() {
		return changeBlockEventPlace;
	}
	
	public Timing onChangeBlockEventBreak() {
		return changeBlockEventBreak;
	}
}
