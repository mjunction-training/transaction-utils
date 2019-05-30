package com.expogrow.transaction.callbacks;

import org.springframework.transaction.support.TransactionSynchronization;

@FunctionalInterface
public interface Compensate<T> extends AfterCompletion<T> {

	@Override
	default T doAfterCompletion(final int status) {

		if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
			return doCompensate(status);
		}

		return null;
	}

	T doCompensate(int status);

}
