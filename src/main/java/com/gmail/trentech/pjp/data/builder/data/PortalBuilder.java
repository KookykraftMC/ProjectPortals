package com.gmail.trentech.pjp.data.builder.data;

import static com.gmail.trentech.pjp.data.DataQueries.BUNGEE;
import static com.gmail.trentech.pjp.data.DataQueries.COLOR;
import static com.gmail.trentech.pjp.data.DataQueries.DESTINATION;
import static com.gmail.trentech.pjp.data.DataQueries.FILL;
import static com.gmail.trentech.pjp.data.DataQueries.FRAME;
import static com.gmail.trentech.pjp.data.DataQueries.PARTICLE;
import static com.gmail.trentech.pjp.data.DataQueries.PRICE;
import static com.gmail.trentech.pjp.data.DataQueries.ROTATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.data.object.Portal;
import com.gmail.trentech.pjp.effects.ParticleColor;
import com.gmail.trentech.pjp.effects.Particles;
import com.gmail.trentech.pjp.utils.Rotation;

public class PortalBuilder extends AbstractDataBuilder<Portal> {

	public PortalBuilder() {
		super(Portal.class, 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Optional<Portal> buildContent(DataView container) throws InvalidDataException {
		if (container.contains(DESTINATION, ROTATION, FRAME, FILL, PARTICLE, COLOR, PRICE, BUNGEE)) {
			String destination = container.getString(DESTINATION).get();
			String rotation = container.getString(ROTATION).get();

			List<Location<World>> frame = new ArrayList<>();

			for (String loc : (List<String>) container.getList(FRAME).get()) {
				String[] args = loc.split(":");

				Optional<World> optionalWorld = Main.getGame().getServer().getWorld(args[0]);

				if (!optionalWorld.isPresent()) {
					continue;
				}
				World world = optionalWorld.get();

				String[] coords = args[1].split("\\.");

				frame.add(new Location<World>(world, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
			}

			List<Location<World>> fill = new ArrayList<>();

			for (String loc : (List<String>) container.getList(FILL).get()) {
				String[] args = loc.split(":");

				Optional<World> optionalWorld = Main.getGame().getServer().getWorld(args[0]);

				if (!optionalWorld.isPresent()) {
					continue;
				}
				World world = optionalWorld.get();

				String[] coords = args[1].split("\\.");

				fill.add(new Location<World>(world, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
			}

			String particle = container.getString(PARTICLE).get();
			String color = container.getString(COLOR).get();

			Double price = container.getDouble(PRICE).get();
			boolean bungee = container.getBoolean(BUNGEE).get();

			return Optional.of(new Portal(destination, Rotation.get(rotation).get(), frame, fill, Particles.get(particle).get(), ParticleColor.get(color), price, bungee));
		}

		return Optional.empty();
	}
}
