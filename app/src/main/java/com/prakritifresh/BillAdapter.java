package com.prakritifresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class BillAdapter extends BaseAdapter {
    public static final String PricePREFERENCES = "PricePrefs";
    public static final String QtyPREFERENCES = "QtyPrefs";
    public static final String amtPREFERENCES = "AmtPrefs";
    public static final String itemPREFERENCES = "ItemPrefs";
    public static final String userPREFERENCES = "UserPrefs";
    public static final String vnamePREFERENCES = "VnamePrefs";
    SharedPreferences amt_prefs;
    Context context;
    ArrayList<HashMap<String, String>> data = new ArrayList<>();
    LayoutInflater inflater;
    SharedPreferences item_prefs;
    String nm;
    SharedPreferences price_prefs;
    SharedPreferences qty_prefs;
    int siz;
    SharedPreferences userprefs;
    View view;
    SharedPreferences vname_prefs;

    public BillAdapter(Context c, int size) {
        context = c;
        siz = size;
        inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        vname_prefs = context.getSharedPreferences("VnamePrefs", 0);
        qty_prefs = context.getSharedPreferences("QtyPrefs", 0);
        price_prefs = context.getSharedPreferences("PricePrefs", 0);
        item_prefs = context.getSharedPreferences("ItemPrefs", 0);
        userprefs = context.getSharedPreferences("UserPrefs", 0);
        amt_prefs = context.getSharedPreferences("AmtPrefs", 0);
        for (Entry<String, String> entry : ((HashMap<String,String>) item_prefs.getAll()).entrySet()) {
            HashMap<String, String> hm = new HashMap<>();
            Log.d("entry", (String) entry.getKey());
            if (vname_prefs.getString((String) entry.getKey(), "").matches(" ")) {
                hm.put("name", item_prefs.getString((String) entry.getKey(), ""));
            } else {
                hm.put("name", vname_prefs.getString((String) entry.getKey(), ""));
            }
            hm.put("qty", qty_prefs.getString((String) entry.getKey(), ""));
            hm.put("price", price_prefs.getString((String) entry.getKey(), ""));
            hm.put("amt", amt_prefs.getString((String) entry.getKey(), ""));
            data.add(hm);
        }
    }

    public int getCount() {
        return siz;
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
            view = inflater.inflate(R.layout.listview_bill_layout, null);
        }
        HashMap<String, String> dat;
        dat = (HashMap<String, String>) data.get(position);
        TextView billnm = (TextView) view.findViewById(R.id.billnm);
        TextView billam = (TextView) view.findViewById(R.id.billam);
        TextView billpr = (TextView) view.findViewById(R.id.billpr);
        TextView billqt = (TextView) view.findViewById(R.id.billqt);
        ((TextView) view.findViewById(R.id.billsl)).setText(Integer.toString(position + 1));
        billnm.setText((CharSequence) dat.get("name"));
        billpr.setText((CharSequence) dat.get("price"));
        billqt.setText((CharSequence) dat.get("qty"));
        billam.setText("₹" + ((String) dat.get("amt")));
        return view;
    }
}
