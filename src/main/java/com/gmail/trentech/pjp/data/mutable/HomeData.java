package com.gmail.trentech.pjp.data.mutable;

import static com.gmail.trentech.pjp.data.Keys.HOMES;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractMappedData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.MapValue;

import com.gmail.trentech.pjp.data.immutable.ImmutableHomeData;
import com.gmail.trentech.pjp.data.object.Home;
import com.google.common.base.Preconditions;

public class HomeData extends AbstractMappedData<String, Home, HomeData, ImmutableHomeData> {

	public HomeData(Map<String, Home> value) {
		super(value, HOMES);
	}

	public MapValue<String, Home> homes() {
		return Sponge.getRegistry().getValueFactory().createMapValue(HOMES, getValue());
	}

	@Override
	public Optional<Home> get(String key) {
		if (getValue().containsKey(key)) {
			return Optional.of(getValue().get(key));
		}
		return Optional.empty();
	}

	@Override
	public Set<String> getMapKeys() {
		return getValue().keySet();
	}

	@Override
	public HomeData put(String key, Home value) {
		getValue().put(key, value);
		return this;
	}

	@Override
	public HomeData putAll(Map<? extends String, ? extends Home> map) {
		getValue().putAll(map);
		return this;
	}

	@Override
	public HomeData remove(String key) {
		getValue().remove(key);
		return this;
	}

	@Override
	public Optional<HomeData> fill(DataHolder dataHolder, MergeFunction mergeFn) {
		HomeData homeData = Preconditions.checkNotNull(mergeFn).merge(copy(), dataHolder.get(HomeData.class).orElse(copy()));
		return Optional.of(set(HOMES, homeData.get(HOMES).get()));
	}

	@Override
	public Optional<HomeData> from(DataContainer container) {
		if (container.contains(HOMES.getQuery())) {
			HashMap<String, Home> homeList = new HashMap<>();

			DataView homes = container.getView(HOMES.getQuery()).get();

			for (DataQuery home : homes.getKeys(false)) {
				homeList.put(home.toString(), homes.getSerializable(home, Home.class).get());
			}
			return Optional.of(new HomeData(homeList));
		}
		return Optional.empty();
	}

	@Override
	public HomeData copy() {
		return new HomeData(getValue());
	}

	@Override
	public int compareTo(HomeData data) {
		return this.compareTo(data);
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public ImmutableHomeData asImmutable() {
		return new ImmutableHomeData(getValue());
	}

	@Override
	public DataContainer toContainer() {
		return super.toContainer().set(HOMES, getValue());
	}
}