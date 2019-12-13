package com.testbirds.selenium.techstack.step.fragment;

import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.Selenide.actions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.testbirds.selenium.techstack.model.dto.LocalStorageItem;
import com.testbirds.selenium.techstack.model.fragment.TodoList;
import com.testbirds.selenium.techstack.step.service.TodoListManipulationService;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TodoListSteps extends AbstractFragmentSteps<TodoList> {

    private static final Logger LOG = LoggerFactory.getLogger(TodoListSteps.class);

    private final TodoListManipulationService todoListManipulationService;

    public TodoListSteps(TodoListManipulationService todoListManipulationService) {
        super(TodoList.class);
        this.todoListManipulationService = todoListManipulationService;
    }

    @After
    @Given("^I have an empty todo list$")
    public void after() {
        todoListManipulationService.clearList();
    }

    @Given("^I have a todo list with (\\d+) item(?:s)?$")
    public void createListWithItems(int numberOfItems) {
        List<LocalStorageItem> items = new ArrayList<LocalStorageItem>();
        for (int i = 1; i <= numberOfItems; i++) {
            items.add(new LocalStorageItem("item " + i));
        }
        todoListManipulationService.setList(items);
        getFragment().getListOfTodoElements().shouldHaveSize(numberOfItems);
    }

    @Given("^the (\\d+)(?:st|nd|rd|th) item in the todo list reads \"([^\"]+)\"$")
    public void updateListItemTitle(int position, String text) {
        todoListManipulationService.editItem(position, (item) -> {
            item.setTitle(text);
        });
        getFragment().getItemByText(text).getLabelElement().shouldBe(Condition.visible);
    }

    @Given("^the (\\d+)(?:st|nd|rd|th) item in the todo list is (not )?completed$")
    public void updateListItemStatus(int position, String notCompleted) {
        todoListManipulationService.editItem(position, (item) -> {
            item.setCompleted(!"not ".equals(notCompleted));
        });
        if ("not ".equals(notCompleted)) {
            getFragment().getItemByPosition(position).getCheckbox().shouldNotBe(Condition.checked);
        } else {
            getFragment().getItemByPosition(position).getCheckbox().shouldBe(Condition.checked);
        }
    }

    @Given("^the (\\d+)(?:st|nd|rd|th) item in the todo list reads \"([^\"]+)\" and is (not )?completed$")
    public void updateListItem(int position, String title, String notCompleted) {
        todoListManipulationService.editItem(position, (item) -> {
            item.setTitle(title);
            item.setCompleted(!"not ".equals(notCompleted));
        });
    }

    @Given("^all items in the todo list are (not )?completed$")
    public void ensureAllItemsInListAreActive(String notCompleted) {
        todoListManipulationService.forEach((item) -> {
            item.setCompleted(!"not ".equals(notCompleted));
        });
        getFragment().getAllItemCheckboxes().forEach((item) -> {
            if ("not ".equals(notCompleted)) {
                item.shouldNotBe(Condition.checked);
            } else {
                item.shouldBe(Condition.checked);
            }
        });
    }

    @Given("^(\\d+) item(?:s)? in the todo list are completed$")
    public void ensureItemsInListAreCompleted(int numberOfCompletedItems) {
        final AtomicInteger counter = new AtomicInteger(0);
        todoListManipulationService.forEach((item) -> {
            item.setCompleted(counter.incrementAndGet() <= numberOfCompletedItems);
        });
        getFragment().getAllItemCheckboxes().filterBy(Condition.checked).shouldHaveSize(numberOfCompletedItems);
    }

    @When("^I add \"([^\"]+)\" to the todo list$")
    public void addItem(String text) {
        getFragment().getTodoInput().setValue(text).pressEnter();
    }

    @When("^I add (\\d+)(?: more)? items to the todo list$")
    public void addItems(int numberOfItems) {
        for (int i = 1; i <= numberOfItems; i++) {
            addItem("additional item " + i);
        }
    }

    @When("^I complete the (\\d+)(?:st|nd|rd|th) item in the todo list$")
    public void completeItemAtPosition(int position) {
        itemAtPositionIsActive(position);
        clickItemCheckboxAtPosition(position);
    }

    @When("^I reactivate the (\\d+)(?:st|nd|rd|th) item in the todo list$")
    public void reactivateItemAtPosition(int position) {
        itemAtPositionIsCompleted(position);
        clickItemCheckboxAtPosition(position);
    }

    private void clickItemCheckboxAtPosition(int position) {
        getFragment().getItemByPosition(position).getCheckbox().click();
    }

    @When("^I complete all items in the todo list$")
    public void completeAllItems() {
        for (int i = 1; i <= getFragment().getListOfTodoElements().size(); i++) {
            completeItemAtPosition(i);
        }
    }

    @When("^I reactivate all items in the todo list$")
    public void reactivateAllItems() {
        for (int i = 1; i <= getFragment().getListOfTodoElements().size(); i++) {
            reactivateItemAtPosition(i);
        }
    }

    @When("^I change the (\\d+)(?:st|nd|rd|th) item in the todo list to read \"([^\"]+)\"")
    public void changeItemAtPosition(int position, String text) {
        TodoList.Item item = getFragment().getItemByPosition(position);
        item.getLabelElement().doubleClick();
        if ("safari".equals(Configuration.browser)) {
            LOG.warn("Known Bug: After this doubleClick, safari still doesn't show the edit");
            // FIXME: does not work on Safari
            item.getEditInput().sendKeys(Keys.chord(Keys.COMMAND, "a"));
        } else {
            item.getEditInput().sendKeys(Keys.chord(Keys.CONTROL, "a"));
        }
        item.getEditInput().sendKeys(Keys.DELETE);
        item.getEditInput().sendKeys(text);
        item.getEditInput().pressEnter();
    }

    /**
     * Helper to improve hovering on Microsoft browsers (IE and Edge).
     *
     * @param element the element to hover.
     */
    private void hover(final SelenideElement element) {
        element.hover();

        // Microsoft browsers don't work well with .hover(), so use actions additionally
        if ("ie".equals(Configuration.browser) || "edge".equals(Configuration.browser)) {
            actions().moveToElement(element.toWebElement()).perform();

            // extra sleep since onMouseOver events can take time to process
            try {
                Thread.sleep(1000L);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * When deleting a Todo list item, the item needs to be hovered to make the
     * delete button appear. Only after it appeared, it can be clicked. This leads
     * to tricky timing situations on most browsers.
     *
     * @param item the item to delete.
     */
    private void deleteTodoListItem(final TodoList.Item item) {
        // Hovering two elements was needed for many browsers (e.g. Firefox) to hover
        // -> after deleting one item, the second item is on the same position
        // -> therefore, the cursor was not really moved at all and hovering failed
        // Solution: we hover the new Headline first and then the label of the item
        hover(getFragment().getHeadline());

        // wait for the element to disappear in case it is still there (i.e. from
        // previous hover and delete)
        Wait().ignoring(Throwable.class).withTimeout(Duration.ofMillis(Configuration.timeout))
                .until(x -> item.getDeleteButton().is(Condition.hidden));

        // hover the label finally
        hover(item.getLabelElement());

        if ("ie".equals(Configuration.browser)) {
            // IE has to do click it with this hacky actions() sequences
            actions()
                    // move the cursor to the hidden delete button (cheating, but works)
                    .moveToElement(item.getDeleteButton().toWebElement())
                    // the delete button needs to appear first (takes around ~400ms, use more)
                    .pause(Duration.ofMillis(1000L))
                    // click the delete button which should have appeared now
                    .click()
                    // perform as a single action, to avoid network delays messing it up
                    .perform();
        } else {
            // wait explicitly for the delete button to appear and click it
            item.getDeleteButton().waitUntil(Condition.visible, Configuration.timeout);
            item.getDeleteButton().click();
        }
    }

    @When("^I delete \"([^\"]+)\" from the todo list$")
    public void deleteItem(String text) {
        TodoList.Item item = getFragment().getItemByText(text);
        deleteTodoListItem(item);
    }

    @When("^I delete the (\\d+)(?:st|nd|rd|th) item from the todo list$")
    public void deleteItemAtPosition(int position) {
        TodoList.Item item = getFragment().getItemByPosition(position);
        deleteTodoListItem(item);
    }

    @When("^I delete (\\d+) item(?:s)? from the todo list$")
    public void deleteItems(int numberOfItems) {
        for (int i = 1; i <= numberOfItems; i++) {
            deleteItemAtPosition(1);
        }
    }

    @When("^I filter the todo list for all items$")
    public void filterForAllItems() {
        getFragment().getFilterButtonAll().click();
    }

    @When("^I filter the todo list for not completed items$")
    public void filterForActiveItems() {
        getFragment().getFilterButtonActive().click();
    }

    @When("^I filter the todo list for completed items$")
    public void filterForCompletedItems() {
        getFragment().getFilterButtonCompleted().click();
    }

    @When("^I clear all completed items in the todo list$")
    public void clearCompletedItems() {
        getFragment().getClearCompletedButton().click();
    }

    @Then("^I will be able to add new items to the todo list$")
    public void todoInputIsVisible() {
        getFragment().getTodoInput().shouldBe(Condition.visible);
    }

    @Then("^I will not be able to complete all items in the todo list at once$")
    public void toggleAllButtonIsNotVisible() {
        getFragment().getToggleAllCheckboxLabel().shouldNotBe(Condition.visible);
        getFragment().getToggleAllCheckbox().shouldNot(Condition.exist);
    }

    @Then("^I will be able to complete all items in the todo list at once$")
    public void toggleAllCheckboxIsVisible() {
        getFragment().getToggleAllCheckbox().should(Condition.exist);
        getFragment().getToggleAllCheckboxLabel().shouldBe(Condition.visible);
    }

    @Then("^I will not be able to complete any item in the todo list individually$")
    public void allItemCheckboxesAreNotVisible() {
        getFragment().getAllItemCheckboxes().shouldHaveSize(0);
    }

    @Then("^I will be able to complete all (\\d+) items in the todo list individually$")
    public void itemCheckboxesAreVisible(int numberOfItems) {
        ElementsCollection listOfTodoElements = getFragment().getListOfTodoElements();
        listOfTodoElements.shouldHaveSize(numberOfItems);
        for (TodoList.Item item : getFragment().createListOfTodoListItems(listOfTodoElements)) {
            // checkbox is covered by label and is thus not visible to Selenium
            item.getCheckbox().should(Condition.exist);
            item.getLabelElement().shouldBe(Condition.visible);
        }
    }

    @Then("^the checkbox of the (\\d+)(?:st|nd|rd|th) item in the todo list will be checked$")
    public void itemAtPositionIsChecked(int position) {
        getFragment().getItemByPosition(position).getCheckbox().shouldBe(Condition.checked);
    }

    @Then("^the text of the (\\d+)(?:st|nd|rd|th) item in the todo list will be crossed out$")
    public void itemAtPositionIsCrossedOut(int position) {
        getFragment().getItemByPosition(position).getLabelElement().parent().parent()
                .shouldHave(Condition.cssClass("completed"));
    }

    @Then("^the (\\d+)(?:st|nd|rd|th) item in the todo list will be displayed as completed")
    public void itemAtPositionIsCompleted(int position) {
        itemAtPositionIsChecked(position);
        itemAtPositionIsCrossedOut(position);
    }

    @Then("^all items in the todo list will be displayed as completed$")
    public void allItemsAreCompleted() {
        for (int i = 1; i <= getFragment().getListOfTodoElements().size(); i++) {
            itemAtPositionIsCompleted(i);
        }
    }

    @Then("^the checkbox of the (\\d+)(?:st|nd|rd|th) item in the todo list will not be checked$")
    public void itemAtPositionIsNotChecked(int position) {
        getFragment().getItemByPosition(position).getCheckbox().shouldNotBe(Condition.checked);
    }

    @Then("^the text of the (\\d+)(?:st|nd|rd|th) item in the todo list will not be crossed out$")
    public void itemAtPositionIsNotCrossedOut(int position) {
        getFragment().getItemByPosition(position).getLabelElement().parent().parent()
                .shouldNotHave(Condition.cssClass("completed"));
    }

    @Then("^the (\\d+)(?:st|nd|rd|th) item in the todo list will be displayed as not completed")
    public void itemAtPositionIsActive(int position) {
        itemAtPositionIsNotChecked(position);
        itemAtPositionIsNotCrossedOut(position);
    }

    @Then("^all items in the todo list will be displayed as not completed$")
    public void allItemsAreActive() {
        for (int i = 1; i <= getFragment().getListOfTodoElements().size(); i++) {
            itemAtPositionIsNotChecked(i);
            itemAtPositionIsNotCrossedOut(i);
        }
    }

    @Then("^I will not see a count of all not completed items in the todo list$")
    public void activeItemCountIsNotVisible() {
        getFragment().getActiveItemCount().shouldNot(Condition.exist);
    }

    @Then("^I will see a count of all not completed items in the todo list$")
    public void activeItemCountIsVisible() {
        getFragment().getActiveItemCount().shouldBe(Condition.visible);
    }

    @Then("^I will not be able to filter for (all|not completed|completed) items in the todo list$")
    public void filterIsNotVisible(String filter) {
        switch (filter) {
        case "all":
            getFragment().getFilterButtonAll().shouldNotBe(Condition.visible);
            break;
        case "not completed":
            getFragment().getFilterButtonActive().shouldNotBe(Condition.visible);
            break;
        case "completed":
            getFragment().getFilterButtonCompleted().shouldNotBe(Condition.visible);
            break;
        default:
            throw new UnsupportedOperationException(
                    "Items can only be filtered by \"all\", \"not completed\" or \"completed\", but got " + filter);
        }
    }

    @Then("^I will be able to filter for (all|not completed|completed) items in the todo list$")
    public void filterIsVisisble(String filter) {
        switch (filter) {
        case "all":
            getFragment().getFilterButtonAll().shouldBe(Condition.visible);
            break;
        case "not completed":
            getFragment().getFilterButtonActive().shouldBe(Condition.visible);
            break;
        case "completed":
            getFragment().getFilterButtonCompleted().shouldBe(Condition.visible);
            break;
        default:
            throw new UnsupportedOperationException(
                    "Items can only be filtered by \"all\", \"not completed\" or \"completed\", but got " + filter);
        }
    }

    @Then("^I will not be able to clear all completed items in the todo list$")
    public void clearCompletedButtonIsNotVisible() {
        getFragment().getClearCompletedButton().shouldNotBe(Condition.visible);
    }

    @Then("^I will be able to clear all completed items in the todo list$")
    public void clearCompletedButtonIsVisible() {
        getFragment().getClearCompletedButton().shouldBe(Condition.visible);
    }

    @Then("^there will be exactly (\\d+) item(?:s)? in the todo list$")
    public void todoListHasSize(int expectedSize) {
        getFragment().getListOfTodoElements().shouldHaveSize(expectedSize);
    }

    @Then("^there will be \"([^\"]+)\" in the todo list$")
    public void itemIsInList(String text) {
        getFragment().getItemByText(text).getLabelElement().shouldBe(Condition.visible);
    }

    @Then("^\"([^\"]+)\" will be the (\\d+)(?:st|nd|rd|th) item in the todo list$")
    public void itemIsInListAtPosition(String text, int position) {
        getFragment().getItemByPosition(position).getLabelElement().shouldHave(Condition.text(text));
    }

    @Then("^the (\\d+)(?:st|nd|rd|th) item in the todo list will read \"([^\"]+)\"$")
    public void itemAtPositionHasText(int position, String text) {
        getFragment().getItemByPosition(position).getLabelElement().shouldHave(Condition.text(text));
    }

    @Then("^the number of not completed items in the todo list will be (\\d+)$")
    public void activeItemsCountEquals(int expectedNumber) {
        getFragment().getActiveItemCount().should(Condition.matchText(expectedNumber + " item[s]? left"));
    }
}
