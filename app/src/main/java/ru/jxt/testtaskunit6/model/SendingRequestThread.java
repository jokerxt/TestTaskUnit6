package ru.jxt.testtaskunit6.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.jxt.testtaskunit6.data.Consts;
import ru.jxt.testtaskunit6.data.User;

import static ru.jxt.testtaskunit6.data.Consts.RequestErrors.IO;
import static ru.jxt.testtaskunit6.data.Consts.RequestErrors.NONE;
import static ru.jxt.testtaskunit6.data.Consts.RequestErrors.TIMEOUT;

/**
 * Created by JokerXT on 07.02.2018.
 */
class SendingRequestThread extends Thread {

    private MainModelImpl.AnswerCallback answerCallback;
    private OkHttpClient httpClient;
    private Request request;

    SendingRequestThread(MainModelImpl.AnswerCallback answerCallback) {
        this.answerCallback = answerCallback;
        httpClient = getHttpClientWithTimeout(10);
        request = getRequestWithUrl("http://androidtesttask.dev.unit6.ru/androidapp/getuser");
    }

    private Request getRequestWithUrl(String url) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        return builder.build();
    }

    private OkHttpClient getHttpClientWithTimeout(long timeoutSec) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeoutSec, TimeUnit.SECONDS);
        builder.readTimeout(timeoutSec, TimeUnit.SECONDS);
        builder.writeTimeout(timeoutSec, TimeUnit.SECONDS);
        return builder.build();
    }

    @Override
    public void run() {
        Consts.RequestErrors error = NONE;
        while (!isInterrupted()) {
            try {
                Response response = httpClient.newCall(request).execute();
                if (!isInterrupted()) {
                    if (response != null && response.body() != null) {
                        String jsonData = response.body().string();
                        User user = new Gson().fromJson(jsonData, User.class);
                        if (answerCallback != null) {
                            answerCallback.onAnswerCallback(user);
                        }
                    }
                }
            } catch (SocketTimeoutException e) {
                long currentTimeoutSec = TimeUnit.MILLISECONDS.toSeconds(httpClient.readTimeoutMillis());
                if (currentTimeoutSec == 40) {
                    interrupt();
                    error = TIMEOUT;
                } else {
                    if (!isInterrupted()) {
                        httpClient = getHttpClientWithTimeout(currentTimeoutSec + 10);
                    }
                }
            } catch (IOException e) {
                interrupt();
                error = IO;
            }
        }

        if (error != NONE && answerCallback != null) {
            answerCallback.onErrorCallback(error);
        }
    }
}
