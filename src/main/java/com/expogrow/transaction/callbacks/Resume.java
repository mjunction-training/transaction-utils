package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface Resume<T> extends Callback<T> {

	@Override
	default T execute(final int status, final boolean redaOnly) {
		return doResume();
	}

	T doResume();
}
