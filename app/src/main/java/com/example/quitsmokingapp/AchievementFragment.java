package com.example.quitsmokingapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AchievementFragment extends Fragment {

    private RecyclerView recyclerView;
    private achievementAdapter achievementAdapter;
    private List<Achievement> achievementList;
    public String SHOW_ALL_USER_DATA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        SHOW_ALL_USER_DATA = "http://" + getString(R.string.ip_address) + "/quitsmoking/outputAchievement.php";
        recyclerView = view.findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        achievementList = new ArrayList<>();

        LoadAchievement();

        return view;
    }

    private void LoadAchievement(){
        JsonArrayRequest request = new JsonArrayRequest(SHOW_ALL_USER_DATA, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONArray array) {

                for(int i=0; i < array.length(); i++){
                    try {
                        JSONObject object = array.getJSONObject(i);
                        String title = object.getString("title").trim();
                        String achievementid = object.getString("achievement_id").trim();
                        String hint = object.getString("hint").trim();
                        String reward = object.getString("reward").trim();

                        Achievement achievement = new Achievement();
                        achievement.setTitle(title);
                        achievement.setAchievementid(achievementid);
                        achievement.setHint(hint);
                        achievement.setReward(reward);
                        achievementList.add(achievement);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                achievementAdapter = new achievementAdapter(getContext(), achievementList);
                recyclerView.setAdapter(achievementAdapter);
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

}