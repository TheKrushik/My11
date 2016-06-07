package com.example.student.myapplication;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class StudentsLoader extends AsyncTaskLoader<ArrayList<Student>> {

    private Context mContext;
    private ArrayList<Student> Students;

    public StudentsLoader(Context context) {
        super(context);

        this.mContext = context;
    }

    @Override
    public ArrayList<Student> loadInBackground() {
        DataBaseHelper helper = new DataBaseHelper(mContext);

        return helper.getStudents();
    }

    @Override
    public void deliverResult(ArrayList<Student> data) {
        if (isReset()) {
            return;
        }

        Students = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (Students != null) {
            deliverResult(Students);
        }

        if (takeContentChanged() || Students == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if (Students != null) {
            Students = null;
        }
    }
}