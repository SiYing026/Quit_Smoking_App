package com.example.quitsmokingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminNotificationSettingsActivity extends AppCompatActivity {

    private EditText etNotiTitle;
    private EditText etNotiContent;

    RequestQueue queue;
    String url_settings_readOne;
    String url_settings_update;
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification_settings);

        url_settings_readOne = "http://" + getString(R.string.ip_address) + "/quitsmoking/settings.php?action=readOne&setting_type=notification";
        url_settings_update = "http://" + getString(R.string.ip_address) + "/quitsmoking/settings.php?action=update&setting_type=notification";
        etNotiTitle = findViewById(R.id.etNotiTitle);
        etNotiContent = findViewById(R.id.etNotiContent);

        showCurrentSetting();
    }

    private void showCurrentSetting() {
        // select title and content from database and show
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url_settings_readOne, response -> {
            try {
                // get settings row in database in json format
                JSONArray arrRow = new JSONArray(response);
                JSONObject jObjRow = arrRow.getJSONObject(0);

                // get content of notification settings in json format
                String settingContentStr = jObjRow.getString("setting_content");
                JSONObject jObjSettingContent = new JSONObject(settingContentStr);
                etNotiTitle.setText(jObjSettingContent.getString("title"));
                etNotiContent.setText(jObjSettingContent.getString("content"));

            } catch (JSONException e) {
                Toast.makeText(AdminNotificationSettingsActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(AdminNotificationSettingsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            Log.d("error",error.toString());
        });
        queue.add(request);
    }

    public void btnUpdateNotiOnclick(View view) {
        //update notification title and content in database
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url_settings_update, response -> Toast.makeText(AdminNotificationSettingsActivity.this,"Notification updated successfully",Toast.LENGTH_LONG).show(), error -> {
            Toast.makeText(AdminNotificationSettingsActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            Log.d("error",error.toString());
        })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(
                        "setting_content",
                        "{\"title\": \"" + etNotiTitle.getText().toString() + "\", " +
                                "\"content\":\"" + etNotiContent.getText().toString() + "\"}");
                return params;
            }
        };
        queue.add(request);
    }
}