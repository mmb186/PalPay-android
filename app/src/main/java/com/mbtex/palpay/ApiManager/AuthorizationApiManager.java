package com.mbtex.palpay.ApiManager;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthorizationApiManager extends ApiManager {


    public AuthorizationApiManager() {}


    public String loginUser(String username, String password) {
        return "";
    }

    public String registerUser(JSONObject formData)
    {
        String req_data;
        req_data = this.doInBackground(_baseURL.concat("/create_user/"), formData.toString());
        System.out.println(req_data);
        return req_data;
    }

    public Boolean logoutUser (String username)
    {
        return false;
    }

    public String refereshToken()
    {
        return "";
    }

    @Override
    protected String doInBackground(String... params) {
        String request_data = "";

        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("PostData=" + params[1]);
            wr.flush();
            wr.close();

            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1)
            {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                request_data += current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return request_data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e("TAG", result);
    }


}
