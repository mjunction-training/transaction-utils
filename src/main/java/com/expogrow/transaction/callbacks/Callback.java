package com.expogrow.transaction.callbacks;

@FunctionalInterface
public interface Callback<T> {
	T execute(int status, boolean redaonly);
}
