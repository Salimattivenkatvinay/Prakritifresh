package com.prakritifresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class GridAdapter extends BaseAdapter {
    public static final String idPREFERENCES = "idPrefs";
    Context context;
    ArrayList<HashMap<String, String>> data = new ArrayList();
    ArrayList<HashMap<String, Bitmap>> imag = new ArrayList();
    HashMap<String, Bitmap> img;
    LayoutInflater inflater = null;
    OnDataChangeListener mOnDataChangeListener;
    HashMap<String, String> f4p;
    HashMap<String, String> prod;
    Editor sedit;
    SharedPreferences sharedPreferences;
    String st;

    public interface OnDataChangeListener {
        void onDataChanged(int i);
    }

    public GridAdapter(Context a, ArrayList<HashMap<String, String>> d, ArrayList<HashMap<String, Bitmap>> im) {
        context = a;
        data = d;
        imag = im;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            v = inflater.inflate(R.layout.gridview_layout, null);
        }
        ImageView imageView = (ImageView) v.findViewById(R.id.imgrid);
        ImageView imageView1 = (ImageView) v.findViewById(R.id.imout);
        TextView textView = (TextView) v.findViewById(R.id.vegname);
        Button imageButton = (Button) v.findViewById(R.id.add_cart);
        prod = new HashMap();
        prod = (HashMap) data.get(position);
        img = new HashMap();
        img = (HashMap) imag.get(position);
        st = (String) prod.get("name");
        textView.setText(st);
        imageView.setImageBitmap((Bitmap) img.get("image"));
        if (Float.parseFloat((String) prod.get("stock")) > 0.0f) {
            imageView1.setVisibility(4);
            imageButton.setVisibility(0);
            imageButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    f4p = new HashMap();
                    f4p = (HashMap) data.get(position);
                    sharedPreferences = context.getSharedPreferences("idPrefs", 0);
                    sedit = sharedPreferences.edit();
                    sedit.putString("iid", (String) f4p.get("name"));
                    Log.d("iid", (String) f4p.get("name"));
                    sedit.commit();
                   // context.getSupportFragmentManager().beginTransaction().replace(R.id.containerView, new GridFragmentVariety()).addToBackStack(null).commit();
                }
            });
        } else {
            imageView1.setVisibility(0);
            imageButton.setVisibility(4);
        }
        return v;
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }
}
