package cs240.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.RegisterResult;
import Tasks.LoginTask;
import Tasks.RegisterTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment  {

    private Listener listener;

    public interface Listener {
        void notifyLogin();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    public EditText serverHost;
    public EditText serverPort;
    public EditText username;
    public EditText password;
    public EditText firstName;
    public EditText lastName;
    public EditText email;

    public RadioButton male;
    public RadioButton female;

    public Button registerButton;
    public Button loginButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater infaler) {

    }

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        serverHost = view.findViewById(R.id.serverHostField);
        serverPort = view.findViewById(R.id.serverPortField);

        username = view.findViewById(R.id.usernameField);
        password = view.findViewById(R.id.passwordField);
        firstName = view.findViewById(R.id.firstNameField);
        lastName = view.findViewById(R.id.lastNameField);
        email = view.findViewById(R.id.emailField);

        registerButton = view.findViewById(R.id.registerButton);
        loginButton = view.findViewById(R.id.loginButton);

        registerButton.setEnabled(false);
        loginButton.setEnabled(false);

        serverHost.setText("10.0.2.2");
        serverPort.setText("8080");
        username.setText("sheila");
        password.setText("parker");

        male = view.findViewById(R.id.maleOption);
        female = view.findViewById(R.id.femaleOption);

        Button registerButton = view.findViewById(R.id.registerButton);

        if (!username.getText().equals("") && !password.getText().equals("")) {
            loginButton.setEnabled(true);
        }


        //Text Watchers
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputFirst = firstName.getText().toString();
                String inputLast = lastName.getText().toString();
                String inputEmail = email.getText().toString();

                loginButton.setEnabled(!inputUsername.equals("") && !inputPassword.equals(""));
                registerButton.setEnabled(!inputUsername.equals("") && !inputPassword.equals("") && !inputFirst.equals("")
                        && !inputLast.equals("") && !inputEmail.equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputFirst = firstName.getText().toString();
                String inputLast = lastName.getText().toString();
                String inputEmail = email.getText().toString();

                loginButton.setEnabled(!inputUsername.equals("") && !inputPassword.equals(""));
                registerButton.setEnabled(!inputUsername.equals("") && !inputPassword.equals("") && !inputFirst.equals("")
                        && !inputLast.equals("") && !inputEmail.equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputFirst = firstName.getText().toString();
                String inputLast = lastName.getText().toString();
                String inputEmail = email.getText().toString();

                registerButton.setEnabled(!inputUsername.equals("") && !inputPassword.equals("") && !inputFirst.equals("")
                && !inputLast.equals("") && !inputEmail.equals(""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputFirst = firstName.getText().toString();
                String inputLast = lastName.getText().toString();
                String inputEmail = email.getText().toString();

                registerButton.setEnabled(!inputUsername.equals("") && !inputPassword.equals("") && !inputFirst.equals("")
                        && !inputLast.equals("") && !inputEmail.equals(""));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputUsername = username.getText().toString();
                String inputPassword = password.getText().toString();
                String inputFirst = firstName.getText().toString();
                String inputLast = lastName.getText().toString();
                String inputEmail = email.getText().toString();

                registerButton.setEnabled(!inputUsername.equals("") && !inputPassword.equals("") && !inputFirst.equals("")
                        && !inputLast.equals("") && !inputEmail.equals(""));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //Register Button Listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {

                    Handler uiThreadRegister = new Handler() {

                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();

                            //TODO: First and last name of user logged
                            String fname = bundle.getString("firstName");
                            String lname = bundle.getString("lastName");
                            boolean success = bundle.getBoolean("Success");
                            listener.notifyLogin();

                            if (success) {
                                Toast.makeText(getContext(), "Registering: " + fname + " " + lname, Toast.LENGTH_SHORT).show();

                                //Initializes Settings
                                DataCache cache = DataCache.getInstance();
                                cache.setSettings("LifeStory", true);
                                cache.setSettings("FamilyLines", true);
                                cache.setSettings("SpouseLines", true);
                                cache.setSettings("FatherLines", true);
                                cache.setSettings("MotherLines", true);
                                cache.setSettings("MaleEvents", true);
                                cache.setSettings("FemaleEvents", true);

                                cache.setUserID(bundle.getString("personID"));
                            }
                            else {
                                Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_LONG).show();
                            }

                            username.setText("");
                            password.setText("");
                            firstName.setText("");
                            lastName.setText("");
                            email.setText("");
                        }

                    };

                    String inputHost = serverHost.getText().toString();
                    String inputPort = serverPort.getText().toString();
                    String inputUsername = username.getText().toString();
                    String inputPassword = password.getText().toString();
                    String inputFirst = firstName.getText().toString();
                    String inputLast = lastName.getText().toString();
                    String inputEmail = email.getText().toString();

                    RegisterRequest request = null;
                    if (male.isChecked()) {
                        request = new RegisterRequest(inputUsername, inputPassword, inputEmail,
                                inputFirst, inputLast, "m");
                    }
                    else if (female.isChecked()) {
                        request = new RegisterRequest(inputUsername, inputPassword, inputEmail,
                                inputFirst, inputLast, "f");
                    }
                    else {
                        Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_LONG).show();
                    }

                    if (request != null) {
                        RegisterTask registerTask = new RegisterTask(uiThreadRegister, request, inputHost, inputPort);
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(registerTask);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //Login Button Listener
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                try {
                    Handler uiThreadLogin = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();

                            String fname = bundle.getString("firstName");
                            String lname = bundle.getString("lastName");
                            boolean success = bundle.getBoolean("Success");

                            if (success) {
                                Toast.makeText(getContext(), "Logging In: " + fname + " " + lname, Toast.LENGTH_SHORT).show();
                                listener.notifyLogin();

                                //Initializes Settings
                                DataCache cache = DataCache.getInstance();
                                cache.setSettings("LifeStory", true);
                                cache.setSettings("FamilyLines", true);
                                cache.setSettings("SpouseLines", true);
                                cache.setSettings("FatherLines", true);
                                cache.setSettings("MotherLines", true);
                                cache.setSettings("MaleEvents", true);
                                cache.setSettings("FemaleEvents", true);

                                cache.setUserID(bundle.getString("personID"));
                            } else {
                                Toast.makeText(getContext(), "Failed to Login", Toast.LENGTH_LONG).show();
                            }

                            username.setText("");
                            password.setText("");
                            firstName.setText("");
                            lastName.setText("");
                            email.setText("");
                        }
                    };

                    String inputUsername = username.getText().toString();
                    String inputPassword = password.getText().toString();
                    String inputHost = serverHost.getText().toString();
                    String inputPort = serverPort.getText().toString();

                    //Login
                    LoginRequest request = new LoginRequest(inputUsername, inputPassword);

                    LoginTask loginTask = new LoginTask(uiThreadLogin, request, inputHost, inputPort);

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(loginTask);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}