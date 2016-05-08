package com.medtep.medteptest.data;

import com.medtep.medteptest.models.Patient;
import com.medtep.medteptest.models.PatientStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alfonso on 08/05/2016.
 */
public class DataManager {

    private static DataManager sInstance;
    private List<Patient> mPatients;
    private List<String> mStatusTypes;


    public static DataManager getInstance() {
        if(sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    private DataManager() {
        mPatients = new ArrayList<>();
        mStatusTypes = new ArrayList<>();
    }

    private void setStatusTypes(List<PatientStatus> statuses) {
        for(PatientStatus status: statuses) {
            if(!mStatusTypes.contains(status.getStatus())) {
                mStatusTypes.add(status.getStatus());
            }
        }
    }

    public void initData(List<Patient> patients, List<PatientStatus> statuses) {
        setStatusTypes(statuses);

        for(Patient item: patients) {
            Patient patient = new Patient(item);

            for(PatientStatus status: statuses) {
                if(patient.getId() == status.getPatient()) {
                    patient.setStatus(status.getStatus());
                    break;
                }
            }

            mPatients.add(patient);
        }
    }
}
