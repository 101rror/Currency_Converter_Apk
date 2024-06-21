package com.error.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/eb7d4acd25652f182f93085c/latest/";
    private HashMap<String, String> countryList;

    private EditText editTextAmount;
    private Spinner spinnerFrom, spinnerTo;
    private TextView textViewResult;
    private Button buttonConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        textViewResult = findViewById(R.id.textViewResult);
        buttonConvert = findViewById(R.id.buttonConvert);

        countryList = getCountryList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(countryList.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExchangeRate();
            }
        });
    }

    private void updateExchangeRate() {
        String amountStr = editTextAmount.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String fromCurrency = spinnerFrom.getSelectedItem().toString();
        String toCurrency = spinnerTo.getSelectedItem().toString();

        String url = BASE_URL + fromCurrency;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject conversionRates = response.getJSONObject("conversion_rates");
                        double rate = conversionRates.getDouble(toCurrency);
                        double finalAmount = amount * rate;
                        textViewResult.setText(String.format("%s %s = %.2f %s", amount, fromCurrency, finalAmount, toCurrency));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(jsonObjectRequest);
    }

    private HashMap<String, String> getCountryList() {
        HashMap<String, String> list = new HashMap<>();
        list.put("USD", "US");
        list.put("BDT", "BD");
        list.put("EUR", "FR");
        list.put("GBP", "GB");
        list.put("INR", "IN");
        list.put("JPY", "JP");
        list.put("CAD", "CA");
        list.put("QAR", "QA");
        list.put("CNY", "CN");
        list.put("BHD", "BH");
        list.put("RUB", "RU");
        list.put("BRL", "BR");
        list.put("ZAR", "ZA");
        list.put("SGD", "SG");
        list.put("TRY", "TR");
        list.put("MYR", "MY");
        list.put("IDR", "ID");
        list.put("SAR", "SA");
        list.put("AED", "AE");
        return list;
    }
}
