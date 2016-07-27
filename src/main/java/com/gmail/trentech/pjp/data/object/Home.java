package com.gmail.trentech.pjp.data.object;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;

import com.gmail.trentech.pjp.data.DataQueries;
import com.gmail.trentech.pjp.utils.Rotation;

public class Home extends PortalBase {

	public Home(String destination, Rotation rotation) {
		super(destination, rotation, 0, false);
	}

	@Override
	public DataContainer toContainer() {
		return new MemoryDataContainer().set(DataQueries.DESTINATION, destination).set(DataQueries.ROTATION, rotation.getName());
	}
}
