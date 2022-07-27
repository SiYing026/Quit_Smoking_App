package com.example.quitsmokingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityFragment extends Fragment {

    RequestQueue queue;
    String url_forum_readAll;
    TextView tvCommunityFbGrpUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, container, false);
        url_forum_readAll = "http://" + getString(R.string.ip_address) + "/quitsmoking/community_forum.php?action=readAll";

        ListView resultsListView = view.findViewById(R.id.lvForum);
        tvCommunityFbGrpUrl = view.findViewById(R.id.tvCommunityFbGrpUrl);

        HashMap<String, String> title_content = new HashMap<>();

        queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url_forum_readAll, response -> {
            try {
                // get settings row in database in json format
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    title_content.put(jObj.getString("title"), jObj.getString("content"));
                }

                List<HashMap<String, String>> listItems = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.forum_list_item,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.forumTitle, R.id.forumContent});


                for (Map.Entry<String, String> stringStringEntry : title_content.entrySet()) {
                    HashMap<String, String> resultsMap = new HashMap<>();
                    resultsMap.put("First Line", ((Map.Entry) stringStringEntry).getKey().toString());
                    resultsMap.put("Second Line", ((Map.Entry) stringStringEntry).getValue().toString());
                    listItems.add(resultsMap);
                }

                resultsListView.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("error",error.toString());
        });
        queue.add(request);

        // Inflate the layout for this fragment
        return view;
    }
}