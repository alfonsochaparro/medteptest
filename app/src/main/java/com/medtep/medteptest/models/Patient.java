package com.medtep.medteptest.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alfonso on 08/05/2016.
 */
public class Patient {

    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_SURNAME = 2;
    public static final int COL_STATUS = 3;

    private long id;
    private String name;
    private String surname;
    private String status;

    public Patient() {
        id = 0;
        name = "";
        surname = "";
        status = "";
    }

    public Patient(long id, String name, String surname, String status) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.status = status;
    }

    public Patient(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getSurname(), patient.getStatus());
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

    public long getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getStatus() { return status; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setStatus(String status) { this.status = status; }

    public Object getColValue(int col) {
        switch (col) {
            case COL_ID: return id;
            case COL_NAME: return name;
            case COL_SURNAME: return surname;
            case COL_STATUS: return status;
        }

        return null;
    }

}
