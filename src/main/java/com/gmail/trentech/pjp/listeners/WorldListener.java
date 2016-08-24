package com.gmail.trentech.pjp.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.event.world.chunk.UnloadChunkEvent;

import com.gmail.trentech.pjp.utils.Teleport;

public class WorldListener {

	@Listener
	public void onLoadWorldEvent(LoadWorldEvent event) {
		Teleport.cacheRandom(event.getTargetWorld());	
	}

	@Listener
	public void onLoadChunkEvent(LoadChunkEvent event) {
		
	}
	
	@Listener
	public void onUnloadChunkEvent(UnloadChunkEvent event) {
		
	}
}
