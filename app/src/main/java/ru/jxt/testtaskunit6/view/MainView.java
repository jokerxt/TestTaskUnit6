package ru.jxt.testtaskunit6.view;

import ru.jxt.testtaskunit6.data.User;

public interface MainView {
    void updateTimerView(String format);
    void fillUserFields(User user);
    void showNetworkRequestErrorToast(String error);
}
