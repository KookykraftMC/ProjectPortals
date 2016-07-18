package com.gmail.trentech.pjp.timings;

import com.gmail.trentech.pjp.Main;

import co.aikar.timings.Timing;

public class DoorTimings {

	private final Main plugin;
	private final Timing displaceEntityEventMove, changeBlockEventBreak, changeBlockEventPlace;

	public DoorTimings(Main plugin) {
		this.plugin = plugin;
		this.displaceEntityEventMove = timing("onDisplaceEntityEventMove");
		this.changeBlockEventBreak = timing("onChangeBlockEventBreak");
		this.changeBlockEventPlace = timing("onChangeBlockEventPlace");
	}

	private Timing timing(String key) {
		return co.aikar.timings.Timings.of(this.plugin, key);
	}

	public Timing onDisplaceEntityEventMove() {
		return displaceEntityEventMove;
	}

	public Timing onChangeBlockEventBreak() {
		return changeBlockEventBreak;
	}

	public Timing onChangeBlockEventPlace() {
		return changeBlockEventPlace;
	}
}
