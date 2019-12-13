package com.testbirds.selenium.techstack.step.page;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;

import com.testbirds.selenium.techstack.model.page.TodoMVC;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class TodoMVCSteps extends AbstractPageSteps<TodoMVC> {

    public TodoMVCSteps() {
        super(TodoMVC.class);
    }

    @Override
    @Given("I am on the TodoMVC page")
    public void loadPage() {
        super.loadPage();
    }

    @When("I open the TodoMVC page for the first time")
    public void clearCookiesAndLoadPage() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
        loadPage();
    }
}
