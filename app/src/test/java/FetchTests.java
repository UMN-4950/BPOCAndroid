import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.umn.bpoc.bpocandroid.resource.Friend;
import edu.umn.bpoc.bpocandroid.resource.Location;
import edu.umn.bpoc.bpocandroid.resource.Notification;
import edu.umn.bpoc.bpocandroid.resource.User;

public class FetchTests {

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    @Test
    public void getAllUsersTest() {
        try {
            URL url = new URL("http://bpocrestservice.azurewebsites.net/api/Users/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String jsonUsers = readStream(in);
            Assert.assertNotNull(jsonUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void friendJSONTest() {
        try {
            Gson gson = new Gson();
            String json = "{}";
            Friend friend = gson.fromJson(json, Friend.class);
            Assert.assertNotNull(friend);
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

    @Test
    public void locationJSONTest() {
        try {
            // example date --> 2008-02-16T12:15:12
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            String json = "{}";
            Location location = gson.fromJson(json, Location.class);
            Assert.assertNotNull(location);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void notificationJSONTest() {
        try {
            Gson gson = new Gson();
            String json = "{}";
            Notification notification = gson.fromJson(json, Notification.class);
            Assert.assertNotNull(notification);
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

    @Test
    public void userJSONTest() {
        try {
            Gson gson = new Gson();
            String json = "{}";
            User user = gson.fromJson(json, User.class);
            Assert.assertNotNull(user);
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }
}
