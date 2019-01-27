package extensions.retry

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContextException
import org.junit.jupiter.api.extension.TestTemplateInvocationContext
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider
import java.util.stream.IntStream
import java.util.stream.Stream

class RetryTestTemplateContextProvider : TestTemplateInvocationContextProvider {

    override fun supportsTestTemplate(context: ExtensionContext): Boolean {
        return context.testMethod.map { it.isAnnotationPresent(TestWithRetry::class.java) }.orElse(false)
    }

    override fun provideTestTemplateInvocationContexts(context: ExtensionContext): Stream<TestTemplateInvocationContext> {
        return context.testMethod
                .map { it.getAnnotation(TestWithRetry::class.java).numberOfRetries }
                .filter { it >= 1 }
                .map { toRetryTemplates(it) }
                .orElseThrow { ExtensionContextException("Retries must be greater than or equal to 1") }
    }

    private fun toRetryTemplates(numberOfRetries: Int): Stream<TestTemplateInvocationContext> {
        return IntStream.rangeClosed(0, numberOfRetries).mapToObj { RetryTemplateContext(it, numberOfRetries) }
    }
}