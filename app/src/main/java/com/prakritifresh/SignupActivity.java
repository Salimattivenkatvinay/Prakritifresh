package com.prakritifresh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private Button buttonRegister;
    private EditText city;
    private EditText coneditpassword;
    private TextInputLayout cpassi;
    private EditText edit_email;
    private EditText editpassword;
    private TextInputLayout emaili;
    private EditText fname;
    private TextView gologin;
    private EditText landmark;
    private EditText lname;
    private TextInputLayout mobi;
    private TextInputLayout passi;
    private EditText phone;
    private RadioGroup pincode;
    private RadioButton pincode_button;
    private EditText plotno;
    String result = "";
    private EditText state;
    private EditText street;

    class C01901 implements TextWatcher {
        C01901() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                emaili.setError(null);
                return;
            }
            emaili.setErrorEnabled(true);
            emaili.setError("Please Enter a Valid Email");
        }

        public void afterTextChanged(Editable s) {
            if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                emaili.setError(null);
                emaili.setErrorEnabled(false);
                return;
            }
            emaili.setErrorEnabled(true);
            emaili.setError("Please Enter a Valid Email");
        }
    }

    class C01912 implements TextWatcher {
        C01912() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() < 8) {
                passi.setErrorEnabled(true);
                passi.setError("Password length should be atlest 8 characters");
                return;
            }
            passi.setErrorEnabled(false);
            passi.setError(null);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    class C01923 implements TextWatcher {
        C01923() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editpassword.getText().toString().matches(coneditpassword.getText().toString())) {
                cpassi.setErrorEnabled(false);
                cpassi.setError(null);
                return;
            }
            cpassi.setErrorEnabled(true);
            cpassi.setError("Passwords do not match");
        }

        public void afterTextChanged(Editable s) {
            if (passi.getEditText().getText().toString().matches(s.toString())) {
                cpassi.setError(null);
                return;
            }
            cpassi.setErrorEnabled(true);
            cpassi.setError("Passwords do not match");
        }
    }

    class C01934 implements TextWatcher {
        C01934() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().startsWith("9") || s.toString().startsWith("8") || (s.toString().startsWith("7") && s.length() == 10)) {
                mobi.setError(null);
                return;
            }
            mobi.setErrorEnabled(true);
            mobi.setError("Invalid Mobile Number");
        }

        public void afterTextChanged(Editable s) {
            if (s.toString().startsWith("9") || s.toString().startsWith("8") || (s.toString().startsWith("7") && s.length() == 10)) {
                mobi.setError(null);
                return;
            }
            mobi.setErrorEnabled(true);
            mobi.setError("Invalid Mobile Number");
        }
    }

    class C01945 implements OnClickListener {
        C01945() {
        }

        public void onClick(View v) {
            registerUser();
        }
    }

    class C01956 implements OnClickListener {
        C01956() {
        }

        public void onClick(View v) {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_signup);
        fname = (EditText) findViewById(R.id.input_fname);
        lname = (EditText) findViewById(R.id.input_lname);
        edit_email = (EditText) findViewById(R.id.input_email);
        editpassword = (EditText) findViewById(R.id.input_password1);
        coneditpassword = (EditText) findViewById(R.id.input_password2);
        phone = (EditText) findViewById(R.id.input_phone);
        plotno = (EditText) findViewById(R.id.input_plot);
        street = (EditText) findViewById(R.id.input_street);
        landmark = (EditText) findViewById(R.id.input_landmark);
        state = (EditText) findViewById(R.id.input_state);
        city = (EditText) findViewById(R.id.input_city);
        pincode = (RadioGroup) findViewById(R.id.input_pincode);
        emaili = (TextInputLayout) findViewById(R.id.emaili);
        passi = (TextInputLayout) findViewById(R.id.passi);
        cpassi = (TextInputLayout) findViewById(R.id.cpassi);
        mobi = (TextInputLayout) findViewById(R.id.mobi);
        gologin = (TextView) findViewById(R.id.gologin);
        emaili.getEditText().addTextChangedListener(new C01901());
        passi.getEditText().addTextChangedListener(new C01912());
        cpassi.getEditText().addTextChangedListener(new C01923());
        mobi.getEditText().addTextChangedListener(new C01934());
        buttonRegister = (Button) findViewById(R.id.btn_signup);
        buttonRegister.setOnClickListener(new C01945());
        gologin.setOnClickListener(new C01956());
    }

    private void registerUser() {
        String sfname = fname.getText().toString();
        String slname = lname.getText().toString();
        String sedit_email = edit_email.getText().toString();
        String seditpassword = editpassword.getText().toString();
        String sconeditpassword = coneditpassword.getText().toString();
        String sphone = phone.getText().toString();
        String splotno = plotno.getText().toString();
        String sstreet = street.getText().toString();
        String slandmark = landmark.getText().toString();
        String sstate = state.getText().toString();
        String scity = city.getText().toString();
        pincode_button = (RadioButton) findViewById(pincode.getCheckedRadioButtonId());
        String spincode = pincode_button.getText().toString();
        if (sfname.matches("") || slname.matches("") || sconeditpassword.matches("") || sphone.matches("") || splotno.matches("") || sfname.matches("") || sstreet.matches("") || slandmark.matches("") || sstate.matches("") || scity.matches("") || sfname.matches("") || spincode.matches("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", 0).show();
        } else if (!seditpassword.equals(sconeditpassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", 0).show();
        } else if (sphone.length() != 10) {
            Toast.makeText(getApplicationContext(), "Please fill 10-digit Mobile Number", 0).show();
        } else if (spincode.length() < 6) {
            Toast.makeText(getApplicationContext(), "Please fill 6-digit Pin", 0).show();
        } else {
            register(sfname, slname, sedit_email, seditpassword, sphone, splotno, sstreet, slandmark, sstate, scity, spincode, "http://www.prakritifresh.com/cgi-bin/signup.py");
        }
    }

    private void register(String fname, String lname, String edit_email, String editpassword, String phone, String plotno, String street, String landmark, String state, String city, String pincode, String url) {
        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(SignupActivity.this);
                loading.setMessage(Html.fromHtml("<b><h2>Please Wait...."));
                loading.show();
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader;
                Exception e;
                HashMap<String, String> data = new HashMap();
                data.put("firstname", params[0]);
                data.put("lastname", params[1]);
                data.put("email", params[2]);
                data.put("password", params[3]);
                data.put("phone", params[4]);
                data.put("plot", params[5]);
                data.put("street", params[6]);
                data.put("landmark", params[7]);
                data.put("state", params[8]);
                data.put("city", params[9]);
                data.put("pin", params[10]);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(params[11]).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode((String) data.get("firstname"), "UTF-8") + "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode((String) data.get("lastname"), "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode((String) data.get("email"), "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode((String) data.get("password"), "UTF-8") + "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode((String) data.get("phone"), "UTF-8") + "&" + URLEncoder.encode("street", "UTF-8") + "=" + URLEncoder.encode((String) data.get("street"), "UTF-8") + "&" + URLEncoder.encode("landmark", "UTF-8") + "=" + URLEncoder.encode((String) data.get("landmark"), "UTF-8") + "&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode((String) data.get("state"), "UTF-8") + "&" + URLEncoder.encode("plot", "UTF-8") + "=" + URLEncoder.encode((String) data.get("plot"), "UTF-8") + "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode((String) data.get("city"), "UTF-8") + "&" + URLEncoder.encode("pin", "UTF-8") + "=" + URLEncoder.encode((String) data.get("pin"), "UTF-8"));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        String line = "";
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            line = bufferedReader2.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                        }
                        result = sb.toString().trim();
                        bufferedReader2.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        bufferedReader = bufferedReader2;
                    } catch (Exception e2) {
                        e = e2;
                        bufferedReader = bufferedReader2;
                        e.printStackTrace();
                        return result;
                    }
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                    return result;
                }
                return result;
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.get("status").toString().matches("SUCCESS")) {
                        Toast.makeText(getApplicationContext(), "Successfully registered", 0).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        return;
                    }
                    new Error().Error(jsonObject.get("status").toString(), getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{fname, lname, edit_email, editpassword, phone, plotno, street, landmark, state, city, pincode, url});
    }
}
