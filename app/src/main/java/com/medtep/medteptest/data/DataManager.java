package com.medtep.medteptest.data;

import com.medtep.medteptest.models.Patient;
import com.medtep.medteptest.models.PatientStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alfonso on 08/05/2016.
 */

/**
 *  Singleton class which manage patients data
 */
public class DataManager {

    public static final int SORT_ASC = 0;
    public static final int SORT_DES = 1;

    private static DataManager sInstance;
    private List<Patient> mPatients;
    private List<String> mStatusTypes;

    //region Init methods

    public static DataManager getInstance() {
        if(sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
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

    private DataManager() {
        mPatients = new ArrayList<>();
        mStatusTypes = new ArrayList<>();
    }

    //endregion

    //region Private util methods

    private void setStatusTypes(List<PatientStatus> statuses) {
        for(PatientStatus status: statuses) {
            if(!mStatusTypes.contains(status.getStatus())) {
                mStatusTypes.add(status.getStatus());
            }
        }
    }

    private long getMaxId() {
        long id = 0;
        for(Patient patient: mPatients) {
            if(patient.getId() > id) {
                id = patient.getId();
            }
        }
        return id;
    }

    //endregion

    //region Patients management methods

    public List<Patient> getPatients() {
        return mPatients;
    }

    public List<Patient> getPatients(final int searchCol, String searchQuery, final int sortCol, int sortMode) {
        List<Patient> patients = new ArrayList<>();
        String query = searchQuery.toLowerCase();

        for(Patient patient: mPatients) {
            Object searchData = patient.getColValue(searchCol);
            if(searchData != null) {
                if (searchData.toString().toLowerCase().contains(query)) {
                    patients.add(patient);
                }
            }
        }

        final boolean asc = sortMode == SORT_ASC;
        Collections.sort(patients, new Comparator<Patient>() {
            @Override
            public int compare(Patient p1, Patient p2) {
                Object val1 = p1.getColValue(sortCol);
                Object val2 = p2.getColValue(sortCol);

                if(val1 instanceof Long) {
                    return (int)(asc ? (long)val1 - (long)val2 : (long)val2 - (long)val1);
                }
                return asc ? val1.toString().toLowerCase().compareTo(val2.toString().toLowerCase())
                        : val2.toString().toLowerCase().compareTo(val1.toString().toLowerCase());
            }
        });

        return patients;
    }

    public Patient getPatientById(long id) {
        for(Patient patient: mPatients) {
            if(patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public List<String> getStatusTypes() {
        return mStatusTypes;
    }

    public void savePatient(Patient patient) {
        if(patient.getId() == 0) {
            patient.setId(getMaxId() + 1);
            mPatients.add(patient);
        }
        else {
            Patient editedPatient = getPatientById(patient.getId());
            editedPatient.setName(patient.getName());
            editedPatient.setSurname(patient.getSurname());
            editedPatient.setStatus(patient.getStatus());
        }
    }

    public void deletePatient(Patient patient) {
        mPatients.remove(getPatientById(patient.getId()));
    }

    //endregion
}
