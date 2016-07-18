package com.gmail.trentech.pjp.timings;

import com.gmail.trentech.pjp.Main;

import co.aikar.timings.Timing;

public class SignTimings {

	private final Main plugin;
	private final Timing changeSignEvent, interactBlockEventSecondary, changeBlockEventBreak;

	public SignTimings(Main plugin) {
		this.plugin = plugin;
		this.changeSignEvent = timing("onChangeSignEvent");
		this.interactBlockEventSecondary = timing("onInteractBlockEventSecondary");
		this.changeBlockEventBreak = timing("onChangeBlockEventBreak");
	}

	private Timing timing(String key) {
		return co.aikar.timings.Timings.of(this.plugin, key);
	}

	public Timing onChangeSignEvent() {
		return changeSignEvent;
	}

	public Timing onInteractBlockEventSecondary() {
		return interactBlockEventSecondary;
	}

	public Timing onChangeBlockEventBreak() {
		return changeBlockEventBreak;
	}
}
