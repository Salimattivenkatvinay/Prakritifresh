package com.prakritifresh;

import android.app.ProgressDialog;
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
import com.prakritifresh.GridAdapter.OnDataChangeListener;
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

public class GridFragment extends Fragment {
    ArrayList<HashMap<String, String>> aList;
    GridAdapter adapter;
    ArrayList<HashMap<String, Bitmap>> bList;
    GridView grid;
    ArrayList<String> imglist;
    String result;

    class C02531 implements OnDataChangeListener {
        C02531() {
        }

        public void onDataChanged(int size) {
            ((MainActivity) getActivity()).update();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_grid_layout, null);
        grid = (GridView) view.findViewById(R.id.gridview);
        aList = new ArrayList();
        bList = new ArrayList();
        imglist = new ArrayList();
        getJSON("http://www.prakritifresh.com/cgi-bin/veg.py");
        return view;
    }

    private void getJSON(String url) {
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
                BufferedReader bufferedReader;
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(params[0]).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("rv", "UTF-8"));
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
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json1 = jsonArray.getJSONObject(i);
                            HashMap<String, String> name = new HashMap();
                            HashMap<String, Bitmap> imag = new HashMap();
                            name.put("name", json1.getString("id"));
                            if (json1.getString("pricing").matches("pk")) {
                                name.put("price", json1.getString("rate") + "/kg");
                            } else {
                                name.put("price", json1.getString("rate") + "/pc");
                            }
                            name.put("stock", json1.getString("stock"));
                            imglist.add(i, json1.getString("image"));
                            imag.put("image", BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.logo1_de));
                            aList.add(i, name);
                            bList.add(i, imag);
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    bufferedReader = bufferedReader2;
                    return sb.toString().trim();
                } catch (Exception e4) {
                    e4.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showJSON();
            }
        }.execute(new String[]{url});
    }

    private void showJSON() {
        adapter = new GridAdapter(getContext(), aList, bList);
        grid.setAdapter(adapter);
        adapter.setOnDataChangeListener(new C02531());
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
