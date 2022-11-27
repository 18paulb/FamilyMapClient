package cs240.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import Request.LoginRequest;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    private Fragment loginFragment;
    private Fragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        //loginFragment = fragmentManager.findFragmentById(R.id.loginFragment);
        //mapFragment = fragmentManager.findFragmentById(R.id.mapFragment);
        //Going to return null
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);

        if (fragment == null) {
            fragment = createLoginFragment();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragmentFrameLayout, fragment);
                transaction.commit();
        }

    }

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    public void notifyLogin() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MapFragment();

        fragmentTransaction.setReorderingAllowed(true);

        fragmentTransaction.replace(R.id.fragmentFrameLayout, fragment);
        fragmentTransaction.commit();
    }
}