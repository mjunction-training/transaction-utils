package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface Flush<T> extends Callback<T> {

	@Override
	default T execute(final int status, final boolean redaOnly) {
		return doFlush();
	}

	T doFlush();
}
