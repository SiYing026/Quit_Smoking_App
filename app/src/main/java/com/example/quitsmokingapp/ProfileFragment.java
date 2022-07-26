package com.example.quitsmokingapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPassword, tvAmt_cigarette, tvPrice_cigarette;
    RequestQueue queue;
    String URL = "http://192.168.0.9/quitsmoking/user.php?email=dorae@gmail.com";
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.name);
        tvEmail = view.findViewById(R.id.email);
        tvPassword = view.findViewById(R.id.password);
        tvAmt_cigarette = view.findViewById(R.id.amt_cigarette);
        tvPrice_cigarette = view.findViewById(R.id.price_cigarette);
        queue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
                    JSONObject jObj = arr.getJSONObject(0);
                    tvName.setText(jObj.getString("name"));
                    tvEmail.setText(jObj.getString("email"));
                    tvPassword.setText(jObj.getString("password"));
                    tvAmt_cigarette.setText(jObj.getString("amt_cigarette"));
                    tvPrice_cigarette.setText(jObj.getString("price_cigarette"));

                    Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvName.setText(error.toString());
                tvEmail.setText(error.toString());
                tvPassword.setText(error.toString());
                tvAmt_cigarette.setText(error.toString());
                tvPrice_cigarette.setText(error.toString());
                Log.d("error",error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);



        return view;
    }
}