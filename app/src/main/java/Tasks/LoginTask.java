package Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Model.Person;
import Request.LoginRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.LoginResult;
import cs240.client.DataCache;
import cs240.client.ServerProxy;

public class LoginTask implements Runnable{
    private final Handler handler;
    private final LoginRequest request;

    private final String serverHost;
    private final String serverPort;

    public LoginTask(Handler handler, LoginRequest request, String serverHost, String serverPort) {
        this.handler = handler;
        this.request = request;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        //Runs the serverProxy
        System.out.println("Login Task is running");
        DataCache cache = DataCache.getInstance();
        LoginResult result = new LoginResult();


        if (request.getUsername().equals("") || request.getPassword().equals("")) {
            sendMessage("","",false);
        }
        else {
            try {
                result = ServerProxy.login(serverHost, serverPort, this.request);

                if (!result.isSuccess()) {
                    sendMessage("","", result.isSuccess());
                }

                else {
                    //Get Persons and find the User and assign info
                    GetAllPersonResult persons = ServerProxy.getPersons(serverHost, serverPort, result.getAuthtoken());
                    cache.setPeople(persons.getData());

                    GetAllEventResult events = ServerProxy.getEvents(serverHost, serverPort, result.getAuthtoken());
                    cache.setEvents(events.getData());
                    String fname = "";
                    String lname = "";
                    for (Person person : persons.getData()) {
                        if (person.getPersonID().equals(result.getPersonID())) {
                            fname = person.getFirstName();
                            lname = person.getLastName();
                        }
                    }

                    sendMessage(fname, lname, result.isSuccess());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String firstName, String lastName, boolean success) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString("firstName", firstName);
        messageBundle.putString("lastName", lastName);
        messageBundle.putBoolean("Success", success);
        message.setData(messageBundle);
        handler.sendMessage(message);
    }
}
