package com.example.quitsmokingapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    ImageSlider imageSlider;
    AdView mAdView;
    Button share;
    TextView timeElapsed,item_createdTimeStamp, amt_amount, won_minutes;
    private TextView tv_title, tv_content;

    RequestQueue queue;
    String URL = "http://192.168.0.9/quitsmoking/user.php?email=dorae@gmail.com";
    String url_tips = "http://192.168.0.9/quitsmoking/tips.php?action=readOne&setting_type=smoking_tips";
    String url_user_readone = "http://192.168.0.9/quitsmoking/user.php?action=readOne";

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        timeElapsed = view.findViewById(R.id.tv_timeElapsed);
        item_createdTimeStamp = view.findViewById(R.id.tv_item_createdTimeStamp);
        amt_amount = view.findViewById(R.id.amount);
        won_minutes = view.findViewById(R.id.minutes);

        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);

        showCurrentSetting();
        queue = Volley.newRequestQueue(getContext());


        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
                    JSONObject jObj = arr.getJSONObject(0);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String currentDateandTime = sdf.format(new Date());

                    //Convert string to date format
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String display = "%d days, %d hours, %d minutes, %d seconds";
                    double cigarette_amt = 0.0;

                    try {
                        Date current = dateformat.parse(currentDateandTime);
                        Date created_time = dateformat.parse(jObj.getString("created_time"));
                        //Calculate days between actual return date and estimated return date (7 days)
                        long difference = 0L, days = 0L, hours = 0L, mins = 0L, secs = 0L;
                        long secondsInMilli = 0L, minutesInMilli = 0L, hoursInMilli = 0L, daysInMilli = 0L;
                        difference = current.getTime() - created_time.getTime();
                        secondsInMilli = 1000;
                        minutesInMilli = secondsInMilli * 60;
                        hoursInMilli = minutesInMilli * 60;
                        daysInMilli = hoursInMilli * 24;

                        days = difference / daysInMilli;
                        difference = difference % daysInMilli;

                        hours = difference / hoursInMilli;
                        difference = difference % hoursInMilli;

                        mins = difference / minutesInMilli;
                        difference = difference % minutesInMilli;

                        secs = difference / secondsInMilli;

                        display = String.format(display,days, mins, hours, secs);
                        timeElapsed.setText(display);

                        int amt_cigarette = Integer.parseInt(jObj.getString("amt_cigarette"));
                        double price_cigarette = Double.parseDouble(jObj.getString("price_cigarette"));

                        double tempcigarette_amt = (double) days * amt_cigarette / 20;
                        cigarette_amt = tempcigarette_amt * price_cigarette;
                        amt_amount.setText(String.format("%.2f", cigarette_amt));

                        int wonbackminutes = amt_cigarette * 11;
                        won_minutes.setText(String.valueOf(wonbackminutes));

                        throw new ParseException("Current Date/ Created Date converted wrongly.", 0);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    item_createdTimeStamp.setText(jObj.getString("created_time"));

                    Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                timeElapsed.setText(error.toString());
                item_createdTimeStamp.setText(error.toString());
                Log.d("error",error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);


        imageSlider = view.findViewById(R.id.slider_banner);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://www.healthhub.sg/sites/assets/Assets/Article%20Images/iStock-698143352.jpg?Width=970&Height=405","Banner 1"));
        slideModels.add(new SlideModel("https://oneyoueastsussex.org.uk/wp-content/uploads/2018/01/Quit-Smoking-Banner.jpg","Banner 2"));
        slideModels.add(new SlideModel("https://2rdnmg1qbg403gumla1v9i2h-wpengine.netdna-ssl.com/wp-content/uploads/sites/3/2018/07/GettyImages-824302050-745x490.jpg","Banner 3"));
        imageSlider.setImageList(slideModels, true);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        share = view.findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String Body = "Download this App";
                String Sub = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                intent.putExtra(Intent.EXTRA_TEXT, Body);
                intent.putExtra(Intent.EXTRA_TEXT, Sub);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });

        return view;
    }

    private void showCurrentSetting() {
        // select title and content from database and show
        queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url_tips, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // get settings row in database in json format
                    JSONArray arrRow = new JSONArray(response);
                    JSONObject jObjRow = arrRow.getJSONObject(0);

                    // get content of notification settings in json format
                    String settingContentStr = jObjRow.getString("setting_content");
                    JSONObject jObjSettingContent = new JSONObject(settingContentStr);
                    tv_title.setText(jObjSettingContent.getString("title"));
                    tv_content.setText(jObjSettingContent.getString("content"));

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }

    private void checkIsUserPro() {
        String url = url_user_readone + "&user_id=1";

        queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // disable unlock btn if user is pro
                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
                    JSONObject jObj = arr.getJSONObject(0);
                    if (jObj.getInt("pro") == 2) {
                        mAdView.setEnabled(false);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }


}