package extensions.webdriver

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.extension.*
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import java.io.File
import java.util.concurrent.TimeUnit

class WebDriverParameterResolver : ParameterResolver, BeforeAllCallback, AfterAllCallback, TestExecutionExceptionHandler {

    private lateinit var driver: WebDriver

    override fun beforeAll(context: ExtensionContext) {

        val envVar: String = System.getenv("BROWSER") ?: "CHROME"
        val selectedBrowser: Browser = Browser.valueOf(envVar)

        when (selectedBrowser) {
            Browser.CHROME -> {
                WebDriverManager.chromedriver().setup()
                val chromeOptions = ChromeOptions()
                chromeOptions.addArguments("--disable-gpu");
                driver = ChromeDriver(chromeOptions)
                driver.manage().window().size = Dimension(1920, 1080)
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS)
            }
            Browser.FIREFOX -> {
                WebDriverManager.firefoxdriver().setup()
                driver = FirefoxDriver()
                driver.manage().window().maximize()
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS)
            }
        }
    }

    override fun afterAll(context: ExtensionContext) {
        driver.quit()
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == WebDriver::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): WebDriver {
        return driver
    }

    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {
        val methodName = context.testMethod.map { it.name }.orElseThrow { ExtensionContextException("No test method") }
        val className = context.testClass.map { it.simpleName }.orElseThrow { ExtensionContextException("No test class") }

        (driver as TakesScreenshot)
                .getScreenshotAs(OutputType.FILE)
                .copyTo(File("screenshots/$className/$methodName.png"), overwrite = true)

        throw throwable
    }
}