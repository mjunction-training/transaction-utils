package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface BeforeCommit<T> extends Callback<T> {

	@Override
	default T execute(final int status, final boolean redaOnly) {
		return doBeforeCommit(redaOnly);
	}

	T doBeforeCommit(boolean redaOnly);
}
