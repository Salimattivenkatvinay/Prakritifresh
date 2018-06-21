package com.prakritifresh;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

public class Error {
    String cod;
    Context ctx;

    public void Error(String code, Context context) {
        ctx = context;
        cod = code;
        getJSON("http://www.prakritifresh.com/cgi-bin/py");
    }

    private void getJSON(String url) {
        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;
            String result;

            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... params) {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(cod, "UTF-8"));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                    }
                    result = sb.toString().trim();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    Toast.makeText(ctx, new JSONObject(result).getString("message"), 1).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{url});
    }
}
