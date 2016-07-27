package com.gmail.trentech.pjp.data.immutable;

import static com.gmail.trentech.pjp.data.Keys.SIGN;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import com.gmail.trentech.pjp.data.mutable.SignPortalData;
import com.gmail.trentech.pjp.data.object.Sign;

public class ImmutableSignPortalData extends AbstractImmutableSingleData<Sign, ImmutableSignPortalData, SignPortalData> {

	public ImmutableSignPortalData(Sign value) {
		super(value, SIGN);
	}

	public ImmutableValue<Sign> sign() {
		return Sponge.getRegistry().getValueFactory().createValue(SIGN, getValue(), getValue()).asImmutable();
	}

	@Override
	public <E> Optional<ImmutableSignPortalData> with(Key<? extends BaseValue<E>> key, E value) {
		if (this.supports(key)) {
			return Optional.of(asMutable().set(key, value).asImmutable());
		} else {
			return Optional.empty();
		}
	}

	@Override
	public int compareTo(ImmutableSignPortalData arg0) {
		return 0;
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	protected ImmutableValue<?> getValueGetter() {
		return Sponge.getRegistry().getValueFactory().createValue(SIGN, getValue()).asImmutable();
	}

	@Override
	public SignPortalData asMutable() {
		return new SignPortalData(this.getValue());
	}
}
