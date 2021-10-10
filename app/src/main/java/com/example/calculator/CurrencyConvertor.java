package com.example.calculator;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CurrencyConvertor extends AppCompatActivity {

    private EditText input;
    private TextView result;
    private Button convertBtn;
    private Spinner fromSpinner, toSpinner;
    private String fromCurrency, toCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Currency Converter");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#171515"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        setContentView(R.layout.activity_currecy_convertor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        input = findViewById(R.id.inputText);
        result = findViewById(R.id.resultText);
        convertBtn = findViewById(R.id.convertBtn);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);

        setupListeners();
    }


    void setupListeners() {
        fromSpinner.setOnItemSelectedListener(new FromDropdown());
        toSpinner.setOnItemSelectedListener(new ToDropdown());
        convertBtn.setOnClickListener(v -> {
            if (!isNetworkConnected(CurrencyConvertor.this)) {
                Toast.makeText(CurrencyConvertor.this, "Host unreachable, check your internet connection and try again.", Toast.LENGTH_SHORT).show();
            }
            if (fromCurrency.equals(toCurrency)) {
                Toast.makeText(CurrencyConvertor.this, "from and to values are same.", Toast.LENGTH_SHORT).show();
            } else if (input.getText().toString().isEmpty()) {
                Toast.makeText(CurrencyConvertor.this, "Please enter a value to convert.", Toast.LENGTH_SHORT).show();
            } else {
                JSONFetch obj = new JSONFetch();
                obj.execute(input.getText().toString());
            }
        });
    }


    class FromDropdown implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            fromCurrency = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


    class ToDropdown implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            toCurrency = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


    public boolean isNetworkConnected(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }


    class JSONFetch extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(CurrencyConvertor.this);
        String error = "", apiResponse = "";

        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://frankfurter.app/latest?amount=" + params[0] + "&from=" + fromCurrency + "&to=" + toCurrency).build();
            try {
                Response response = client.newCall(request).execute();
                apiResponse = Objects.requireNonNull(response.body()).string();
            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Converting...");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            if (!error.isEmpty()) {
                showToast("Something went wrong " + error);
            }
            try {
                result.setText(input.getText().toString() + " " + fromCurrency + " " + "=" + " " + new JSONObject(apiResponse).getJSONObject("rates").getString(toCurrency) + " " + toCurrency);
            } catch (Exception e) {
                showToast("Something went wrong " + e.toString());
            }

            super.onPostExecute(v);
        }
    }


    void showToast(String message) {
        Toast.makeText(CurrencyConvertor.this, message, Toast.LENGTH_SHORT).show();
    }
}