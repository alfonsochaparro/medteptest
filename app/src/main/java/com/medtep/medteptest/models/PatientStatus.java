package com.medtep.medteptest.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alfonso on 08/05/2016.
 */
public class PatientStatus {

    private long id;
    private long patient;
    private String status;

    public PatientStatus(long id, long patient, String status) {
        this.id = id;
        this.patient = patient;
        this.status = status;
    }

    public PatientStatus(JSONObject json) throws JSONException {
        this(
                json.has("id") ? json.getLong("id") : 0,
                json.has("patient") ? json.getLong("patient") : 0,
                json.has("status") ? json.getString("status") : "");
    }

    public JSONObject toJs() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("id", id);
        json.put("patient", patient);
        json.put("status", status);

        return json;
    }

    public long getId() { return id; }
    public long getPatient() { return patient; }
    public String getStatus() { return status; }

    public void setPatient(long patient) { this.patient = patient; }
    public void setStatus(String status) { this.status = status; }
}
