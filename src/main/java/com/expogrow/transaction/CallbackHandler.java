package com.expogrow.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.expogrow.transaction.callbacks.AfterCommit;
import com.expogrow.transaction.callbacks.AfterCompletion;
import com.expogrow.transaction.callbacks.BeforeCommit;
import com.expogrow.transaction.callbacks.BeforeCompletion;
import com.expogrow.transaction.callbacks.Callback;
import com.expogrow.transaction.callbacks.Compensate;
import com.expogrow.transaction.callbacks.Flush;
import com.expogrow.transaction.callbacks.PostCommit;
import com.expogrow.transaction.callbacks.Resume;
import com.expogrow.transaction.callbacks.Suspend;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("transactionCallbackHandler")
public class CallbackHandler {

	@Autowired
	@Qualifier("compensationRetryTemplate")
	private RetryTemplate compensationRetryTemplate;

	public <T> boolean registerAfterCommit(final AfterCommit<T> callback) {
		return register(callback);
	}

	public <T> boolean registerAfterCompletion(final AfterCompletion<T> callback) {
		return register(callback);
	}

	public <T> boolean registerBeforeCommit(final BeforeCommit<T> callback) {
		return register(callback);
	}

	public <T> boolean registerBeforeCompletion(final BeforeCompletion<T> callback) {
		return register(callback);
	}

	public <T> boolean registerFlush(final Flush<T> callback) {
		return register(callback);
	}

	public <T> boolean registerPostCommit(final PostCommit<T> callback) {
		return register(callback);
	}

	public <T> boolean registerCompensate(final Compensate<T> callback) {
		return register(callback);
	}

	public <T> boolean registerResume(final Resume<T> callback) {
		return register(callback);
	}

	public <T> boolean registerSuspend(final Suspend<T> callback) {
		return register(callback);
	}

	@SuppressWarnings("unchecked")
	private <T> boolean register(final Callback<T> callback) {

		if (null == callback) {

			log.error("callback is null.");

			return false;

		}

		if (!TransactionSynchronizationManager.isSynchronizationActive()) {

			log.error("Transaction is not active.");

			log.error("Most likely @Transaction annotation not used or transaction manger bean not defined");

			return false;

		}

		final List<TransactionSynchronization> synshronizers = TransactionSynchronizationManager.getSynchronizations();

		for (final TransactionSynchronization transactionSynchronization : synshronizers) {

			if (TransactionAdapter.class.isAssignableFrom(transactionSynchronization.getClass())) {

				log.info("Registering callback");

				((TransactionAdapter<T>) transactionSynchronization).register(callback);

				log.info("Callback is registered.");

				return true;

			}
		}

		final TransactionAdapter<T> synchronization = new TransactionAdapter<>(compensationRetryTemplate);

		log.info("Registering callback");

		synchronization.register(callback);

		TransactionSynchronizationManager.registerSynchronization(synchronization);

		log.info("Callback is registered.");

		return true;

	}

}
