package cs240.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import Request.LoginRequest;
import ViewModels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {


    private MainActivityViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }


    /*
     Intent intent = new Intent(currAcitivty.this, newActivity.class);
     startActivity(intent)
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);

        //If there is a previous activity state
        if (savedInstanceState != null) {

        }

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outstate) {
        super.onSaveInstanceState(outstate);
        //outstate.putInt("Test", 1);
    }
}