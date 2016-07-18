package com.gmail.trentech.pjp.timings;

import com.gmail.trentech.pjp.Main;

import co.aikar.timings.Timing;

public class LegacyTimings {

	private final Main plugin;
	private final Timing changeBlockEventPlace, changeBlockEventBreak;

	public LegacyTimings(Main plugin) {
		this.plugin = plugin;
		this.changeBlockEventPlace = timing("onChangeBlockEventPlace");
		this.changeBlockEventBreak = timing("onChangeBlockEventBreak");
	}

	private Timing timing(String key) {
		return co.aikar.timings.Timings.of(this.plugin, key);
	}

	public Timing onChangeBlockEventPlace() {
		return changeBlockEventPlace;
	}

	public Timing onChangeBlockEventBreak() {
		return changeBlockEventBreak;
	}
}
