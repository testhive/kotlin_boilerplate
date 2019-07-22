package JourneyTests

import extensions.webdriver.WebDriverSuite
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver
import pageObjects.FormEntryPage

@WebDriverSuite
class FormUiJourney(private val driver: WebDriver){
    private lateinit var formEntryPage: FormEntryPage

    @Test
    fun leaveLeftFormEmptyAndGetAnError(){
        formEntryPage = FormEntryPage(driver)
        formEntryPage.goToPage()
        formEntryPage.calculateCaptcha()
        formEntryPage.leaveLeftFieldsEmptyAndSubmit()
        assert(formEntryPage.fieldsEmptyErrorMessageIsVisible())
    }

    @Test
    fun fillFormAndSubmit(){
        formEntryPage = FormEntryPage(driver)
        formEntryPage.goToPage()
        formEntryPage.fillRightFormWithCaptcha()
        formEntryPage.fillLeftForm()
        assert(formEntryPage.successfulLeftSubmitMessageVisible())
    }
}