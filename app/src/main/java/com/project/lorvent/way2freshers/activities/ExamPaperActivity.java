package com.project.lorvent.way2freshers.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.project.lorvent.way2freshers.R;
import com.project.lorvent.way2freshers.adapters.LatestJobAdapter;
import com.project.lorvent.way2freshers.utils.AppConstant;
import com.project.lorvent.way2freshers.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ExamPaperActivity extends AppCompatActivity {
    ArrayList<String> exampaperList;
    ArrayList<String> urlArrayList;
    RecyclerView recyclerView;
    LatestJobAdapter mAdapter;
    ActionBar actionBar;
    String category_id;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_paper);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Question Paper");
        }
        category_id=getIntent().getStringExtra("id");
        exampaperList = new ArrayList<>();
        urlArrayList = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        new GetAllEaxmPapers().execute();
    }
    class GetAllEaxmPapers extends AsyncTask<String, Void, String> {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ExamPaperActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(AppConstant.EXAM_URL+ category_id);
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

            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject object1 = object.getJSONObject("title");
                    exampaperList.add(object1.getString("rendered"));
                    JSONObject object2 = object.getJSONObject("_links");
                    JSONArray array = object2.getJSONArray("self");
                    JSONObject object3 = array.getJSONObject(0);
                    urlArrayList.add(object3.getString("href"));

                }

                //rv.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new LatestJobAdapter(ExamPaperActivity.this, exampaperList);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                RecyclerView.LayoutManager lmanager = new LinearLayoutManager(ExamPaperActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(lmanager);
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ExamPaperActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override

                    public void onClick(View view, int position) {

                        Intent i = new Intent(ExamPaperActivity.this, DetailsActivity.class);
                        i.putExtra("url", urlArrayList.get(position));
                        startActivity(i);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();

            }
        }
        return super.onOptionsItemSelected(item);
    }

}
