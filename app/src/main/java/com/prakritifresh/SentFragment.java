package com.prakritifresh;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.prakritifresh.CartAdapter.OnDataChangeListenerc;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SentFragment extends Fragment {
    public static final String PricePREFERENCES = "PricePrefs";
    public static final String QtyPREFERENCES = "QtyPrefs";
    public static final String amtPREFERENCES = "AmtPrefs";
    public static final String idPREFERENCES = "idPrefs";
    public static final String itemPREFERENCES = "ItemPrefs";
    public static final String userPREFERENCES = "UserPrefs";
    public static final String vnamePREFERENCES = "VnamePrefs";
    ArrayList<HashMap<String, String>> aList;
    CartAdapter adapter;
    Editor amt_editor;
    SharedPreferences amt_prefs;
    ArrayList<HashMap<String, Bitmap>> bList;
    Button button;
    ArrayList<String> imglist;
    Editor item_editor;
    SharedPreferences item_prefs;
    ListView listView;
    FragmentManager mFragmentManager;
    String no = "";
    Editor price_editor;
    SharedPreferences price_prefs;
    Editor qty_editor;
    SharedPreferences qty_prefs;
    String result = "";
    SharedPreferences sharedPreferences;
    int size = 0;
    TextView textView;
    SharedPreferences userprefs;
    Editor vname_editor;
    SharedPreferences vname_prefs;

    class C01891 implements OnClickListener {
        C01891() {
        }

        public void onClick(View v) {
            mFragmentManager.beginTransaction().replace(R.id.containerView, new BillFragment()).addToBackStack(null).commit();
        }
    }

    class C02592 implements OnDataChangeListenerc {
        C02592() {
        }

        public void onDataChanged(int size) {
            ((MainActivity) getActivity()).update();
            ((MainActivity) getActivity()).replace();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sent_layout, null);
        mFragmentManager = getFragmentManager();
        userprefs = getContext().getSharedPreferences("UserPrefs", 0);
        listView = (ListView) view.findViewById(R.id.listview_cart);
        textView = (TextView) view.findViewById(R.id.noitems);
        button = (Button) view.findViewById(R.id.checkout);
        aList = new ArrayList();
        bList = new ArrayList();
        imglist = new ArrayList();
        vname_prefs = getContext().getSharedPreferences("VnamePrefs", 0);
        item_prefs = getContext().getSharedPreferences("ItemPrefs", 0);
        amt_prefs = getContext().getSharedPreferences("AmtPrefs", 0);
        qty_prefs = getContext().getSharedPreferences("QtyPrefs", 0);
        price_prefs = getContext().getSharedPreferences("PricePrefs", 0);
        vname_editor = vname_prefs.edit();
        item_editor = item_prefs.edit();
        price_editor = price_prefs.edit();
        qty_editor = qty_prefs.edit();
        amt_editor = amt_prefs.edit();
        vname_editor.clear().commit();
        item_editor.clear().commit();
        price_editor.clear().commit();
        qty_editor.clear().commit();
        amt_editor.clear().commit();
        getJSON("http://www.prakritifresh.com/cgi-bin/cart_display.py");
        button.setOnClickListener(new C01891());
        return view;
    }

    private void getJSON(String url) {
        new AsyncTask<String, Void, String>() {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getContext());
                loading.setMessage(Html.fromHtml("<b><h2>Just a sec...."));
                loading.show();
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
            }

            protected String doInBackground(String... params) {
                Exception e;
                try {
                    BufferedReader bufferedReader;
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
                            if (json == null) {
                                break;
                            }
                            sb.append(json + "\n");
                        } catch (Exception e2) {
                            e = e2;
                            bufferedReader = bufferedReader2;
                        }
                    }
                    result = sb.toString().trim();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("no").matches("0")) {
                            no = "0";
                        } else {
                            no = jsonObject.getString("no");
                            JSONArray jsonArray = jsonObject.getJSONArray("arr");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json1 = jsonArray.getJSONObject(i);
                                HashMap<String, String> name = new HashMap();
                                HashMap<String, Bitmap> imag = new HashMap();
                                name.put("name", json1.getString("name"));
                                name.put("item_id", json1.getString("item_id"));
                                name.put("unit", json1.getString("unit"));
                                if (json1.getString("pricing").matches("pk")) {
                                    name.put("price", "₹" + json1.getString("rate") + "/kg");
                                } else {
                                    name.put("price", "₹" + json1.getString("rate") + "/pc");
                                }
                                name.put("rp", json1.getString("rate"));
                                name.put("pricing", json1.getString("pricing"));
                                imglist.add(i, json1.getString("image"));
                                imag.put("image", BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.logo1_de));
                                aList.add(i, name);
                                bList.add(i, imag);
                            }
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    bufferedReader = bufferedReader2;
                    return result;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.get("status").toString().equals("SUCCESS")) {
                        new Error().Error(jsonObject.get("code").toString(), getContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (no.matches("0")) {
                    textView.setText("No items in cart");
                    Toast.makeText(getContext(), "Cart is empty", 1).show();
                } else {
                    textView.setVisibility(4);
                }
                showJSON();
            }
        }.execute(new String[]{url});
    }

    private void showJSON() {
        adapter = new CartAdapter(getContext(), aList, bList);
        listView.setAdapter(adapter);
        adapter.setOnDataChangeListenerc(new C02592());
        for (int j = 0; j < imglist.size(); j++) {
            getIMG((String) imglist.get(j), j);
        }
    }

    private void getIMG(String url, int j) {
        new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... params) {
                String[] parts = params[0].split("`");
                String uri = parts[0];
                String s = parts[1];
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new URL(uri).openStream());
                    int x = Integer.parseInt(s);
                    HashMap<String, Bitmap> imge = new HashMap();
                    imge.put("image", bitmap);
                    bList.set(x, imge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s;
            }

            protected void onPostExecute(String s) {
                adapter.notifyDataSetChanged();
            }
        }.execute(new String[]{url + "`" + Integer.toString(j)});
    }
}
