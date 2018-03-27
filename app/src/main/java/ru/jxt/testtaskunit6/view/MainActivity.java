package ru.jxt.testtaskunit6.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.jxt.testtaskunit6.data.Consts.RequestsMode;
import ru.jxt.testtaskunit6.model.MainModelImpl;
import ru.jxt.testtaskunit6.presenter.MainPresenter;
import ru.jxt.testtaskunit6.presenter.MainPresenterImpl;
import ru.jxt.testtaskunit6.R;
import ru.jxt.testtaskunit6.data.User;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter mainPresenter;
    private Button activateRequestsButton;
    private TextView timerTextView;
    private EditText ageEditText;
    private EditText firstNameEditText;
    private EditText secondNameEditText;
    private EditText addressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = (MainPresenterImpl) getLastCustomNonConfigurationInstance();
        if (mainPresenter == null) {
            mainPresenter = new MainPresenterImpl(new MainModelImpl());
            mainPresenter.startTimer();
        }
        mainPresenter.attachView(this);

        timerTextView = findViewById(R.id.timerTextView);
        activateRequestsButton = findViewById(R.id.activateRequestsButton);
        ageEditText = findViewById(R.id.ageEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        secondNameEditText = findViewById(R.id.secondNameEditText);
        addressEditText = findViewById(R.id.addressEditText);

        CharSequence savedAgeText = "";
        CharSequence savedFirstNameText = "";
        CharSequence savedSecondNameText = "";
        CharSequence savedAddressText = "";
        CharSequence savedTimerText = "00:00:00";
        RequestsMode savedButtonState = RequestsMode.DISABLE;

        if(savedInstanceState != null) {
            savedAgeText = savedInstanceState.getCharSequence("age", savedTimerText);
            savedFirstNameText = savedInstanceState.getCharSequence("fname", savedTimerText);
            savedSecondNameText = savedInstanceState.getCharSequence("sname", savedTimerText);
            savedAddressText = savedInstanceState.getCharSequence("addr", savedTimerText);
            savedTimerText = savedInstanceState.getCharSequence("timer", savedTimerText);
            savedButtonState = (RequestsMode) savedInstanceState.get("button");
        }

        ageEditText.setText(savedAgeText);
        firstNameEditText.setText(savedFirstNameText);
        secondNameEditText.setText(savedSecondNameText);
        addressEditText.setText(savedAddressText);
        timerTextView.setText(savedTimerText);
        activateRequestsButton.setTag(savedButtonState);
        activateRequestsButton.setText(savedButtonState == RequestsMode.DISABLE ?
                                                            R.string.buttonStart :
                                                            R.string.buttonStop);

        activateRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activateRequestsButton.getTag() == RequestsMode.DISABLE) {
                    mainPresenter.startSendingRequests();
                    activateRequestsButton.setTag(RequestsMode.ENABLE);
                    activateRequestsButton.setText(R.string.buttonStop);
                }
                else {
                    mainPresenter.stopSendingRequests();
                    activateRequestsButton.setTag(RequestsMode.DISABLE);
                    activateRequestsButton.setText(R.string.buttonStart);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("age", ageEditText.getText());
        outState.putCharSequence("fname", firstNameEditText.getText());
        outState.putCharSequence("sname", secondNameEditText.getText());
        outState.putCharSequence("addr", addressEditText.getText());
        outState.putCharSequence("timer", timerTextView.getText());
        outState.putSerializable("button", (RequestsMode) activateRequestsButton.getTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateTimerView(String timerText) {
        timerTextView.setText(timerText);
    }

    @Override
    public void fillUserFields(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ageEditText.setText(String.valueOf(user.getAge()));
                firstNameEditText.setText(user.getFirstName());
                secondNameEditText.setText(user.getSecondName());
                addressEditText.setText(user.getAddress());
            }
        });
    }

    @Override
    public void showNetworkRequestErrorToast(String error) {
        Toast.makeText(this, "111111  Network Request error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mainPresenter;
    }

    @Override
    protected void onDestroy() {
        mainPresenter.detachView();
        super.onDestroy();
    }
}
