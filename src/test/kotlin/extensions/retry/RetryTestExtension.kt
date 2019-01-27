package extensions.retry

import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.opentest4j.TestAbortedException

internal class RetryTestExtension(private val invocationCount: Int, val maxInvocations: Int) : ExecutionCondition, TestExecutionExceptionHandler {

    override fun evaluateExecutionCondition(context: ExtensionContext): ConditionEvaluationResult {
        val successCount = invocationCount - context.failureCount()

        return if (successCount == 0) {
            ConditionEvaluationResult.enabled("Have not succeeded yet")
        } else {
            ConditionEvaluationResult.disabled("Test succeeded. Skipping run ${invocationCount + 1}")
        }
    }

    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {
        context.markFailure()
        if (invocationCount == maxInvocations) {
            // fail the test
            throw throwable
        }
        // skip the test, we have more retries
        throw TestAbortedException(throwable.message, throwable)
    }
}

private fun ExtensionContext.markFailure() {
    val namespace = ExtensionContext.Namespace.create(RetryTestExtension::class.java)
    val store = this.parent.get().getStore(namespace)
    val failures = this.failureCount() + 1
    return store.put(this.requiredTestMethod.name, failures)
}

private fun ExtensionContext.failureCount(): Int {
    val namespace = ExtensionContext.Namespace.create(RetryTestExtension::class.java)
    val store = this.parent.get().getStore(namespace)
    return store.getOrComputeIfAbsent(this.requiredTestMethod.name, { 0 }, Int::class.java)
}