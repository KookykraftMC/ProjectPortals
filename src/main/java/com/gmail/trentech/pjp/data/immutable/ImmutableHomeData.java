package com.gmail.trentech.pjp.data.immutable;

import static com.gmail.trentech.pjp.data.Keys.HOMES;

import java.util.Map;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableMappedData;
import org.spongepowered.api.data.value.immutable.ImmutableMapValue;

import com.gmail.trentech.pjp.data.mutable.HomeData;
import com.gmail.trentech.pjp.data.object.Home;

public class ImmutableHomeData extends AbstractImmutableMappedData<String, Home, ImmutableHomeData, HomeData> {

	public ImmutableHomeData(Map<String, Home> value) {
		super(value, HOMES);
	}

	public ImmutableMapValue<String, Home> homes() {
		return Sponge.getRegistry().getValueFactory().createMapValue(HOMES, getValue()).asImmutable();
	}

	@Override
	public int compareTo(ImmutableHomeData data) {
		return this.compareTo(data);
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public HomeData asMutable() {
		return new HomeData(this.getValue());
	}

	@Override
	public DataContainer toContainer() {
		return super.toContainer().set(HOMES, getValue());
	}
}
