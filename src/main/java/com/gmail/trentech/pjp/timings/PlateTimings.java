package com.gmail.trentech.pjp.timings;

import com.gmail.trentech.pjp.Main;

import co.aikar.timings.Timing;

public class PlateTimings {

	private final Main plugin;
	private final Timing changeBlockEventModify, changeBlockEventBreak, changeBlockEventPlace;

	public PlateTimings(Main plugin) {
		this.plugin = plugin;
		this.changeBlockEventModify = timing("onChangeBlockEventModify");
		this.changeBlockEventBreak = timing("onChangeBlockEventBreak");
		this.changeBlockEventPlace = timing("onChangeBlockEventPlace");
	}

	private Timing timing(String key) {
		return co.aikar.timings.Timings.of(this.plugin, key);
	}

	public Timing onChangeBlockEventModify() {
		return changeBlockEventModify;
	}

	public Timing onChangeBlockEventBreak() {
		return changeBlockEventBreak;
	}

	public Timing onChangeBlockEventPlace() {
		return changeBlockEventPlace;
	}
}
