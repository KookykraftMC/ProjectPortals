package com.gmail.trentech.pjp.listeners;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.data.object.PortalBuilder;

public class LegacyListener {

	public static ConcurrentHashMap<UUID, PortalBuilder> builders = new ConcurrentHashMap<>();

	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Place event, @First Player player) {
		if (!builders.containsKey(player.getUniqueId())) {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				Location<World> location = transaction.getFinal().getLocation().get();

				if (!Portal.get(location).isPresent()) {
					continue;
				}

				event.setCancelled(true);
				break;
			}
			return;
		}
		PortalBuilder builder = builders.get(player.getUniqueId());

		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			if (transaction.getFinal().getState().getType().equals(BlockTypes.FIRE)) {
				event.setCancelled(true);
				break;
			}

			Location<World> location = transaction.getFinal().getLocation().get();

			if (builder.isFill()) {
				builder.addFill(location);
			} else {
				builder.addFrame(location);
			}
		}
	}

	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Break event, @First Player player) {
		if (!builders.containsKey(player.getUniqueId())) {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
				Location<World> location = transaction.getFinal().getLocation().get();

				if (!Portal.get(location).isPresent()) {
					continue;
				}

				event.setCancelled(true);
				break;
			}
			return;
		}
		PortalBuilder builder = builders.get(player.getUniqueId());

		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			Location<World> location = transaction.getFinal().getLocation().get();
			if (builder.isFill()) {
				builder.removeFill(location);
			} else {
				builder.removeFrame(location);
			}
		}
	}
}
