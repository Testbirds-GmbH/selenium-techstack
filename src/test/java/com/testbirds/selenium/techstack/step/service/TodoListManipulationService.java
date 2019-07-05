package com.testbirds.selenium.techstack.step.service;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.refresh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.testbirds.selenium.techstack.model.dto.LocalStorageItem;
import com.testbirds.selenium.techstack.util.JacksonHelper;

public class TodoListManipulationService {

    private static final Logger LOG = LoggerFactory.getLogger(TodoListManipulationService.class);

    private static final String LOCALSTORAGE_KEY = "react-todos";

    public final void clearList() {
        pushLocalStorageList("");
    }

    public final void setList(final List<LocalStorageItem> items) {
        String jsonList = "";
        try {
            jsonList = JacksonHelper.getMapper().writeValueAsString(items);
        } catch (final IOException e) {
            LOG.error("Failed to map List<LocalStorageItem> to json", e);
        }
        pushLocalStorageList(jsonList);
    }

    public final List<LocalStorageItem> getList() {
        final String jsonList = pullLocalStorageList();
        final List<LocalStorageItem> items = new ArrayList<LocalStorageItem>();
        try {
            items.addAll(JacksonHelper.getMapper().readValue(jsonList, new TypeReference<List<LocalStorageItem>>() {
            }));
        } catch (final IOException e) {
            LOG.error("Failed to read List<LocalStorageItem> from local storage", e);
        }
        return items;
    }

    public final void forEach(final Consumer<LocalStorageItem> action) {
        final List<LocalStorageItem> items = getList();
        items.forEach(action);
        setList(items);
    }

    public final void addItem(final LocalStorageItem item) {
        final List<LocalStorageItem> items = getList();
        items.add(item);
        setList(items);
    }

    public final void editItem(final int position, final Consumer<LocalStorageItem> action) {
        List<LocalStorageItem> items = getList();
        action.accept(items.get(position - 1));
        setList(items);
    }

    private void pushLocalStorageList(final String jsonList) {
        executeJavaScript("window.localStorage.setItem('" + LOCALSTORAGE_KEY + "', '" + jsonList + "');");
        refresh();
    }

    private String pullLocalStorageList() {
        return executeJavaScript("return window.localStorage.getItem('" + LOCALSTORAGE_KEY + "');");
    }
}
