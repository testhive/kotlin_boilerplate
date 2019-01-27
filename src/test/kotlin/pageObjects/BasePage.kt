package pageObjects

import org.openqa.selenium.*
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.By.*
import org.openqa.selenium.support.ui.WebDriverWait

abstract class BasePage(private val driver: WebDriver) {
    private val defaultMaxWAitTime: Long = 20
    val wait = WebDriverWait(driver, defaultMaxWAitTime)

    fun refreshPage(){
        driver.navigate().refresh()
    }
    fun findByCss(css: String): WebElement? {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css)))
    }
    fun WebElement.findByCss(css: String): WebElement? {
        return wait.until(ExpectedConditions.visibilityOf(this.findElement(By.cssSelector(css))))
    }
    fun findByXpath(xpath: String): WebElement? {
        return wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath))))
    }
    fun WebElement.findByXpath(xpath: String): WebElement? {
        return wait.until(ExpectedConditions.visibilityOf(this.findElement(xpath(xpath))))
    }
    fun findById(id: String): WebElement? {
        return driver.findElement(By.id(id))
    }
    fun WebElement.findById(id: String): WebElement? {
        return wait.until(ExpectedConditions.visibilityOf(this.findElement(By.id(id))))
    }
    fun findByTag(tag: String): WebElement? {
        return driver.findElement(By.tagName(tag))
    }
    fun WebElement.findByTag(tag: String): WebElement? {
        return wait.until(ExpectedConditions.visibilityOf(this.findElement(By.tagName(tag))))
    }

    fun findByText(term: String): WebElement? {
        return findByXpath("//*[contains(text(), '$term')]")
    }
    fun allByCss(css: String): MutableList<WebElement>? {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css)))
        return driver.findElements(By.cssSelector(css))
    }
    fun WebElement.allByCss(css: String): MutableList<WebElement>? {
        wait.until(ExpectedConditions.visibilityOf(this.findElement(By.cssSelector(css))))
        return this.findElements(By.cssSelector(css))
    }

    fun WebElement.waitForInvisibility(maxSeconds: Int) {
        val startTime = System.currentTimeMillis()
        try {
            while (System.currentTimeMillis() - startTime < maxSeconds * 1000 && this.isDisplayed) {}
        } catch (e: StaleElementReferenceException) {
            return
        }
    }

    fun goBack(){
        val js = driver as JavascriptExecutor
        js.executeScript("window.history.back();")
    }
    fun suppressPrintingDialog(){
        val js = driver as JavascriptExecutor
        js.executeScript("window.print=function(){console.info('native print function is overriden.');}")
    }
}