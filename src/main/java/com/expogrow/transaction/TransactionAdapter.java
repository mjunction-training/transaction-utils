package com.expogrow.transaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.support.TransactionSynchronization;

import com.expogrow.transaction.callbacks.AfterCommit;
import com.expogrow.transaction.callbacks.AfterCompletion;
import com.expogrow.transaction.callbacks.BeforeCommit;
import com.expogrow.transaction.callbacks.BeforeCompletion;
import com.expogrow.transaction.callbacks.Callback;
import com.expogrow.transaction.callbacks.Flush;
import com.expogrow.transaction.callbacks.Resume;
import com.expogrow.transaction.callbacks.Suspend;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TransactionAdapter<T> implements TransactionSynchronization {

	private final List<Callback<T>> callbacks = new ArrayList<>();
	private final RetryTemplate retryTemplate;

	public TransactionAdapter(final RetryTemplate retryTemplate) {
		super();
		this.retryTemplate = retryTemplate;
	}

	public void register(final Callback<T> callback) {
		callbacks.add(callback);
	}

	@Override
	public void suspend() {
		execute(Suspend.class, -1, false);
	}

	@Override
	public void resume() {
		execute(Resume.class, -1, false);
	}

	@Override
	public void flush() {
		execute(Flush.class, -1, false);
	}

	@Override
	public void beforeCommit(final boolean readOnly) {
		execute(BeforeCommit.class, -1, readOnly);
	}

	@Override
	public void beforeCompletion() {
		execute(BeforeCompletion.class, -1, false);
	}

	@Override
	public void afterCommit() {
		execute(AfterCommit.class, -1, false);
	}

	@Override
	public void afterCompletion(final int status) {
		execute(AfterCompletion.class, status, false);
	}

	private void execute(final Class<?> clazz, final int status, final boolean redaOnly) {

		for (final Callback<T> callback : callbacks) {

			if (callback.getClass().isAssignableFrom(clazz)) {

				try {

					log.info("Before executing Callback :: {} ", clazz.getName());
					final T result = retryTemplate.execute(arg0 -> callback.execute(status, redaOnly));
					log.info("Callback :: {} result {}", clazz.getName(), result);

				} catch (final Throwable e) {
					log.error("Callback :: {} error {}", clazz.getName(), e.getMessage(), e);
				}

			}
		}
	}

}