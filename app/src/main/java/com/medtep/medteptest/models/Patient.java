package com.medtep.medteptest.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alfonso on 08/05/2016.
 */
public class Patient {

    public long id;
    public String name;
    public String surname;
    public String status;

    public Patient(long id, String name, String surname, String status) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.status = status;
    }

    public Patient(JSONObject json) throws JSONException {
        this(
                json.has("id") ? json.getLong("id") : 0,
                json.has("name") ? json.getString("name") : "",
                json.has("surname") ? json.getString("surname") : "",
                json.has("status") ? json.getString("status") : "");
    }

    public JSONObject toJs() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("id", id);
        json.put("name", name);
        json.put("surname", surname);
        json.put("status", status);

        return json;
    }
}
