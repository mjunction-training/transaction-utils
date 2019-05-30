package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface BeforeCompletion<T> extends Callback<T> {

	@Override
	default T execute(final int status, final boolean redaOnly) {
		return doBeforeCompletion();
	}

	T doBeforeCompletion();
}
