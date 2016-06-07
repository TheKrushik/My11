package com.example.student.myapplication;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Student>> {

    private ProgressDialog mDialog;
    private SaveTask mSaveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(false);
    }

    private void init(boolean restart) {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        if (restart) {
            getSupportLoaderManager().restartLoader(0, null, this);
        } else {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
    }

    public void OnClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.button1:
                onStudentSelected(new Student());
                break;
        }

        transaction.commit();
    }

    @Override
    public Loader<ArrayList<Student>> onCreateLoader(int id, Bundle args) {
        return new StudentsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Student>> loader, final ArrayList<Student> data) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                onLoaded(data);
            }
        };
        handler.sendEmptyMessage(0);
    }

    private void onLoaded(ArrayList<Student> students) {
        Fragment1 fragment1 = Fragment1.newInstance(students);
        fragment1.setStudentListener(new Fragment1.StudentListener() {
            @Override
            public void studentSelected(Student student) {
                onStudentSelected(student);
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentView, fragment1);
        transaction.commit();

        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void onStudentSelected(Student student) {
        Fragment2 fragment2 = Fragment2.newInstance(student);
        fragment2.setStudentListener(new Fragment2.StudentListener() {
            @Override
            public void studentSaved(Student student) {
                mSaveTask = new SaveTask();
                mSaveTask.execute(student);
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentView, fragment2);
        transaction.commit();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Student>> loader) {

    }

    class SaveTask extends AsyncTask<Student, Void, Boolean> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Loading...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Student... params) {
            Student student = params[0];
            DataBaseHelper helper = new DataBaseHelper(MainActivity.this);

            return helper.saveStudent(student);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (mDialog != null) {
                mDialog.dismiss();
            }

            init(true);
        }
    }
}
