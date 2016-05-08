package com.medtep.medteptest.data;

/**
 * Created by Alfonso on 08/05/2016.
 */
public class Constants {

    private static String URL_BASE = "https://demo3417391.mockable.io/";

    private static String URL_PATIENTS = URL_BASE + "patients";
    private static String URL_PATIENT_STATUSES = URL_BASE + "patient_status";

    public static String getUrlPatients() {
        return getUrlPatients(0);
    }

    public static String getUrlPatients(int page) {
        return URL_PATIENTS + (page > 0 ? "?page=" + page : "");
    }

    public static String getUrlPatientStatuses() {
        return URL_PATIENT_STATUSES;
    }
}
