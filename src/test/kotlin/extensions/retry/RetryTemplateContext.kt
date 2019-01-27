package extensions.retry

import org.junit.jupiter.api.extension.Extension
import org.junit.jupiter.api.extension.TestTemplateInvocationContext

class RetryTemplateContext(private val invocation: Int, val maxInvocations: Int) : TestTemplateInvocationContext {
    override fun getDisplayName(invocationIndex: Int): String {
        return "Invocation number $invocationIndex (requires 1 success)"
    }

    override fun getAdditionalExtensions(): MutableList<Extension> {
        return mutableListOf(RetryTestExtension(invocation, maxInvocations))
    }
}