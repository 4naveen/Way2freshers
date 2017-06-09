package com.project.lorvent.way2freshers.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.lorvent.way2freshers.R;
import com.project.lorvent.way2freshers.utils.AppConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {

    ProgressDialog dialog;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_result, container, false);
        setHasOptionsMenu(true);
        ActionBar actionBar=((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Dashboard");
        }
        new GetAllNotification().execute();

        return v;
    }
    class GetAllNotification extends AsyncTask<String, Void, String> {
        String response;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(AppConstant.NOTIFICATION_URL);
                connection = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                }
                response = buffer.toString();
                //  Log.i("response in d",response);
            } catch (IOException e) {

                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

    }

}}
