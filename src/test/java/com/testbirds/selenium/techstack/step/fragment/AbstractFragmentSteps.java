package com.testbirds.selenium.techstack.step.fragment;

import static com.codeborne.selenide.Selenide.page;

import com.testbirds.selenium.techstack.model.fragment.Fragment;

public abstract class AbstractFragmentSteps<T extends Fragment> {

    private T fragement;

    protected AbstractFragmentSteps(final Class<T> fragmentClass) {
        fragement = page(fragmentClass);
    }

    protected final T getFragment() {
        return fragement;
    }
}
