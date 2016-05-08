package com.medtep.medteptest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.medtep.medteptest.data.Constants;
import com.medtep.medteptest.data.DataManager;
import com.medtep.medteptest.models.Patient;
import com.medtep.medteptest.models.PatientStatus;
import com.medtep.network.NetworkRequest;
import com.medtep.utils.Alerts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mStatusView;

    private List<Patient> mPatients;
    private List<PatientStatus> mPatientStatuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        initViews();
        initData();
    }

    //region Inits

    private void initViews() {
        mStatusView = (TextView) findViewById(R.id.txtStatus);
    }

    private void initData() {
        mPatients = new ArrayList<>();
        mPatientStatuses = new ArrayList<>();

        loadPatientStatuses();
    }

    //endregion

    //region Data loads

    private void loadPatientStatuses() {
        mStatusView.setText(R.string.loading_statuses);

        new NetworkRequest.Builder(this)
                .setMethod(Request.Method.GET)
                .setUrl(Constants.getUrlPatientStatuses())
                .setListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("main", "Response: " + response);

                        if(!parsePatientStatuses(response)) {
                            Alerts.showSimpleError(MainActivity.this,
                                    getString(R.string.error_patient_statuses));
                        }
                        else {
                            loadPatients(1);
                        }
                    }
                })
                .setErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("main", "Error: " + error.getMessage());

                        Alerts.showSimpleError(MainActivity.this, getString(R.string.error_network),
                                getString(R.string.btn_retry), new Runnable() {
                                    @Override
                                    public void run() {
                                        loadPatientStatuses();
                                    }
                                });
                    }
                })
                .build();
    }

    private void loadPatients(final int page) {
        mStatusView.setText(R.string.loading_patients);

        new NetworkRequest.Builder(this)
                .setMethod(Request.Method.GET)
                .setUrl(Constants.getUrlPatients(page))
                .setListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("main", "Response: " + response);

                        int nextPage = parsePatients(response);
                        if(nextPage == -1) {
                            Alerts.showSimpleError(MainActivity.this,
                                    getString(R.string.error_patients));
                        }
                        else if (nextPage != 0){
                            loadPatients(nextPage);
                        }
                        else {
                            storeDataOnDataManager();
                            goForward();
                        }
                    }
                })
                .setErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("main", "Error: " + error.getMessage());

                        Alerts.showSimpleError(MainActivity.this, getString(R.string.error_network),
                                getString(R.string.btn_retry), new Runnable() {
                                    @Override
                                    public void run() {
                                        loadPatientStatuses();
                                    }
                                });
                    }
                })
                .build();
    }

    //endregion

    //region Data parsing

    private boolean parsePatientStatuses(String data) {
        try {
            mPatientStatuses.clear();

            JSONObject json = new JSONObject(data);
            if(json.has("results")) {
                JSONArray array = json.getJSONArray("results");
                for(int i = 0; i < array.length(); i++) {
                    mPatientStatuses.add(new PatientStatus(array.getJSONObject(i)));
                }
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return  if parsing error happened: -1
     *          if there are not more pages: 0
     *          otherwise: newxt page to load
     */
    private int parsePatients(String data) {
        try {
            List<Patient> patients = new ArrayList<>();

            JSONObject json = new JSONObject(data);
            if(json.has("results")) {
                JSONArray array = json.getJSONArray("results");
                for(int i = 0; i < array.length(); i++) {
                    patients.add(new Patient(array.getJSONObject(i)));
                }
                mPatients.addAll(patients);

                return json.isNull("next_page") ? 0 : json.getInt("next_page");
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    //endregion



    private void storeDataOnDataManager() {
        DataManager.getInstance().initData(mPatients, mPatientStatuses);
    }

    private void goForward() {
        startActivity(new Intent(MainActivity.this, ListActivity.class));
        finish();
    }

}
