package cs240.client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.GetAllEventResult;
import Result.GetAllPersonResult;
import Result.LoginResult;
import Result.RegisterResult;

public class ServerProxy {

    public static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        String serverHost = args[0];
        String serverPort = args[1];
    }

    public static void test() {
        System.out.println("Hello World");
    }

    public static RegisterResult register(String serverHost, String serverPort, RegisterRequest request) throws IOException {

        RegisterResult result = new RegisterResult();

        try {
            System.out.println("Starting the Client Register");
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            //Specifies we would like to receive the server's response in JSON
            http.addRequestProperty("Accept", "application/json");

            System.out.println("Connecting to HTTP");

            http.connect();

            String reqData = gson.toJson(request);

            System.out.println("Writing to output stream");

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User registered");

                InputStream resBody = http.getInputStream();

                //String resData = resBody.toString();
                String resData = readString(resBody);

                result = gson.fromJson(resData, RegisterResult.class);

                System.out.println(resData);

            } else {
                System.out.println("Error: " + http.getResponseMessage());

                InputStream resBody = http.getErrorStream();

                String resData = resBody.toString();

                result = gson.fromJson(resData, RegisterResult.class);

                System.out.println(resData);
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();

            return new RegisterResult();
        }
    }

    public static LoginResult login(String serverHost, String serverPort, LoginRequest request) throws IOException {
        System.out.println("Starting the Client Login");

        LoginResult result = new LoginResult();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String reqData = gson.toJson(request);

            System.out.println("Writing to output stream");

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User Logged In");

                InputStream resBody = http.getInputStream();

                String resData = readString(resBody);

                result = gson.fromJson(resData, LoginResult.class);

                System.out.println(resData);

            } else {
                System.out.println("Error: " + http.getResponseMessage());

                InputStream resBody = http.getErrorStream();

                String resData = resBody.toString();

                result = gson.fromJson(resData, LoginResult.class);

                System.out.println(resData);
            }
        } catch (Exception e) {
            e.printStackTrace();

            return result;
        }
        return result;
    }

    public static GetAllPersonResult getPersons(String serverHost, String serverPort, String authToken) {

        GetAllPersonResult result = new GetAllPersonResult();

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");


            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();


            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");

            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(false);


            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");


            // Connect to the server and send the HTTP request
            http.connect();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);

                // Display the JSON data returned from the server
                System.out.println(respData);
                result = gson.fromJson(respData, GetAllPersonResult.class);
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);
                result = gson.fromJson(respData, GetAllPersonResult.class);
            }

            return result;
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return result;
        }
    }

    public static GetAllEventResult getEvents(String serverHost, String serverPort, String authToken) {
        GetAllEventResult result = new GetAllEventResult();

        try {

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");

            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();

                String respData = readString(respBody);

                System.out.println(respData);
                result = gson.fromJson(respData, GetAllEventResult.class);
            }
            else {

                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getErrorStream();

                String respData = readString(respBody);

                System.out.println(respData);
                result = gson.fromJson(respData, GetAllEventResult.class);
            }

            return result;
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
            return result;
        }
    }



    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    //login
    //LoginResult login(LoginRequest request) {}

    //register
    //RegisterResult register(RegisterRequest request) {}

    //getPeople

    //getEvents

    //clear
    //fill
    //load
}
