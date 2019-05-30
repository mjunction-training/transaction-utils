package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface AfterCompletion<T> extends Callback<T> {

	@Override
	default T execute(final int status, final boolean redaOnly) {
		return doAfterCompletion(status);
	}

	T doAfterCompletion(int status);

}
