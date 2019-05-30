package com.expogrow.transaction.callbacks;

import org.springframework.transaction.support.TransactionSynchronization;

@FunctionalInterface
public interface PostCommit<T> extends AfterCompletion<T> {

	@Override
	default T doAfterCompletion(final int status) {

		if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
			return doPostCommit(status);
		}

		return null;
	}

	T doPostCommit(int status);

}
