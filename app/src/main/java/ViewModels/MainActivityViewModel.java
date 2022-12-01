package ViewModels;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private int test = 0;

    public int getTest() {
        return test;
    }

    public void increment() {
        test++;
    }
}
