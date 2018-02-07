package ru.jxt.testtaskunit6.presenter;

import ru.jxt.testtaskunit6.view.MainView;

public interface MainPresenter {
    void attachView(MainView mainView);
    void detachView();
    void startSendingRequests();
    void stopSendingRequests();
    void startTimer();
}
