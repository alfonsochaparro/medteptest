package com.medtep.medteptest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.medtep.medteptest.data.DataManager;
import com.medtep.medteptest.models.Patient;

import org.json.JSONObject;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TextView mIdView;
    private EditText mNameView;
    private EditText mSurnameView;
    private Spinner mStatusView;

    private Patient mPatient;
    private List<String> mStatusTypes;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();
        initData(getIntent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(mPatient.getId() == 0 ?
                R.menu.details_new : R.menu.details_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save) {
            saveData();
        }
        else if(item.getItemId() == R.id.delete) {
            deleteDataDialog();
        }
        else finish();
        return true;
    }

    //region Inits

    private void initViews() {
        mIdView = (TextView) findViewById(R.id.txtId);
        mNameView = (EditText) findViewById(R.id.txtName);
        mSurnameView = (EditText) findViewById(R.id.txtSurname);
        mStatusView = (Spinner) findViewById(R.id.spnStatus);
    }

    private void initData(Intent intent) {
        mDataManager = DataManager.getInstance();

        mStatusTypes = mDataManager.getStatusTypes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                mStatusTypes.toArray(new String[mStatusTypes.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mStatusView.setAdapter(adapter);

        String data = intent.getStringExtra("patient");
        if(data != null) {
            getSupportActionBar().setTitle(getString(R.string.details_edit));
            try {
                mPatient = new Patient(new JSONObject(data));
                showData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            getSupportActionBar().setTitle(getString(R.string.details_new));
            mPatient = new Patient();
        }
    }

    //endregion

    //region Actions

    private void showData() {
        mIdView.setText("Id: " + mPatient.getId());
        mNameView.setText(mPatient.getName());
        mSurnameView.setText(mPatient.getSurname());

        for(int i = 0; i < mStatusTypes.size(); i++) {
            if(mStatusTypes.get(i).compareTo(mPatient.getStatus()) == 0) {
                mStatusView.setSelection(i);
                break;
            }
        }

    }

    private void saveData() {
        mPatient.setName(mNameView.getText().toString().trim());
        mPatient.setSurname(mSurnameView.getText().toString().trim());
        mPatient.setStatus(mStatusView.getSelectedItem().toString());

        mDataManager.savePatient(mPatient);
        exitWithChanges();
    }

    private void deleteDataDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_patient_confirm)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteData();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void deleteData() {
        mDataManager.deletePatient(mPatient);
        exitWithChanges();
    }

    private void exitWithChanges() {
        setResult(RESULT_OK);
        finish();
    }

    //endregion

}
