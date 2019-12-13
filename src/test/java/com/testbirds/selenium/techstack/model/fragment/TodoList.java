package com.testbirds.selenium.techstack.model.fragment;

import static com.codeborne.selenide.Selenide.*;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class TodoList implements Fragment {

    private static final String CONTEXT = ".todoapp";

    private static final String HEADER = CONTEXT + " .header";

    private static final String MAIN = CONTEXT + " .main";
    private static final String LIST_TYPE = "ul";
    private static final String LIST_CLASS = "todo-list";
    private static final String ITEM_TYPE = "li";
    private static final String ITEM_CHECKBOX_SELECTOR = "input.toggle";
    private static final String ITEM_LABEL_SELECTOR = "label";
    private static final String ITEM_DELETE_BUTTON_SELECTOR = "button.destroy";
    private static final String ITEM_EDIT_INPUT_SELECTOR = "input.edit";

    private static final String FOOTER = CONTEXT + " .footer";
    private static final String FILTER = FOOTER + " .filters > li";
    private static final int FILTER_POSITION_ALL = 1;
    private static final int FILTER_POSITION_ACTIVE = 2;
    private static final int FILTER_POSITION_COMPLETED = 3;
    private static final String CLEAR_COMPLETED_BUTTON = FOOTER + " .clear-completed";

    // Header Elements

    @FindBy(
            how = How.CSS,
            using = HEADER + " h1")
    private SelenideElement headline;

    @FindBy(
            how = How.CSS,
            using = HEADER + " .new-todo")
    private SelenideElement todoInput;

    public SelenideElement getHeadline() {
        return headline;
    }

    public SelenideElement getTodoInput() {
        return todoInput;
    }

    // Main Elements

    @FindBy(
            how = How.CSS,
            using = MAIN + " input#toggle-all")
    private SelenideElement toggleAllCheckbox;

    public SelenideElement getToggleAllCheckbox() {
        return toggleAllCheckbox;
    }

    @FindBy(
            how = How.CSS,
            using = MAIN + " label[for='toggle-all']")
    private SelenideElement toggleAllCheckboxLabel;

    public SelenideElement getToggleAllCheckboxLabel() {
        return toggleAllCheckboxLabel;
    }

    private String getListSelector() {
        return MAIN + " > " + LIST_TYPE + "." + LIST_CLASS;
    }

    private String getItemSelector() {
        return getListSelector() + " > " + ITEM_TYPE;
    }

    private String getItemCheckboxSelector() {
        return getItemSelector() + " " + ITEM_CHECKBOX_SELECTOR;
    }

    private String getItemLabelSelector() {
        return getItemSelector() + " " + ITEM_LABEL_SELECTOR;
    }

    private String getItemEditInputSelector() {
        return getItemSelector() + " " + ITEM_EDIT_INPUT_SELECTOR;
    }

    private String getItemDeleteButtonSelector() {
        return getItemSelector() + " " + ITEM_DELETE_BUTTON_SELECTOR;
    }

    public ElementsCollection getListOfTodoElements() {
        return $$(getItemSelector());
    }

    public List<Item> createListOfTodoListItems(ElementsCollection listOfElements) {
        List<Item> result = new ArrayList<Item>();
        for (SelenideElement element : listOfElements) {
            result.add(new Item(element));
        }
        return result;
    }

    public ElementsCollection getAllItemCheckboxes() {
        return $$(getItemCheckboxSelector());
    }

    public ElementsCollection getAllItemLabels() {
        return $$(getItemLabelSelector());
    }

    public ElementsCollection getAllItemDeleteButtons() {
        return $$(getItemDeleteButtonSelector());
    }

    public ElementsCollection getAllItemEditInputs() {
        return $$(getItemEditInputSelector());
    }

    public Item getItemByPosition(int position) {
        return new Item(getListOfTodoElements().get(--position));
    }

    public Item getItemByText(String text) {
        return new Item(getListOfTodoElements().findBy(Condition.text(text)));
    }

    public class Item {

        private SelenideElement item;

        Item(SelenideElement item) {
            item.parent().shouldHave(Condition.cssClass(LIST_CLASS));
            this.item = item;
        }

        public SelenideElement getCheckbox() {
            return item.$(ITEM_CHECKBOX_SELECTOR);
        }

        public SelenideElement getLabelElement() {
            return item.$(ITEM_LABEL_SELECTOR);
        }

        public SelenideElement getDeleteButton() {
            return item.$(ITEM_DELETE_BUTTON_SELECTOR);
        }

        public SelenideElement getEditInput() {
            return item.$(ITEM_EDIT_INPUT_SELECTOR);
        }
    }

    // Footer Elements

    @FindBy(
            how = How.CSS,
            using = FOOTER + " .todo-count")
    private SelenideElement activeItemCount;

    public SelenideElement getActiveItemCount() {
        return activeItemCount;
    }

    private SelenideElement getFilterButtonByPosition(int position) {
        return $(FILTER + ":nth-of-type(" + position + ") > a");
    }

    public SelenideElement getFilterButtonAll() {
        return getFilterButtonByPosition(FILTER_POSITION_ALL);
    }

    public SelenideElement getFilterButtonActive() {
        return getFilterButtonByPosition(FILTER_POSITION_ACTIVE);
    }

    public SelenideElement getFilterButtonCompleted() {
        return getFilterButtonByPosition(FILTER_POSITION_COMPLETED);
    }

    @FindBy(
            how = How.CSS,
            using = CLEAR_COMPLETED_BUTTON)
    private SelenideElement clearCompletedButton;

    public SelenideElement getClearCompletedButton() {
        return clearCompletedButton;
    }
}
