package com.example.hello.presenter.impl;

import com.example.hello.model.impl.MainAModelImpl;
import com.example.hello.model.inter.IMainAModel;
import com.example.hello.presenter.inter.IMainAPresenter;
import com.example.hello.view.inter.IMainAView;

public class MainAPresenterImpl implements IMainAPresenter {
    private IMainAView mIMainAView;
    private IMainAModel mIMainAModel;

    public MainAPresenterImpl(IMainAView aIMainAView) {
        mIMainAView = aIMainAView;
        mIMainAModel = new MainAModelImpl();
    }
}
