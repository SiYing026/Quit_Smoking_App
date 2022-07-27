package com.example.quitsmokingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Context context;
    EditText email, password;
    Button btn_login;
    TextView link_regist;
    ProgressBar loading;
    RequestQueue queue;
    String URL;
    @RequiresApi(api = Build.VERSION_CODES.N)

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        URL = "http://" + getString(R.string.ip_address) + "/quitsmoking/login.php";
        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        link_regist = findViewById(R.id.link_regist);

        googleBtn = findViewById(R.id.google_btn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(Login.this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Login.this);
        if(acct!=null){
            navigateToSecondActivity();
        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String vEmail = email.getText().toString().trim();
                String vPass = password.getText().toString().trim();
                if (( !vPassword() | !vEmail())) {
                    return;

                } else {
                    queue = Volley.newRequestQueue(Login.this);
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONArray arr = null;

                            try {
                                arr = new JSONArray(response);
                                JSONObject jObj = arr.getJSONObject(0);
                                String error = jObj.getString("error");
                                int user_id = jObj.getInt("user_id");

                                if (error.equals("0")) {

                                    for (int i = 0; i < arr.length(); i++) {

                                        JSONObject object = arr.getJSONObject(i);

                                        btn_login.setVisibility(View.GONE);
                                        loading.setVisibility(View.GONE);

                                        SharedPreferences pref = getSharedPreferences("user_id", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putInt("user_id", user_id);
                                        editor.commit();

                                        Toast.makeText(Login.this, " Success Login \n  User ID: " + pref.getInt("user_id", 0), Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Password Incorrect", Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                loading.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Log.d("error", error.toString());
                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email.getText().toString());
                            params.put("password", password.getText().toString());
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
        });

        link_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
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
    private boolean vPassword() {
        String vPass = password.getText().toString().trim();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (vPass.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;

        } else if (!vPass.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;

        } else {
            password.setError(null);
            return true;
        }
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(Login.this, HomeFragment.class);
        startActivity(intent);
    }


}

