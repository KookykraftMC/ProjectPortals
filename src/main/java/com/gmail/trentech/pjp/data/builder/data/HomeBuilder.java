package com.gmail.trentech.pjp.data.builder.data;

import static com.gmail.trentech.pjp.data.DataQueries.DESTINATION;
import static com.gmail.trentech.pjp.data.DataQueries.ROTATION;

import java.util.Optional;

import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import com.gmail.trentech.pjp.data.object.Home;
import com.gmail.trentech.pjp.utils.Rotation;

public class HomeBuilder extends AbstractDataBuilder<Home> {

	public HomeBuilder() {
		super(Home.class, 1);
	}

	@Override
	protected Optional<Home> buildContent(DataView container) throws InvalidDataException {
		if (container.contains(DESTINATION, ROTATION)) {
			String destination = container.getString(DESTINATION).get();
			String rotation = container.getString(ROTATION).get();

			return Optional.of(new Home(destination, Rotation.get(rotation).get()));
		}

		return Optional.empty();
	}
}
