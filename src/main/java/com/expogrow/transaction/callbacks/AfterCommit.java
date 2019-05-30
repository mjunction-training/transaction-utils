package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface AfterCommit<T> extends Callback<T> {

	@Override
	default T execute(final int status, final boolean redaOnly) {
		return doAfterCommit();
	}

	T doAfterCommit();

}
