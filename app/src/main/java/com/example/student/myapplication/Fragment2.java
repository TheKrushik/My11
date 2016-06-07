package com.example.student.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Fragment2 extends Fragment {

    private static final String EXTRA_STUDENT = "com.example.student.myapplication.STUDENT";

    private Student mStudent;
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextAge;
    private Button mButtonSave;

    public static Fragment2 newInstance(Student student) {
        Fragment2 fragment = new Fragment2();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STUDENT, student);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);

        mStudent = getArguments().getParcelable(EXTRA_STUDENT);

        mEditTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        mEditTextLastName = (EditText) view.findViewById(R.id.editTextLastName);
        mEditTextAge = (EditText) view.findViewById(R.id.editTextAge);

        mEditTextFirstName.setText(mStudent.FirstName);
        mEditTextLastName.setText(mStudent.LastName);
        mEditTextAge.setText(String.valueOf(mStudent.Age));

        mButtonSave = (Button) view.findViewById(R.id.buttonSave);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStudent.FirstName = mEditTextFirstName.getText().toString();
                mStudent.LastName = mEditTextLastName.getText().toString();
                mStudent.Age = Integer.parseInt(mEditTextAge.getText().toString());

                if (mListener != null) {
                    mListener.studentSaved(mStudent);
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
        void studentSaved(Student student);
    }
}
