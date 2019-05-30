package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface Suspend<T> extends Callback<T> {

	@Override
	default T execute(final int status, final boolean redaOnly) {
		return doSuspend(status);
	}

	T doSuspend(int status);

}
