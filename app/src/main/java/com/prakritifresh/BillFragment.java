package com.prakritifresh;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

public class BillFragment extends Fragment {
    public static final String PricePREFERENCES = "PricePrefs";
    public static final String QtyPREFERENCES = "QtyPrefs";
    public static final String amtPREFERENCES = "AmtPrefs";
    public static final String itemPREFERENCES = "ItemPrefs";
    public static final String userPREFERENCES = "UserPrefs";
    public static final String vnamePREFERENCES = "VnamePrefs";
    BillAdapter adapter;
    float amount = 0.0f;
    String amt = "";
    Editor amt_editor;
    SharedPreferences amt_prefs;
    Button button;
    ArrayList<HashMap<String, String>> data = new ArrayList();
    String deli = "20";
    EditText deli_address;
    EditText deli_landmark;
    EditText deli_phone;
    String item = "";
    Editor item_editor;
    SharedPreferences item_prefs;
    ListView listView;
    FragmentManager mFragmentManager;
    Editor price_editor;
    SharedPreferences price_prefs;
    String qty = "";
    Editor qty_editor;
    SharedPreferences qty_prefs;
    String result;
    String resultval = "";
    TextView textView;
    TextView tv;
    SharedPreferences userprefs;
    String vname = "";
    Editor vname_editor;
    SharedPreferences vname_prefs;

    class C01771 implements OnClickListener {
        C01771() {
        }

        public void onClick(View v) {
            updateuser();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_layout, null);
        getmyJSON("http://www.prakritifresh.com/cgi-bin/account.py");
        mFragmentManager = getFragmentManager();
        listView = (ListView) view.findViewById(R.id.listview_bill);
        textView = (TextView) view.findViewById(R.id.totitemsbi);
        tv = (TextView) view.findViewById(R.id.upm);
        deli_address = (EditText) view.findViewById(R.id.address_new);
        deli_landmark = (EditText) view.findViewById(R.id.landmark_new);
        deli_phone = (EditText) view.findViewById(R.id.phone_new);
        button = (Button) view.findViewById(R.id.checkoutbill);
        userprefs = getContext().getSharedPreferences("UserPrefs", 0);
        vname_prefs = getContext().getSharedPreferences("VnamePrefs", 0);
        item_prefs = getContext().getSharedPreferences("ItemPrefs", 0);
        price_prefs = getContext().getSharedPreferences("PricePrefs", 0);
        qty_prefs = getContext().getSharedPreferences("QtyPrefs", 0);
        amt_prefs = getContext().getSharedPreferences("AmtPrefs", 0);
        vname_editor = vname_prefs.edit();
        item_editor = item_prefs.edit();
        price_editor = price_prefs.edit();
        qty_editor = qty_prefs.edit();
        amt_editor = amt_prefs.edit();
        for (Entry<String, String> entry : ((HashMap<String,String>) item_prefs.getAll()).entrySet()) {
            item += item_prefs.getString((String) entry.getKey(), "") + ";";
            amt += amt_prefs.getString((String) entry.getKey(), "") + ";";
            qty += qty_prefs.getString((String) entry.getKey(), "") + ";";
            vname += vname_prefs.getString((String) entry.getKey(), "") + ";";
        }
        button.setOnClickListener(new C01771());
        amt_prefs = getContext().getSharedPreferences("AmtPrefs", 0);
        int size = amt_prefs.getAll().size();
        adapter = new BillAdapter(getContext(), size);
        for (Entry<String, String> entry2 : ((HashMap<String,String>) amt_prefs.getAll()).entrySet()) {
            amount = Float.parseFloat(amt_prefs.getString((String) entry2.getKey(), "10")) + amount;
        }
        listView.setAdapter(adapter);
        if (size != 0) {
            textView.setVisibility(0);
            if (amount >= 70.0f) {
                textView.setText(Html.fromHtml("Total products cost = ₹ " + String.format("%.2f", new Object[]{Float.valueOf(amount)}) + "<br>Delivery charge = ₹ 0 <br>Total amount to be paid = <b>₹ " + String.format("%.2f", new Object[]{Float.valueOf(amount)}) + "</b>"));
                tv.setText(Html.fromHtml("<p>Total amount to be paid = <b>₹ " + String.format("%.2f", new Object[]{Float.valueOf(amount)}) + "</b> by COD"));
                deli = "0";
            } else {
                textView.setText(Html.fromHtml("Total products cost = ₹ " + String.format("%.2f", new Object[]{Float.valueOf(amount)}) + "<br>Delivery charge (₹ 0 for orders above ₹ 70)  = ₹ 20<br> Total amount to be paid = <b>₹ " + String.format("%.2f", new Object[]{Float.valueOf(amount + 20.0f)}) + "</b>"));
                tv.setText(Html.fromHtml("<p>Total amount to be paid = <b>₹ " + String.format("%.2f", new Object[]{Float.valueOf(amount + 20.0f)}) + "</b> by COD"));
                deli = "20";
            }
            button.setVisibility(0);
            listView.setVisibility(0);
        } else {
            tv.setText("Please choose some items first");
            button.setVisibility(4);
            listView.setVisibility(4);
            Toast.makeText(getContext(), "No items", 0).show();
        }
        setListViewHeightBasedOnChildren(listView);
        return view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), 0);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);
                if (i == 0) {
                    view.setLayoutParams(new LayoutParams(desiredWidth, -2));
                }
                view.measure(desiredWidth, 0);
                totalHeight += view.getMeasuredHeight();
            }
            LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
        }
    }

    private void updateuser() {
        String saddress_new = deli_address.getText().toString();
        String slandmark_new = deli_landmark.getText().toString();
        String sphone_new = deli_phone.getText().toString();
        if (saddress_new.matches("") || sphone_new.matches("") || slandmark_new.matches("")) {
            Toast.makeText(getContext(), "Please fill in all fields", 0).show();
        } else if (sphone_new.length() == 10 && (sphone_new.startsWith("9") || sphone_new.startsWith("8") || sphone_new.startsWith("7"))) {
            getJSON(saddress_new, slandmark_new, sphone_new, "http://www.prakritifresh.com/cgi-bin/order_copy.py");
        } else {
            Toast.makeText(getContext(), "Please enter a valid phone number", 0).show();
        }
    }

    private void getJSON(String daddress_new, String dlandmark_new, String dphone_new, String urlorder) {
        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getContext());
                loading.setMessage(Html.fromHtml("<b><h2>Placing order...."));
                loading.show();
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
            }

            protected String doInBackground(String... params) {
                Exception e;
                HashMap<String, String> data = new HashMap();
                data.put("delivery_address", params[0]);
                data.put("delivery_landmark", params[1]);
                data.put("delivery_phone", params[2]);
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(params[3]).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(userprefs.getString("uid", ""), "UTF-8") + "&" + URLEncoder.encode("deliver_address", "UTF-8") + "=" + URLEncoder.encode((String) data.get("delivery_address"), "UTF-8") + "&" + URLEncoder.encode("deliver_landmark", "UTF-8") + "=" + URLEncoder.encode((String) data.get("delivery_landmark"), "UTF-8") + "&" + URLEncoder.encode("deliver_phone", "UTF-8") + "=" + URLEncoder.encode((String) data.get("delivery_phone"), "UTF-8") + "&" + URLEncoder.encode("items", "UTF-8") + "=" + URLEncoder.encode(item, "UTF-8") + "&" + URLEncoder.encode("vnames", "UTF-8") + "=" + URLEncoder.encode(vname, "UTF-8") + "&" + URLEncoder.encode("amt", "UTF-8") + "=" + URLEncoder.encode(amt, "UTF-8") + "&" + URLEncoder.encode("qty", "UTF-8") + "=" + URLEncoder.encode(qty, "UTF-8") + "&" + URLEncoder.encode("tot", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(amount + ((float) Integer.parseInt(deli))), "UTF-8") + "&" + URLEncoder.encode("deli", "UTF-8") + "=" + URLEncoder.encode(deli, "UTF-8"));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while (true) {
                        BufferedReader bufferedReader2;
                        try {
                            String json = bufferedReader.readLine();
                            if (json != null) {
                                sb.append(json + "\n");
                            } else {
                                result = sb.toString().trim();
                                bufferedReader2 = bufferedReader;
                                return sb.toString().trim();
                            }
                        } catch (Exception e2) {
                            e = e2;
                            bufferedReader2 = bufferedReader;
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
                        item_editor.clear().commit();
                        amt_editor.clear().commit();
                        qty_editor.clear().commit();
                        amt_editor.clear().commit();
                        vname_editor.clear().commit();
                        ((MainActivity) getActivity()).update();
                        mFragmentManager.beginTransaction().replace(R.id.containerView, new ThankYouFragment()).commit();
                        return;
                    }
                    new Error().Error(jsonObject.get("code").toString(), getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{daddress_new, dlandmark_new, dphone_new, urlorder});
    }

    private void getmyJSON(String url) {
        new AsyncTask<String, Void, String>() {
            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... params) {
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
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while (true) {
                        BufferedReader bufferedReader2;
                        try {
                            String json = bufferedReader.readLine();
                            if (json != null) {
                                sb.append(json + "\n");
                            } else {
                                result = sb.toString().trim();
                                bufferedReader2 = bufferedReader;
                                return sb.toString().trim();
                            }
                        } catch (Exception e2) {
                            e = e2;
                            bufferedReader2 = bufferedReader;
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
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.get("status").toString().equals("SUCCESS")) {
                        deli_address.setText(Html.fromHtml(jsonObject.getString("plot") + " " + jsonObject.getString("street") + " " + jsonObject.getString("city") + " " + jsonObject.getString("pincode")));
                        deli_landmark.setText(Html.fromHtml(jsonObject.getString("landmark")));
                        deli_phone.setText(Html.fromHtml(jsonObject.getString("phone")));
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
