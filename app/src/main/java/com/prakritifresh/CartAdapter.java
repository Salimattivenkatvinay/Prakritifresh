package com.prakritifresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class CartAdapter extends BaseAdapter {
    public static final String PricePREFERENCES = "PricePrefs";
    public static final String QtyPREFERENCES = "QtyPrefs";
    public static final String amtPREFERENCES = "AmtPrefs";
    public static final String idPREFERENCES = "idPrefs";
    public static final String itemPREFERENCES = "ItemPrefs";
    public static final String userPREFERENCES = "UserPrefs";
    public static final String vnamePREFERENCES = "VnamePrefs";
    float aFloat;
    ImageButton addi;
    Editor amt_editor;
    SharedPreferences amt_prefs;
    Context context;
    ArrayList<HashMap<String, String>> data = new ArrayList();
    ImageButton imageButton;
    ImageView imageView;
    ArrayList<HashMap<String, Bitmap>> img = new ArrayList();
    LayoutInflater inflater;
    Editor item_editor;
    SharedPreferences item_prefs;
    OnDataChangeListenerc mOnDataChangeListener;
    TextView nam;
    TextView pr;
    Editor price_editor;
    SharedPreferences price_prefs;
    Editor qty_editor;
    SharedPreferences qty_prefs;
    ImageButton remove;
    String result;
    SharedPreferences sharedPreferences;
    String st;
    TextView un;
    SharedPreferences userprefs;
    View view;
    Editor vname_editor;
    SharedPreferences vname_prefs;

    public interface OnDataChangeListenerc {
        void onDataChanged(int i);
    }

    public CartAdapter(Context c, ArrayList<HashMap<String, String>> d, ArrayList<HashMap<String, Bitmap>> im) {
        context = c;
        inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        data = d;
        img = im;
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
        view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.listview_social_layout, null);
        }
        sharedPreferences = context.getSharedPreferences("idPrefs", 0);
        userprefs = context.getSharedPreferences("UserPrefs", 0);
        vname_prefs = context.getSharedPreferences("VnamePrefs", 0);
        item_prefs = context.getSharedPreferences("ItemPrefs", 0);
        price_prefs = context.getSharedPreferences("PricePrefs", 0);
        qty_prefs = context.getSharedPreferences("QtyPrefs", 0);
        amt_prefs = context.getSharedPreferences("AmtPrefs", 0);
        vname_editor = vname_prefs.edit();
        item_editor = item_prefs.edit();
        price_editor = price_prefs.edit();
        qty_editor = qty_prefs.edit();
        amt_editor = amt_prefs.edit();
        HashMap<String, String> dat = new HashMap();
        dat = (HashMap) data.get(position);
        HashMap<String, Bitmap> ig = new HashMap();
        ig = (HashMap) img.get(position);
        imageView = (ImageView) view.findViewById(R.id.flag1);
        un = (TextView) view.findViewById(R.id.unit1);
        final TextView txtqty1 = (TextView) view.findViewById(R.id.txtqt1);
        nam = (TextView) view.findViewById(R.id.namecart);
        pr = (TextView) view.findViewById(R.id.pricecart);
        remove = (ImageButton) view.findViewById(R.id.removeit);
        addi = (ImageButton) view.findViewById(R.id.addit);
        imageButton = (ImageButton) view.findViewById(R.id.cart1);
        final TextView qty = (TextView) view.findViewById(R.id.nfunit);
        String nm = (String) dat.get("name");
        vname_editor.putString(Integer.toString(position + 1), nm).commit();
        item_editor.putString(Integer.toString(position + 1), (String) dat.get("item_id")).commit();
        price_editor.putString(Integer.toString(position + 1), (String) dat.get("price")).commit();
        if (nm.matches(" ")) {
            nam.setText((CharSequence) dat.get("item_id"));
            st = " ";
        } else {
            nam.setText(nm);
            st = nm;
        }
        pr.setText((CharSequence) dat.get("price"));
        imageView.setImageBitmap((Bitmap) ig.get("image"));
        imageView.setBackgroundColor(0);
        if (qty_prefs.getString(Integer.toString(position + 1), " ").matches(" ")) {
            if (((String) dat.get("pricing")).matches("pp")) {
                un.setText(String.format(Locale.US, "%.0f", new Object[]{Float.valueOf(Float.parseFloat((String) dat.get("unit")))}) + " pc");
                txtqty1.setText(String.format(Locale.US, "%.0f", new Object[]{Float.valueOf(Float.parseFloat((String) dat.get("unit")))}) + " pc");
                qty_editor.putString(Integer.toString(position + 1), ((String) dat.get("unit")) + " pc").commit();
                amt_editor.putString(Integer.toString(position + 1), (String) dat.get("rp")).commit();
            } else {
                aFloat = Float.parseFloat(((String) dat.get("unit")).toString());
                if (((double) aFloat) < 1.0d) {
                    un.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(aFloat * 1000.0f)}) + " gm");
                    txtqty1.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(aFloat * 1000.0f)}) + " gm");
                    qty_editor.putString(Integer.toString(position + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(aFloat * 1000.0f)}) + " gm").commit();
                    amt_editor.putString(Integer.toString(position + 1), String.valueOf(Float.parseFloat((String) dat.get("rp")) * aFloat)).commit();
                } else {
                    un.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(aFloat)}) + " kg");
                    txtqty1.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(aFloat)}) + " kg");
                    qty_editor.putString(Integer.toString(position + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(aFloat)}) + " kg").commit();
                    amt_editor.putString(Integer.toString(position + 1), String.valueOf(Float.parseFloat((String) dat.get("rp")) * aFloat)).commit();
                }
            }
        }
        final int i = position;
        remove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int i = Integer.parseInt(qty.getText().toString());
                Log.d("iski maa ka ", "value " + i);
                if (i > 1) {
                    i--;
                }
                HashMap<String, String> datf = (HashMap) data.get(i);
                if (((String) datf.get("pricing")).matches("pk")) {
                    float f = Float.parseFloat(((String) datf.get("unit")).toString()) * ((float) i);
                    if (((double) f) < 1.0d) {
                        txtqty1.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f * 1000.0f)}) + " gm");
                        qty_editor.putString(Integer.toString(i + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f * 1000.0f)}) + " gm").commit();
                        amt_editor.putString(Integer.toString(i + 1), String.valueOf(Float.parseFloat((String) datf.get("rp")) * f)).commit();
                    } else {
                        txtqty1.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f)}) + " kg");
                        qty_editor.putString(Integer.toString(i + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f * 1000.0f)}) + " gm").commit();
                        amt_editor.putString(Integer.toString(i + 1), String.valueOf(Float.parseFloat((String) datf.get("rp")) * f)).commit();
                    }
                } else {
                    txtqty1.setText(Integer.toString(i) + " pc");
                    qty_editor.putString(Integer.toString(i + 1), Integer.toString(i) + " pc").commit();
                    amt_editor.putString(Integer.toString(i + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(Float.parseFloat((String) datf.get("rp")) * ((float) i))})).commit();
                }
                qty.setText(Integer.toString(i));
            }
        });
        addi.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int i = Integer.parseInt(qty.getText().toString()) + 1;
                HashMap<String, String> datf = (HashMap) data.get(i);
                if (((String) datf.get("pricing")).matches("pk")) {
                    float f = Float.parseFloat(((String) datf.get("unit")).toString()) * ((float) i);
                    if (((double) f) < 1.0d) {
                        txtqty1.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f * 1000.0f)}) + " gm");
                        qty_editor.putString(Integer.toString(i + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f * 1000.0f)}) + " gm").commit();
                        amt_editor.putString(Integer.toString(i + 1), String.valueOf(Float.parseFloat((String) datf.get("rp")) * f)).commit();
                        Log.d("my quantity", qty_prefs.getString(Integer.toString(i + 1), ""));
                        Log.d("my amount", amt_prefs.getString(Integer.toString(i + 1), ""));
                    } else {
                        txtqty1.setText(String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f)}) + " kg");
                        qty_editor.putString(Integer.toString(i + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(f * 1000.0f)}) + " gm").commit();
                        amt_editor.putString(Integer.toString(i + 1), String.valueOf(Float.parseFloat((String) datf.get("rp")) * f)).commit();
                        Log.d("my quantity", qty_prefs.getString(Integer.toString(i + 1), ""));
                        Log.d("my amount", amt_prefs.getString(Integer.toString(i + 1), ""));
                    }
                } else {
                    txtqty1.setText(Integer.toString(i) + " pc");
                    qty_editor.putString(Integer.toString(i + 1), Integer.toString(i) + " pc").commit();
                    amt_editor.putString(Integer.toString(i + 1), String.format(Locale.US, "%.2f", new Object[]{Float.valueOf(Float.parseFloat((String) datf.get("rp")) * ((float) i))})).commit();
                    Log.d("my quantity", qty_prefs.getString(Integer.toString(i + 1), ""));
                    Log.d("my amount", amt_prefs.getString(Integer.toString(i + 1), ""));
                }
                qty.setText(Integer.toString(i));
            }
        });
        imageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getdata("http://www.prakritifresh.com/cgi-bin/remove_cart.py", st, (String) ((HashMap) data.get(i)).get("item_id"));
                mOnDataChangeListener.onDataChanged(0);
                data.remove(i);
                img.remove(i);
                vname_editor.remove(Integer.toString(i + 1)).commit();
                item_editor.remove(Integer.toString(i + 1)).commit();
                price_editor.remove(Integer.toString(i + 1)).commit();
                qty_editor.remove(Integer.toString(i + 1)).commit();
                amt_editor.remove(Integer.toString(i + 1)).commit();
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private void getdata(String url, String vname, String iid) {
        new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... params) {
                Exception e;
                String uri = params[0];
                String name = params[1];
                String item = params[2];
                try {
                    BufferedReader bufferedReader;
                    HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(URLEncoder.encode("iid", "UTF-8") + "=" + URLEncoder.encode(item, "UTF-8") + "&" + URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(userprefs.getString("uid", ""), "UTF-8") + "&" + URLEncoder.encode("vname", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8"));
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
        }.execute(new String[]{url, vname, iid});
    }

    public void setOnDataChangeListenerc(OnDataChangeListenerc onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }
}
