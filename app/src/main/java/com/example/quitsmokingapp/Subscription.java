package com.example.quitsmokingapp;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Subscription extends AppCompatActivity implements PaymentResultListener {

    private Button btnUnlockPro;
    RequestQueue queue;
    String url_user_update;
    String url_user_readone;

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription);

        url_user_update = "http://" + getString(R.string.ip_address) + "/quitsmoking/user.php?action=update";
        url_user_readone = "http://" + getString(R.string.ip_address) + "/quitsmoking/user.php?action=readOne";
        btnUnlockPro = findViewById(R.id.btnUnlockPro);
        String strAmount = "6.66";
        int amount = Math.round(Float.parseFloat(strAmount) * 100);

        // check and disable unlock button if user already unlock PRO plan
        checkIsUserPro();

        // razorpay
        btnUnlockPro.setOnClickListener(view -> {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_1Kz92Ma8b1iztx");
            checkout.setImage(R.drawable.clip_smart_speaker_notification);
            JSONObject object = new JSONObject();

            try {
                object.put("name", "Pro Subscription");
//                    object.put("description", "Test Desc"); ********************** if din put will it err?
//                    object.put("send_sms_hash",true);
//                    object.put("allow_rotation", true);
                //You can omit the image option to fetch the image from dashboard
                object.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                object.put("currency", "MYR");
                object.put("amount", amount);

                checkout.open(Subscription.this, object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void checkIsUserPro() {
        SharedPreferences pref = getSharedPreferences("user_id", MODE_PRIVATE);
        int user_id = pref.getInt("user_id", 0);
        String url = url_user_readone + "&user_id=" + user_id;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            // disable unlock btn if user is pro
            JSONArray arr;
            try {
                arr = new JSONArray(response);
                JSONObject jObj = arr.getJSONObject(0);
                if (jObj.getInt("pro") == 2) {
                    btnUnlockPro.setEnabled(false);
                }
            } catch (JSONException e) {
//                Toast.makeText(Subscription.this, e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(Subscription.this, response, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(Subscription.this, error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("error",error.toString());
        });
        queue.add(request);
    }

    @Override
    public void onPaymentSuccess(String s) {
        // show success message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Payment is successful. You've unlock PRO subscription plan! \n\nPayment ID: " + s);
        builder.show();
        btnUnlockPro.setEnabled(false);

        // update user to PRO in database
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url_user_update, response -> {
//                Toast.makeText(Subscription.this, response.toString(), Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(Subscription.this, error.toString(), Toast.LENGTH_LONG).show())
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", "1");
                params.put("pro", "2");
                return params;
            }
        };
        queue.add(request);

    }

    @Override
    public void onPaymentError(int i, String s) {
        // show error message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Payment failed. " + s);
        builder.show();
    }
}
