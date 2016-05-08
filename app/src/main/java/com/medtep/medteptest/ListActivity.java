package com.medtep.medteptest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.medtep.medteptest.data.DataManager;
import com.medtep.medteptest.models.Patient;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private DataManager mDataManager;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    private Spinner mSearchCols;
    private EditText mSearchView;

    private Spinner mSortCols;
    private Spinner mSortModes;

    private ProgressBar mPrb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initViews();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            doSearchAndSort();
        }
    }

    //region Inits

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPatient();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchCols = (Spinner) findViewById(R.id.spnSearchCol);
        mSearchView = (EditText) findViewById(R.id.txtSearch);

        mSortCols = (Spinner) findViewById(R.id.spnSortCol);
        mSortModes = (Spinner) findViewById(R.id.spnSortMode);

        mPrb = (ProgressBar) findViewById(R.id.prb);

        AdapterView.OnItemSelectedListener spnListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doSearchAndSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };

        mSearchCols.setOnItemSelectedListener(spnListener);
        mSortCols.setOnItemSelectedListener(spnListener);
        mSortModes.setOnItemSelectedListener(spnListener);

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                doSearchAndSort();
            }
        });
    }

    private void initData() {
        mDataManager = DataManager.getInstance();

        mAdapter = new Adapter();
        mAdapter.setItems(mDataManager.getPatients());
        mRecyclerView.setAdapter(mAdapter);
    }

    //endregion

    //region Actions

    private void newPatient() {
        startActivityForResult(new Intent(ListActivity.this, DetailsActivity.class), 0);
    }

    private void editPatient(Patient patient) {
        try {
            startActivityForResult(new Intent(this, DetailsActivity.class)
                .putExtra("patient", patient.toJs().toString()), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showContextualDialog(final Patient patient) {
        new AlertDialog.Builder(this)
                .setItems(getResources().getStringArray(R.array.patient_actions),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0) {
                            editPatient(patient);
                        }
                        else {
                            deletePatientDialog(patient);
                        }
                    }
                })
                .show();
    }

    private void deletePatientDialog(final Patient patient) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_patient_confirm)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePatient(patient);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void deletePatient(Patient patient) {
        mDataManager.deletePatient(patient);
        doSearchAndSort();
    }

    private void doSearchAndSort() {
        new AsyncTask<Object, Void, List<Patient>>() {
            @Override
            protected void onPreExecute() {
                mPrb.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<Patient> doInBackground(Object... params) {
                List<Patient> patients = new ArrayList<>();
                try {
                    patients = mDataManager.getPatients(
                            (int) params[0],
                            (String) params[1],
                            (int) params[2],
                            (int) params[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return patients;
            }

            @Override
            protected void onPostExecute(List<Patient> patients) {
                mPrb.setVisibility(View.GONE);

                mAdapter.setItems(patients);
                mAdapter.notifyDataSetChanged();
            }
        }.execute(mSearchCols.getSelectedItemPosition(), mSearchView.getText().toString().trim(),
                mSortCols.getSelectedItemPosition(), mSortModes.getSelectedItemPosition());
    }

    //endregion

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<Patient> mItems;
        private LayoutInflater mInflater;

        public Adapter() {
            mItems = new ArrayList<>();
            mInflater = getLayoutInflater();
        }

        public void setItems(List<Patient> items) {
            mItems.clear();
            mItems.addAll(items);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.layout_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Patient item = mItems.get(position);

            holder.idView.setText("Id: " + item.getId());
            holder.nameView.setText(item.getName());
            holder.surnameView.setText(item.getSurname());
            holder.statusView.setText(item.getStatus());

            holder.cardView.setTag(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            CardView cardView;
            TextView idView;
            TextView nameView;
            TextView surnameView;
            TextView statusView;

            public ViewHolder(View itemView) {
                super(itemView);

                cardView = (CardView) itemView.findViewById(R.id.cardView);
                idView = (TextView) itemView.findViewById(R.id.txtId);
                nameView = (TextView) itemView.findViewById(R.id.txtName);
                surnameView = (TextView) itemView.findViewById(R.id.txtSurname);
                statusView = (TextView) itemView.findViewById(R.id.txtStatus);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editPatient((Patient) view.getTag());
                    }
                });

                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showContextualDialog((Patient) view.getTag());
                        return true;
                    }
                });
            }
        }
    }
}
