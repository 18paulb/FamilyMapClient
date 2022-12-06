package cs240.client;

import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import Request.RegisterRequest;
import Result.RegisterResult;

@RunWith(AndroidJUnit4.class)
public class RegisterTest {

    @Test
    public void registerTest() {
        RegisterRequest request = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(result.isSuccess());
    }

    @Test
    public void getRegisteredPeopleTest() {
        RegisterRequest request = new RegisterRequest("asdf", "asdf", "asdf" ,"asdf", "adsf", "m");

        RegisterResult result = new RegisterResult();
        try {
            result = ServerProxy.register("localhost", "8080", request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(result.isSuccess());
    }
}
