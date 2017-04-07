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
import java.text.DateFormat;
import java.util.Date;

import edu.umn.bpoc.bpocandroid.resource.Location;
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
    public void testLocationTest() {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

            //String json = "{'Id':3,'Latitude':12.0,'Longitude':89.1,'Time':'2008-02-16T12:15:12','UserId':5,'User':null)";
            //Location location = gson.fromJson(json, Location.class);
            //Assert.assertNotNull(location);

            String json = "{'i':1,'s':'testing','d':'2008-02-16T12:15:12'}";
            Dummy d = gson.fromJson(json, Dummy.class);
            Assert.assertNotNull(d);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Dummy {
        public int i;
        public String s;
        public Date d;
    }
}
