package cs240.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
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
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Toast toast;

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

    boolean maleChecked;
    boolean femaleChecked;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        username = view.findViewById(R.id.usernameField);
        serverHost = view.findViewById(R.id.serverHostField);
        serverPort = view.findViewById(R.id.serverPortField);

        password = view.findViewById(R.id.passwordField);
        firstName = view.findViewById(R.id.firstNameField);
        lastName = view.findViewById(R.id.lastNameField);
        email = view.findViewById(R.id.emailField);

        registerButton = view.findViewById(R.id.registerButton);
        loginButton = view.findViewById(R.id.loginButton);

        serverHost.setText("10.0.2.2");
        serverPort.setText("8080");


        male = view.findViewById(R.id.maleOption);
        female = view.findViewById(R.id.femaleOption);

        Button registerButton = view.findViewById(R.id.registerButton);
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

                            if (success) {
                                Toast.makeText(getContext(), "Registering: " + fname + " " + lname, Toast.LENGTH_LONG).show();
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
                                Toast.makeText(getContext(), "Logging In: " + fname + " " + lname, Toast.LENGTH_LONG).show();
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


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

}