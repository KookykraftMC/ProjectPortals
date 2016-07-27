package com.gmail.trentech.pjp.data.mutable;

import static com.gmail.trentech.pjp.data.Keys.SIGN;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import com.gmail.trentech.pjp.data.immutable.ImmutableSignPortalData;
import com.gmail.trentech.pjp.data.object.Sign;
import com.google.common.base.Preconditions;

public class SignPortalData extends AbstractSingleData<Sign, SignPortalData, ImmutableSignPortalData> {

	public SignPortalData(Sign value) {
		super(value, SIGN);
	}

	public Value<Sign> sign() {
		return Sponge.getRegistry().getValueFactory().createValue(SIGN, getValue(), getValue());
	}

	@Override
	public SignPortalData copy() {
		return new SignPortalData(this.getValue());
	}

	@Override
	public Optional<SignPortalData> fill(DataHolder dataHolder, MergeFunction mergeFn) {
		SignPortalData signData = Preconditions.checkNotNull(mergeFn).merge(copy(), dataHolder.get(SignPortalData.class).orElse(copy()));
		return Optional.of(set(SIGN, signData.get(SIGN).get()));
	}

	@Override
	public Optional<SignPortalData> from(DataContainer container) {
		if (container.contains(SIGN.getQuery())) {
			return Optional.of(set(SIGN, container.getSerializable(SIGN.getQuery(), Sign.class).orElse(getValue())));
		}
		return Optional.empty();
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public ImmutableSignPortalData asImmutable() {
		return new ImmutableSignPortalData(this.getValue());
	}

	@Override
	public int compareTo(SignPortalData value) {
		return value.compareTo(this);
	}

	@Override
	protected Value<Sign> getValueGetter() {
		return Sponge.getRegistry().getValueFactory().createValue(SIGN, getValue(), getValue());
	}

	@Override
	public DataContainer toContainer() {
		return super.toContainer().set(SIGN, getValue());
	}

}
