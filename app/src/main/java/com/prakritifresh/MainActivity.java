package com.prakritifresh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String PricePREFERENCES = "PricePrefs";
    public static final String QtyPREFERENCES = "QtyPrefs";
    public static final String amtPREFERENCES = "AmtPrefs";
    public static final String itemPREFERENCES = "ItemPrefs";
    public static final String userPREFERENCES = "UserPrefs";
    public static final String vnamePREFERENCES = "VnamePrefs";
    Editor amt_editor;
    SharedPreferences amt_prefs;
    int f12i = 0;
    Editor item_editor;
    SharedPreferences item_prefs;
    DrawerLayout mDrawerLayout;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    NavigationView mNavigationView;
    String no;
    Editor price_editor;
    SharedPreferences price_prefs;
    Editor qty_editor;
    SharedPreferences qty_prefs;
    String result;
    Editor shared_edit;
    TextView textView;
    SharedPreferences userPreferences;
    Editor vname_editor;
    SharedPreferences vname_prefs;

    class C01862 implements OnClickListener {
        C01862() {
        }

        public void onClick(View v) {
            mFragmentManager.beginTransaction().replace(R.id.containerView, new SentFragment()).commit();
            f12i = 1;
        }
    }

    class C01873 implements OnClickListener {
        C01873() {
        }

        public void onClick(View v) {
            mFragmentManager.beginTransaction().replace(R.id.containerView, new AccountFragment()).commit();
            f12i = 1;
        }
    }

    class C02581 implements OnNavigationItemSelectedListener {
        C02581() {
        }

        public boolean onNavigationItemSelected(MenuItem menuItem) {
            mDrawerLayout.closeDrawers();
            if (menuItem.getItemId() == R.id.nav_item_sent) {
                mFragmentManager.beginTransaction().replace(R.id.containerView, new SentFragment()).commit();
                f12i = 1;
            }
            if (menuItem.getItemId() == R.id.nav_item_inbox) {
                mFragmentManager.beginTransaction().replace(R.id.containerView, new TabFragment()).commit();
                f12i = 0;
            }
            if (menuItem.getItemId() == R.id.nav_logout) {
                shared_edit.clear();
                shared_edit.commit();
                vname_editor.clear().commit();
                item_editor.clear().commit();
                price_editor.clear().commit();
                qty_editor.clear().commit();
                amt_editor.clear().commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                f12i = 0;
            }
            return false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        getJSON("http://www.prakritifresh.com/cgi-bin/nocart.py");
        userPreferences = getSharedPreferences("UserPrefs", 0);
        shared_edit = userPreferences.edit();
        vname_prefs = getSharedPreferences("VnamePrefs", 0);
        item_prefs = getSharedPreferences("ItemPrefs", 0);
        price_prefs = getSharedPreferences("PricePrefs", 0);
        qty_prefs = getSharedPreferences("QtyPrefs", 0);
        amt_prefs = getSharedPreferences("AmtPrefs", 0);
        vname_editor = vname_prefs.edit();
        item_editor = item_prefs.edit();
        price_editor = price_prefs.edit();
        qty_editor = qty_prefs.edit();
        amt_editor = amt_prefs.edit();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
        mNavigationView.setNavigationItemSelectedListener(new C02581());
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, (Toolbar) findViewById(R.id.toolbar), R.string.app_name, R.string.app_name);
        textView = (TextView) findViewById(R.id.textOne);
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.profile);
        ((ImageButton) findViewById(R.id.cart_item)).setOnClickListener(new C01862());
        imageButton1.setOnClickListener(new C01873());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public void onBackPressed() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        if (mDrawerLayout.isDrawerOpen(8388611)) {
            mDrawerLayout.closeDrawer(8388611);
        } else if (f12i == 1) {
            mFragmentManager.beginTransaction().replace(R.id.containerView, new TabFragment()).commit();
        } else {
            fragmentManager.popBackStack();
            if (fragmentManager.getBackStackEntryCount() == 0) {
                super.onBackPressed();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void replace() {
        mFragmentManager.beginTransaction().replace(R.id.containerView, new SentFragment()).commit();
    }

    public void update() {
        getJSON("http://www.prakritifresh.com/cgi-bin/nocart.py");
    }

    private void getJSON(String url) {
        new AsyncTask<String, Void, String>() {
            protected void onPreExecute() {
                super.onPreExecute();
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
                    bufferedWriter.write(URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(userPreferences.getString("uid", ""), "UTF-8"));
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
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.get("status").toString().equals("SUCCESS")) {
                        showJson(jsonObject.getString("no"));
                    } else {
                        new Error().Error(jsonObject.get("code").toString(), MainActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new String[]{url});
    }

    private void showJson(String no) {
        textView = (TextView) findViewById(R.id.textOne);
        if (Integer.parseInt(no) == 0) {
            textView.setVisibility(4);
        } else {
            textView.setVisibility(0);
        }
        textView.setText(no);
    }
}
