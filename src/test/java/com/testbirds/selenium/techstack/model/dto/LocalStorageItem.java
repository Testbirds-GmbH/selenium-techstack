package com.testbirds.selenium.techstack.model.dto;

import java.util.UUID;

public class LocalStorageItem {

    private String id;
    private String title;
    private boolean completed;

    public LocalStorageItem() {
        this("item");
    }

    public LocalStorageItem(final String title) {
        this(title, false);
    }

    public LocalStorageItem(final String title, final boolean completed) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.completed = completed;
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public final boolean isCompleted() {
        return completed;
    }

    public final void setCompleted(final boolean completed) {
        this.completed = completed;
    }
}
