package com.example.quitsmokingapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {

    RequestQueue queue;
    EditText name,email,password, c_password, amt_cigarette, price_cigarette;
    Button btn_regist;
    ProgressBar loading;
    String URL;
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        URL = "http://" + getString(R.string.ip_address) + "/quitsmoking/register.php";
        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password= findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_regist = findViewById(R.id.btn_regist);
        amt_cigarette = findViewById(R.id.amt_cigarette);
        price_cigarette = findViewById(R.id.price_cigarette);
    }

    //Name
    private Boolean vName() {
        String vaName = name.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (vaName .isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else if (vaName.length() >= 15) {
            name.setError("Username too long");
            return false;
        } else if (!vaName.matches(noWhiteSpace)) {
            name.setError("White Spaces are not allowed");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    //Email
    private Boolean vEmail() {
        String vaEmail = email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (vaEmail.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!vaEmail.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    //Password

    //Confirm Password

    //Password && Confirm Password
    private boolean vPass() {
        String vPass = password.getText().toString().trim();
        String vCPass = c_password.getText().toString().trim();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (vPass.isEmpty() |vCPass.isEmpty()) {
            password.setError("Field can't be empty");
            c_password.setError("Field can't be empty");
            return false;

        }  else if (!vPass.matches(passwordVal) | !vCPass.matches(passwordVal)) {
            password.setError("Password is too weak");
            c_password.setError("Password is too weak");
            return false;

        }if (vPass.length()<5) {
            password.setError("Password must be at least 5 characters");
            return false;
        }
        if (!vPass.equals(vCPass)) {
            password.setError("Password Would Not be matched");
            return false;
        }else {

            return true;
        }
    }


    public void btnPostClicked(View view) {
        String vName = name.getText().toString().trim();
        String vEmail = email.getText().toString().trim();
        String vPass = password.getText().toString().trim();
        String vCPass = c_password.getText().toString().trim();

        if ((!vName() | !vPass() | !vEmail())) {
            return;

        } else {
            queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(Register.this, response.toString(), Toast.LENGTH_LONG).show();

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("error", error.toString());
                    Toast.makeText(Register.this, error.toString(), Toast.LENGTH_LONG).show();

                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("password", password.getText().toString());
                    params.put("amt_cigarette", amt_cigarette.getText().toString());
                    params.put("price_cigarette", price_cigarette.getText().toString());
                    return params;
                }
            };
            queue.add(request);
            request.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
        }
    }
}