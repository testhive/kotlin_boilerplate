package extensions.webdriver

import org.junit.jupiter.api.extension.ExtendWith

@Target(AnnotationTarget.CLASS)
@ExtendWith(WebDriverParameterResolver::class)
annotation class WebDriverSuite
