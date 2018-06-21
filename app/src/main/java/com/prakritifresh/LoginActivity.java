package com.prakritifresh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends Activity {
    public static final String userPREFERENCES = "UserPrefs";
    EditText PasswordEt;
    EditText UsernameEt;
    Button bsignup;
    Button btnLogin;
    String result;
    SharedPreferences sharedPreferences;
    Editor shared_edit;

    class C01841 implements OnClickListener {
        C01841() {
        }

        public void onClick(View v) {
            String username = UsernameEt.getText().toString();
            String password = PasswordEt.getText().toString();
            if (username.equals("") || password.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter all fields", 0).show();
            } else {
                getJSON("http://www.prakritifresh.com/cgi-bin/login.py", username, password);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UsernameEt = (EditText) findViewById(R.id.etUserName);
        PasswordEt = (EditText) findViewById(R.id.etPassword);
        sharedPreferences = getSharedPreferences("UserPrefs", 0);
        getWindow().setSoftInputMode(3);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new C01841());
        bsignup = (Button) findViewById(R.id.btn_signup);
        bsignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void getJSON(String url, String username, String password) {
        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;

            protected void onPreExecute() {

                super.onPreExecute();
                loading = new ProgressDialog(LoginActivity.this);
                loading.setMessage(Html.fromHtml("<b><h2>Please Wait...."));
                loading.show();
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
            }

            protected String doInBackground(String... params) {
                String uri = params[0];
                String user = params[1];
                String pass = params[2];
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(uri).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8"));
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
                shared_edit = sharedPreferences.edit();
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.get("status").toString().equals("SUCCESS")) {
                        Toast.makeText(getApplicationContext(), "Successfully logged in", 0).show();
                        shared_edit.putString("uid", jsonObject.getString("uid"));
                        shared_edit.commit();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        return;
                    }
                    new Error().Error(jsonObject.get("code").toString(), getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{url, username, password});
    }
}
