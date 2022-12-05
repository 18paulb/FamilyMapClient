package cs240.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import Request.LoginRequest;
import ViewModels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {


    private MainActivityViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);

        //If there is a previous activity state
        Iconify.with(new FontAwesomeModule());

        if (fragment == null) {
            fragment = createLoginFragment();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragmentFrameLayout, fragment);
                transaction.commit();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem settingsItem = menu.findItem(R.id.settings);

        settingsItem.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_cog)
                .colorRes(R.color.white)
                .actionBarSize()
        );

        MenuItem searchItem = menu.findItem(R.id.search);

        searchItem.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize()
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.settings:
                System.out.println("Settings Clicked");
                return true;
            case R.id.search:
                System.out.println("Search Clicked");
                return true;
            default: return super.onOptionsItemSelected(menu);
        }
    }

     */




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
    }
}