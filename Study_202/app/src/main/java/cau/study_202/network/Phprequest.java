package cau.study_202.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import cau.study_202.LoginStatus;
import cau.study_202.MemberActivity;

public class Phprequest {
    public static final String BASE_URL = "http://54.180.65.106/";
    public static String postData;
    private URL url;

    public Phprequest(String url) throws MalformedURLException { this.url = new URL(url); }

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
        String line = null;

        while((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }

    public String registmember(final String ID, final String PASSWORD,final String NAME,final String NICKNAME,final String EMAIL,final String PHONE,final String BIRTHDAY) {
        try {
            String postData = "ID=" + ID + "&" + "PASSWORD=" + PASSWORD + "&" + "NAME=" + NAME+ "&" + "NICKNAME=" + NICKNAME+ "&" + "EMAIL=" + EMAIL+ "&" + "PHONE=" + PHONE+ "&" + "BIRTHDAY=" + BIRTHDAY;
            return accept(postData);
        }
        catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }



    public String createStudy(final String TITLE, final String CONTENT, final String ATTENDENCETIME, final String ATTENDENCELATE, final String LATEFINE, final String ABSENTFINE) {


            String postData = "TITLE=" + TITLE + "&" + "CONTENT=" + CONTENT + "&" + "ATTENDENCETIME=" + ATTENDENCETIME
                    + "&" + "ATTENDENCELATE=" + ATTENDENCELATE+ "&" + "LATEFINE=" + LATEFINE+ "&"
                    + "ABSENTFINE=" + ABSENTFINE + "&" + "LEADERID=" + LoginStatus.getMemberID();
            Log.i("PHPRequset", postData);

            return postData;
            //return accept(postData);


    }

    private String accept(final String postData){
        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.i("PHPRequest", "setDoinput밑");
            OutputStream outputStream = conn.getOutputStream();
            Log.i("PHPRequest", "outputstream밑");
            outputStream.write(postData.getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;
        }
        catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            e.printStackTrace();
            return null;
        }
    }

}