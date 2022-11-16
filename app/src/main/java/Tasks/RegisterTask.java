package Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.List;

import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.RegisterResult;
import cs240.client.DataCache;
import cs240.client.ServerProxy;

public class RegisterTask implements Runnable {
    private final Handler handler;
    private final RegisterRequest request;

    private final String serverHost;
    private final String serverPort;

    public RegisterTask(Handler handler, RegisterRequest request, String serverHost, String serverPort) {
        this.handler = handler;
        this.request = request;
        this.serverHost = serverHost;
        this.serverPort = serverPort;

    }

    @Override
    public void run() {
        System.out.println("Register Task is running");
        RegisterResult result = new RegisterResult();
        DataCache cache = DataCache.getInstance();

        if (request.getUsername().equals("") || request.getPassword().equals("") || request.getFirstName().equals("")
            || request.getLastName().equals("")|| request.getEmail().equals("")) {
            sendMessage("","",false);
        }
        else {

            try {
                //10.0.2.2
                result = ServerProxy.register(serverHost, serverPort, this.request);

                if (!result.isSuccess()) {
                    sendMessage("","",false);
                }
                else {
                    GetAllPersonResult persons = ServerProxy.getPersons(serverHost, serverPort, result.getAuthtoken());
                    cache.setPeople(persons.getData());

                    GetAllEventResult events = ServerProxy.getEvents(serverHost, serverPort, result.getAuthtoken());
                    cache.setEvents(events.getData());

                    sendMessage(request.getFirstName(), request.getLastName(), result.isSuccess());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String first, String last, boolean success) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString("firstName", first);
        messageBundle.putString("lastName", last);
        messageBundle.putBoolean("Success", success);
        message.setData(messageBundle);
        handler.sendMessage(message);
    }
}
