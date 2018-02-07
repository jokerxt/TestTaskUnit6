package ru.jxt.testtaskunit6.model;

public interface MainModel {
    void startTimer(MainModelImpl.TickCallback mTickCallback);
    void stopTimer();
    void startSendingRequests(MainModelImpl.AnswerCallback answerCallback);
    void stopSendingRequests();
}
