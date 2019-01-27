package JourneyTests

import extensions.retry.TestWithRetry
import extensions.webdriver.WebDriverSuite
import org.openqa.selenium.WebDriver
import pageObjects.FormEntryPage

@WebDriverSuite
class FormUiJourney(private val driver: WebDriver){
    private lateinit var formEntryPage: FormEntryPage

    @TestWithRetry
    fun leaveLeftFormEmptyAndGetAnError(){
        formEntryPage = FormEntryPage(driver)
        formEntryPage.goToPage()
        formEntryPage.calculateCaptcha()
        formEntryPage.leaveLeftFieldsEmptyAndSubmit()
        assert(formEntryPage.fieldsEmptyErrorMessageIsVisible())
    }

    @TestWithRetry
    fun fillFormAndSubmit(){
        formEntryPage = FormEntryPage(driver)
        formEntryPage.goToPage()
        formEntryPage.fillRightFormWithCaptcha()
        formEntryPage.fillLeftForm()
        assert(formEntryPage.successfulLeftSubmitMessageVisible())
    }
}