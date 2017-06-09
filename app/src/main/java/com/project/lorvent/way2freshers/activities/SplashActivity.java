package com.project.lorvent.way2freshers.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.project.lorvent.way2freshers.MainActivity;
import com.project.lorvent.way2freshers.R;
import com.project.lorvent.way2freshers.utils.AppConstant;
import com.project.lorvent.way2freshers.utils.AppSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {
    //private static int SPLASH_TIME_OUT = 7000;
    ImageView image;
    Animation animation1, animation2, animation3;
    ArrayList<String> titleArrayList;
    ArrayList<String> urlArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        image = (ImageView) findViewById(R.id.imageView);
        Fabric.with(this, new Crashlytics());

        // TODO: Move this to where you establish a user session
        logUser();

        titleArrayList=new ArrayList<>();
        urlArrayList=new ArrayList<>();
        AppSession.titleArrayList=new ArrayList<>();
        AppSession.urlArrayList=new ArrayList<>();
        animation1 = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.anti_rotate);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        image.startAnimation(animation2);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new GetAllRecent().execute();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(animation3);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
  /*  public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }*/
    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }

    class GetAllRecent extends AsyncTask<String, Void, String> {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(AppConstant.RECENT_URL);
                connection = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    buffer.append(temp);
                }
                response = buffer.toString();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject object1=object.getJSONObject("title");
                    titleArrayList.add(object1.getString("rendered"));
                    JSONObject object2 = object.getJSONObject("_links");
                    JSONArray array = object2.getJSONArray("self");
                    JSONObject object3 = array.getJSONObject(0);
                    urlArrayList.add(object3.getString("href"));
                }
                AppSession.titleArrayList.addAll(titleArrayList);
                AppSession.urlArrayList.addAll(urlArrayList);
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            catch (JSONException e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
        }
    }
}

