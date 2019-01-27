package extensions.retry

import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith

/**
 * @param numberOfRetries - The number of times the annotated test should be retried in case of failure.
 */
@TestTemplate
@Target(AnnotationTarget.FUNCTION)
@ExtendWith(RetryTestTemplateContextProvider::class)
annotation class TestWithRetry(val numberOfRetries: Int = 1)