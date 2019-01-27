package pageObjects

import org.openqa.selenium.WebDriver

class FormEntryPage(private val driver: WebDriver) : BasePage(driver){
    private val leftNameField = "#et_pb_contact_name_0"
    private val leftMessageField = "#et_pb_contact_message_0"
    private val leftForm = "#et_pb_contact_form_0"
    private val rightNameField = "#et_pb_contact_name_1"
    private val rightMessageField = "#et_pb_contact_message_1"
    private val rightForm = "#et_pb_contact_form_1"
    private val submitButton = ".et_pb_contact_submit"
    private val captchaQuestionField = ".et_pb_contact_captcha_question"
    private val captchaResultField = ".et_pb_contact_captcha"

    private val url = "https://www.ultimateqa.com/filling-out-forms/"

    private val emptyFieldsMessage = "Please, fill in the following fields:"
    private val formFilledSuccessfullyMessage = "Form filled out successfully"
    private val successMessage = "Success"

    fun goToPage(){
        driver.get(url)
        findByCss(captchaQuestionField)
    }

    fun calculateCaptcha(): Int{
        val captchaText = findByCss(captchaQuestionField)!!.text as String
        val integers = captchaText.split("+")
        var sum = 0
        integers.forEach { sum += it.trim().toInt() }
        println("$captchaText = $sum")
        return sum
    }

    fun leaveLeftFieldsEmptyAndSubmit(){
        findByCss(leftNameField)!!.sendKeys("")
        findByCss(leftMessageField)!!.sendKeys("")
        findByCss(leftForm)!!.findByCss(submitButton)!!.click()
    }

    fun fieldsEmptyErrorMessageIsVisible(): Boolean{
        val message = findByText(emptyFieldsMessage)
        return message!!.isDisplayed
    }

    fun successfulRightSubmitMessageVisible(): Boolean {
        val message = findByText(successMessage)
        return message!!.isDisplayed
    }

    fun successfulLeftSubmitMessageVisible(): Boolean {
        val message = findByText(formFilledSuccessfullyMessage)
        return message!!.isDisplayed
    }
    
    fun fillRightFormWithCaptcha(){
        findByCss(rightNameField)!!.sendKeys("test name")
        findByCss(rightMessageField)!!.sendKeys("this message is 1 of 234 messages online")
        findByCss(captchaResultField)!!.sendKeys(calculateCaptcha().toString())
        findByCss(rightForm)!!.findByCss(submitButton)!!.click()
    }

    fun fillLeftForm(){
        findByCss(leftNameField)!!.sendKeys("test name")
        findByCss(leftMessageField)!!.sendKeys("this message is 1 of 234 messages online")
        findByCss(leftForm)!!.findByCss(submitButton)!!.click()
    }
}