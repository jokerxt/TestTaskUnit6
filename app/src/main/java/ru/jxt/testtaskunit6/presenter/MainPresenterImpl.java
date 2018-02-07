package ru.jxt.testtaskunit6.presenter;

import java.util.Locale;
import ru.jxt.testtaskunit6.data.Consts;
import ru.jxt.testtaskunit6.data.User;
import ru.jxt.testtaskunit6.model.MainModel;
import ru.jxt.testtaskunit6.model.MainModelImpl;
import ru.jxt.testtaskunit6.view.MainView;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;
    private MainModel mainModel;

    public MainPresenterImpl(MainModelImpl mainModel) {
        this.mainModel = mainModel;
    }

    @Override
    public void attachView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void detachView() {
        mainView = null;
    }

    @Override
    public void startSendingRequests() {
        mainModel.startSendingRequests(new MainModelImpl.AnswerCallback() {
            @Override
            public void onAnswerCallback(User user) {
                mainView.fillUserFields(user);
            }

            @Override
            public void onErrorCallback(Consts.RequestErrors error) {
                mainView.showNetworkRequestErrorToast(error.name());
            }
        });
    }

    @Override
    public void stopSendingRequests() {
        mainModel.stopSendingRequests();
    }

    @Override
    public void startTimer() {
        mainModel.startTimer(new MainModelImpl.TickCallback() {
            @Override
            public void onTickCallback(long currentMillis) {
                int seconds = (int) (currentMillis / 1000);
                int minutes = seconds / 60;
                int hours = minutes / 60;
                minutes %= 60;
                seconds %= 60;

                String timerText = String.format(Locale.US,
                                                 "%02d:%02d:%02d",
                                                 hours,
                                                 minutes,
                                                 seconds);

                mainView.updateTimerView(timerText);
            }
        });
    }
}
