package com.expogrow.transaction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import lombok.extern.log4j.Log4j2;

@Configuration
@EnableRetry
@Log4j2
public class CollbackConfiguration {

	@Bean("compensationRetryTemplate")
	public RetryTemplate retryTemplate(@Value("${transaction.callback.retry.maxAttempts:3}") final int maxAttempts,
			@Value("${transaction.callback.retry.backOffPeriodInMS:2000}") final long backOffPeriodInMS) {
		final RetryTemplate retryTemplate = new RetryTemplate();
		final FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(backOffPeriodInMS);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
		retryTemplate.registerListener(new DefaultListenerSupport());
		final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(maxAttempts);
		retryTemplate.setRetryPolicy(retryPolicy);
		return retryTemplate;
	}

	public class DefaultListenerSupport extends RetryListenerSupport {
		@Override
		public <T, E extends Throwable> void close(final RetryContext context, final RetryCallback<T, E> callback,
				final Throwable throwable) {
			log.info("onClose");
			super.close(context, callback, throwable);
		}

		@Override
		public <T, E extends Throwable> void onError(final RetryContext context, final RetryCallback<T, E> callback,
				final Throwable throwable) {
			log.info("onError");
			super.onError(context, callback, throwable);
		}

		@Override
		public <T, E extends Throwable> boolean open(final RetryContext context, final RetryCallback<T, E> callback) {
			log.info("onOpen");
			return super.open(context, callback);
		}
	}

}
