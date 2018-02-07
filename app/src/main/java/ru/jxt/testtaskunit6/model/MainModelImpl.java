package ru.jxt.testtaskunit6.model;


import ru.jxt.testtaskunit6.data.Consts;
import ru.jxt.testtaskunit6.data.User;

public class MainModelImpl implements MainModel {

    private AppTimer timer;
    private SendingRequestThread sendingRequestThread;

    public interface AnswerCallback {
        void onAnswerCallback(User user);
        void onErrorCallback(Consts.RequestErrors error);
    }

    public interface TickCallback {
        void onTickCallback(long millis);
    }

    @Override
    public void startTimer(TickCallback mTickCallback) {
        if(timer == null) {
            timer = new AppTimer();
        }
        timer.start(mTickCallback);
    }

    @Override
    public void stopTimer() {
        if(timer != null) {
            timer.stop();
        }
    }

    @Override
    public void startSendingRequests(AnswerCallback answerCallback) {
        sendingRequestThread = new SendingRequestThread(answerCallback);
        sendingRequestThread.start();
    }

    @Override
    public void stopSendingRequests() {
        sendingRequestThread.interrupt();
    }
}
