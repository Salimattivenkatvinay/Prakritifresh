package com.prakritifresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import org.json.JSONException;
import org.json.JSONObject;

public class GridAdapterVariety extends BaseAdapter {
    public static final String idPREFERENCES = "idPrefs";
    public static boolean st1 = false;
    public static boolean st2 = false;
    public static final String userPREFERENCES = "UserPrefs";
    ImageButton about1;
    Context context;
    ArrayList<HashMap<String, String>> data = new ArrayList();
    ArrayList<HashMap<String, Bitmap>> imag = new ArrayList();
    HashMap<String, Bitmap> img;
    LayoutInflater inflater = null;
    OnDataChangeListener mOnDataChangeListener;
    HashMap<String, String> prod;
    String result = "";
    SharedPreferences sharedPreferences;
    String st = " ";
    SharedPreferences userprefs;
    View f5v;

    public interface OnDataChangeListener {
        void onDataChanged(int i);
    }

    public GridAdapterVariety(Context a, ArrayList<HashMap<String, String>> d, ArrayList<HashMap<String, Bitmap>> im) {
        context = a;
        data = d;
        imag = im;
        st1 = false;
        st2 = false;
        inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        f5v = convertView;
        if (convertView == null) {
            f5v = inflater.inflate(R.layout.gridview_layout_variety, null);
        }
        ImageView imageView = (ImageView) f5v.findViewById(R.id.imgrid1);
        ImageView imageView1 = (ImageView) f5v.findViewById(R.id.imout1);
        TextView textView = (TextView) f5v.findViewById(R.id.vegname1);
        TextView textView1 = (TextView) f5v.findViewById(R.id.prc1);
        final ImageButton fav1 = (ImageButton) f5v.findViewById(R.id.fav1);
        final ImageButton imageButton = (ImageButton) f5v.findViewById(R.id.add_to_cart1);
        about1 = (ImageButton) f5v.findViewById(R.id.about1);
        sharedPreferences = context.getSharedPreferences("idPrefs", 0);
        userprefs = context.getSharedPreferences("UserPrefs", 0);
        prod = new HashMap();
        prod = (HashMap) data.get(position);
        img = new HashMap();
        img = (HashMap) imag.get(position);
        if (data.size() != 1) {
            st = (String) prod.get("name");
        }
        textView.setText((CharSequence) prod.get("name"));
        textView1.setText("₹" + ((String) prod.get("price")));
        if (((String) prod.get("cart")).equals("1")) {
            imageButton.setImageResource(R.mipmap.vegetable_page);
            mOnDataChangeListener.onDataChanged(0);
            st2 = true;
        }
        if (((String) prod.get("wish")).equals("1") && !st1) {
            fav1.setImageResource(R.mipmap.favourite);
            st1 = true;
        }
        imageView.setImageBitmap((Bitmap) img.get("image"));
        fav1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (st1) {
                    getdata("http://www.prakritifresh.com/cgi-bin/remove_wishlist.py", st);
                    fav1.setImageResource(R.mipmap.favourite_not);
                    st1 = false;
                    return;
                }
                getdata("http://www.prakritifresh.com/cgi-bin/wishlist.py", st);
                fav1.setImageResource(R.mipmap.favourite);
                st1 = true;
            }
        });
        if (Float.parseFloat((String) prod.get("stock")) > 0.0f) {
            imageView1.setVisibility(4);
            imageButton.setVisibility(0);
            imageButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (st2) {
                        Toast.makeText(context, "Item already in cart", 0).show();
                        return;
                    }
                    getdata("http://www.prakritifresh.com/cgi-bin/addcart.py", st);
                    imageButton.setImageResource(R.mipmap.vegetable_page);
                    mOnDataChangeListener.onDataChanged(0);
                    st2 = true;
                }
            });
        } else {
            imageView1.setVisibility(0);
            imageButton.setVisibility(4);
        }
        return f5v;
    }

    private void getdata(String url, String vname) {
        new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... params) {
                Exception e;
                BufferedReader bufferedReader;
                String uri = params[0];
                String name = params[1];
                try {
                    HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("iid", "UTF-8") + "=" + URLEncoder.encode(sharedPreferences.getString("iid", ""), "UTF-8") + "&" + URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(userprefs.getString("uid", ""), "UTF-8") + "&" + URLEncoder.encode("vname", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8"));
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
                    bufferedReader = bufferedReader2;
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                    return result;
                }
                return result;
            }

            protected void onPostExecute(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.get("status").toString().equals("SUCCESS")) {
                        new Error().Error(jsonObject.get("code").toString(), context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{url, vname});
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }
}
