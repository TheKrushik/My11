package com.example.student.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Fragment1 extends Fragment {

    private static final String EXTRA_STUDENT = "com.example.student.myapplication.STUDENTS";

    private ListView mListView;
    private ArrayList<Student> mStudents;

    public static Fragment1 newInstance(ArrayList<Student> students) {
        Fragment1 fragment = new Fragment1();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_STUDENT, students);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        mStudents = getArguments().getParcelableArrayList(EXTRA_STUDENT);

        ArrayAdapter<Student> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mStudents);

        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = mStudents.get(position);

                if (mListener != null) {
                    mListener.studentSelected(student);
                }
            }
        });

        return view;
    }

    private StudentListener mListener;

    public void setStudentListener(StudentListener listener) {
        mListener = listener;
    }

    public interface StudentListener {
        void studentSelected(Student student);
    }
}
