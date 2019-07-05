package com.testbirds.selenium.techstack.step.page;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

import com.testbirds.selenium.techstack.model.page.Page;

abstract class AbstractPageSteps<T extends Page> {

    private final T page;

    protected AbstractPageSteps(final Class<T> pageClass) {
        page = page(pageClass);
    }

    protected final T getPage() {
        return page;
    }

    public void loadPage() {
        open(getPage().getUrl());
    }
}
