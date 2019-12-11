package com.example.hello.presenter.callback;

public interface CallBack<T> {
    void onSuccess(T response);

    void onError(Throwable t);
}
