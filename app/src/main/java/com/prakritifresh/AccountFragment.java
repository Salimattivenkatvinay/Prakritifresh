package com.prakritifresh;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountFragment extends Fragment {
    public static final String userPREFERENCES = "UserPrefs";
    private Button buttonRegister;
    private EditText city;
    private EditText edit_email;
    private TextInputLayout emaili;
    private EditText fname;
    private EditText landmark;
    private EditText lname;
    FragmentManager mFragmentManager;
    private TextInputLayout mobi;
    private EditText pincode;
    private EditText plotno;
    String result = "";
    private EditText state;
    private EditText street;
    SharedPreferences userprefs;
    View view;

    class C01751 implements TextWatcher {
        C01751() {
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
                return;
            }
            emaili.setErrorEnabled(true);
            emaili.setError("Please Enter a Valid Email");
        }
    }

    class C01762 implements OnClickListener {
        C01762() {
        }

        public void onClick(View v) {
            registerUser();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.account_layout, null);
        getJSON("http://www.prakritifresh.com/cgi-bin/account.py");
        fname = (EditText) view.findViewById(R.id.input_fname);
        lname = (EditText) view.findViewById(R.id.input_lname);
        edit_email = (EditText) view.findViewById(R.id.input_email);
        plotno = (EditText) view.findViewById(R.id.input_plot);
        street = (EditText) view.findViewById(R.id.input_street);
        landmark = (EditText) view.findViewById(R.id.input_landmark);
        state = (EditText) view.findViewById(R.id.input_state);
        city = (EditText) view.findViewById(R.id.input_city);
        pincode = (EditText) view.findViewById(R.id.input_pincode);
        emaili = (TextInputLayout) view.findViewById(R.id.emaili);
        userprefs = getContext().getSharedPreferences("UserPrefs", 0);
        emaili.getEditText().addTextChangedListener(new C01751());
        buttonRegister = (Button) view.findViewById(R.id.btn_signup);
        buttonRegister.setOnClickListener(new C01762());
        return view;
    }

    private void registerUser() {
        String sfname = fname.getText().toString();
        String slname = lname.getText().toString();
        String sedit_email = edit_email.getText().toString();
        String splotno = plotno.getText().toString();
        String sstreet = street.getText().toString();
        String slandmark = landmark.getText().toString();
        String sstate = state.getText().toString();
        String scity = city.getText().toString();
        String spincode = pincode.getText().toString();
        if (sfname.matches("") || slname.matches("") || splotno.matches("") || sfname.matches("") || sstreet.matches("") || slandmark.matches("") || sstate.matches("") || scity.matches("") || sfname.matches("") || spincode.matches("")) {
            Toast.makeText(getContext(), "Please fill in all fields", 0).show();
        } else if (spincode.length() < 6) {
            Toast.makeText(getContext(), "Please fill 6-digit Pin", 0).show();
        } else {
            register(sfname, slname, sedit_email, splotno, sstreet, slandmark, sstate, scity, spincode, "http://www.prakritifresh.com/cgi-bin/update.py");
        }
    }

    private void register(String fname, String lname, String edit_email, String plotno, String street, String landmark, String state, String city, String pincode, String url) {
        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getContext());
                loading.setMessage(Html.fromHtml("<b><h2>Please Wait...."));
                loading.show();
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
            }

            protected String doInBackground(String... params) {
                Exception e;
                HashMap<String, String> data = new HashMap();
                data.put("firstname", params[0]);
                data.put("lastname", params[1]);
                data.put("email", params[2]);
                data.put("plot", params[4]);
                data.put("street", params[5]);
                data.put("landmark", params[6]);
                data.put("state", params[7]);
                data.put("city", params[8]);
                data.put("pin", params[9]);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(params[10]).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode((String) data.get("firstname"), "UTF-8") + "&" + URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode((String) data.get("lastname"), "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode((String) data.get("email"), "UTF-8") + "&" + URLEncoder.encode("street", "UTF-8") + "=" + URLEncoder.encode((String) data.get("street"), "UTF-8") + "&" + URLEncoder.encode("lm", "UTF-8") + "=" + URLEncoder.encode((String) data.get("landmark"), "UTF-8") + "&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode((String) data.get("state"), "UTF-8") + "&" + URLEncoder.encode("plot", "UTF-8") + "=" + URLEncoder.encode((String) data.get("plot"), "UTF-8") + "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode((String) data.get("city"), "UTF-8") + "&" + URLEncoder.encode("pin", "UTF-8") + "=" + URLEncoder.encode((String) data.get("pin"), "UTF-8") + "&" + URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(userprefs.getString("uid", ""), "UTF-8"));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    BufferedReader bufferedReader2;
                    try {
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
                        bufferedReader2 = bufferedReader;
                    } catch (Exception e2) {
                        e = e2;
                        bufferedReader2 = bufferedReader;
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
                    if (jsonObject.get("status").toString().equals("SUCCESS")) {
                        Toast.makeText(getContext(), "Successfully updated", 0).show();
                        return;
                    }
                    Error error = new Error();
                    Log.d("re", result);
                    error.Error(jsonObject.get("status").toString(), getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{fname, lname, edit_email, plotno, street, landmark, state, city, pincode, url});
    }

    private void getJSON(String url) {
        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getContext());
                loading.setMessage(Html.fromHtml("<b><h2>Retrieving data...."));
                loading.show();
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader;
                Exception e;
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(params[0]).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(userprefs.getString("uid", ""), "UTF-8"));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while (true) {
                        try {
                            String json = bufferedReader2.readLine();
                            if (json != null) {
                                sb.append(json + "\n");
                            } else {
                                result = sb.toString().trim();
                                bufferedReader = bufferedReader2;
                                return sb.toString().trim();
                            }
                        } catch (Exception e2) {
                            e = e2;
                            bufferedReader = bufferedReader2;
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.get("status").toString().equals("SUCCESS")) {
                        fname.setText(jsonObject.getString("firstname"));
                        lname.setText(jsonObject.getString("lastname"));
                        edit_email.setText(jsonObject.getString("email"));
                        plotno.setText(jsonObject.getString("plot"));
                        street.setText(jsonObject.getString("street"));
                        landmark.setText(jsonObject.getString("landmark"));
                        city.setText(jsonObject.getString("city"));
                        pincode.setText(jsonObject.getString("pincode"));
                        state.setText(jsonObject.getString("state"));
                        return;
                    }
                    Error error = new Error();
                    Log.d("result", result);
                    error.Error(jsonObject.get("code").toString(), getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{url});
    }
}
