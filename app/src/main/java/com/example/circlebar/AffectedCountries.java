package com.example.circlebar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountries extends AppCompatActivity {

    EditText editText;
    ListView listView;
//    SimpleArcLoader simpleArcLoader;

    public static List<CountryModel> countryModelList = new ArrayList<>();
    CountryModel countryModel;
    MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);

        editText = findViewById(R.id.editSearch);
        listView = findViewById(R.id.country_listView);
//        simpleArcLoader = findViewById(R.id.loader1);

        //title
        getSupportActionBar().setTitle("Affected Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fetchData();



        //listener //searching
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myCustomAdapter.getFilter().filter(s);
                myCustomAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    //Back Button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/countries";
//        simpleArcLoader.start();

        StringRequest request =new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                String country = jsonObject.getString("country");
                                String cases = jsonObject.getString("cases");
                                String todayCases = jsonObject.getString("todayCases");
                                String deaths = jsonObject.getString("deaths");
                                String todayDeaths = jsonObject.getString("todayDeaths");
                                String recovered = jsonObject.getString("recovered");
                                String active = jsonObject.getString("active");
                                String critical = jsonObject.getString("critical");
                                String casesPerOneMillion = jsonObject.getString("casesPerOneMillion");
                                String deathsPerOneMillion = jsonObject.getString("deathsPerOneMillion");
                                String tests = jsonObject.getString("tests");
                                String testsPerOneMillion = jsonObject.getString("testsPerOneMillion");
                                String population = jsonObject.getString("population");
                                String continent = jsonObject.getString("continent");
                                String activePerOneMillion = jsonObject.getString("activePerOneMillion");
                                String recoveredPerOneMillion = jsonObject.getString("recoveredPerOneMillion");
                                String criticalPerOneMillion = jsonObject.getString("criticalPerOneMillion");

                                JSONObject object = jsonObject.getJSONObject("countryInfo");
                                String flagUrl = object.getString("flag");

                                countryModel = new CountryModel(flagUrl, country, cases, todayCases, deaths, todayDeaths, recovered, active, critical, casesPerOneMillion,
                                                                deathsPerOneMillion, tests, testsPerOneMillion, population, continent, activePerOneMillion, recoveredPerOneMillion,
                                                                criticalPerOneMillion);



                                countryModelList.add(countryModel);

                            }

                            myCustomAdapter = new MyCustomAdapter(AffectedCountries.this, countryModelList);
                            listView.setAdapter(myCustomAdapter);
//                            simpleArcLoader.stop();
//                            simpleArcLoader.setVisibility(View.GONE);


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AffectedCountries.this, "Connection Failed", Toast.LENGTH_SHORT).show();
//                            simpleArcLoader.stop();
//                            simpleArcLoader.setVisibility(View.GONE);
                        }
                        catch (Exception e){
                            Toast.makeText(AffectedCountries.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AffectedCountries.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);



    }
}