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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import com.prakritifresh.GridAdapterVariety.OnDataChangeListener;
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
import org.json.JSONObject;

public class GridFragmentVariety extends Fragment {
    public static final String idPREFERENCES = "idPrefs";
    public static final String userPREFERENCES = "UserPrefs";
    ArrayList<HashMap<String, String>> aList;
    GridAdapterVariety adapter;
    ArrayList<HashMap<String, Bitmap>> bList;
    GridView grid;
    ArrayList<String> imglist;
    String name_item = "";
    String no = "";
    String result;
    Editor sedit;
    SharedPreferences sharedPreferences;
    TextView textView;
    SharedPreferences userprefs;

    class C02571 implements OnDataChangeListener {
        C02571() {
        }

        public void onDataChanged(int size) {
            ((MainActivity) getActivity()).update();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_grid_layout_variety, null);
        textView = (TextView) view.findViewById(R.id.tvar);
        grid = (GridView) view.findViewById(R.id.gridview1);
        sharedPreferences = getContext().getSharedPreferences("idPrefs", 0);
        userprefs = getContext().getSharedPreferences("UserPrefs", 0);
        aList = new ArrayList();
        bList = new ArrayList();
        imglist = new ArrayList();
        getJSON("http://www.prakritifresh.com/cgi-bin/variety.py");
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
                BufferedReader bufferedReader;
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(params[0]).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    name_item = sharedPreferences.getString("iid", "");
                    bufferedWriter.write(URLEncoder.encode("iid", "UTF-8") + "=" + URLEncoder.encode(name_item, "UTF-8") + "&" + URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(userprefs.getString("uid", ""), "UTF-8"));
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
                        HashMap<String, String> name;
                        HashMap<String, Bitmap> imag;
                        if (jsonObject.getString("no").matches("0")) {
                            no = "1";
                            name = new HashMap();
                            imag = new HashMap();
                            name.put("name", jsonObject.getString("name"));
                            if (jsonObject.getString("pricing").matches("pk")) {
                                name.put("price", jsonObject.getString("rate") + "/kg");
                            } else {
                                name.put("price", jsonObject.getString("rate") + "/pc");
                            }
                            name.put("about", jsonObject.getString("about"));
                            name.put("cart", jsonObject.getString("cart"));
                            name.put("wish", jsonObject.getString("wish"));
                            name.put("stock", jsonObject.getString("stock"));
                            imglist.add(0, jsonObject.getString("image"));
                            imag.put("image", BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.logo1_de));
                            aList.add(0, name);
                            bList.add(0, imag);
                            bufferedReader = bufferedReader2;
                            return result;
                        }
                        no = jsonObject.getString("no");
                        JSONArray jsonArray = jsonObject.getJSONArray("arr");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json1 = jsonArray.getJSONObject(i);
                            name = new HashMap();
                            imag = new HashMap();
                            name.put("name", json1.getString("name"));
                            if (json1.getString("pricing").matches("pk")) {
                                name.put("price", json1.getString("rate") + "/kg");
                            } else {
                                name.put("price", json1.getString("rate") + "/kg");
                            }
                            name.put("about", json1.getString("about"));
                            name.put("stock", json1.getString("stock"));
                            name.put("cart", json1.getString("cart"));
                            name.put("wish", json1.getString("wish"));
                            imglist.add(i, json1.getString("image"));
                            imag.put("image", BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.logo1_de));
                            aList.add(i, name);
                            bList.add(i, imag);
                        }
                        bufferedReader = bufferedReader2;
                        return result;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return null;
                }
                return result;
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (no.matches("1")) {
                    textView.setText("There is 1 variety for " + name_item);
                } else {
                    textView.setText("There are " + no + " varieties for " + name_item);
                }
                showJSON();
            }
        }.execute(new String[]{url});
    }

    private void showJSON() {
        adapter = new GridAdapterVariety(getContext(), aList, bList);
        grid.setAdapter(adapter);
        adapter.setOnDataChangeListener(new C02571());
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
