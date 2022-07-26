package com.example.quitsmokingapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class achievementAdapter extends RecyclerView.Adapter<achievementAdapter.achievementHolder>{

    final Context context;
    final List<Achievement> achievementList;

    RequestQueue queue;
    final String URL = "http://192.168.0.108/quitSmoke/insertComplete.php";
    @RequiresApi(api = Build.VERSION_CODES.N)


    public achievementAdapter(Context context, List<Achievement> achievementList) {
        this.context = context;
        this.achievementList = achievementList;
    }

    @NonNull
    @Override
    public achievementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View achievementLayout = LayoutInflater. from(parent.getContext()).inflate(R.layout.user_list,parent,false);
        return new achievementHolder(achievementLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull achievementHolder holder, int position) {

        Achievement achievement = achievementList.get(position);
        holder.title.setText(achievement.getTitle());
        holder.hint.setText(achievement.getHint());
        holder.reward.setText(achievement.getReward());
        holder.achievementid.setText(achievement.getAchievementid());
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public class achievementHolder extends RecyclerView.ViewHolder{

        final TextView title;
        final TextView hint;
        final TextView reward;
        final TextView achievementid;
        ImageView complete;
        Button btncomplete;


        public achievementHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            hint = itemView.findViewById(R.id.tv_hint);
            reward = itemView.findViewById(R.id.tv_reward);
            achievementid = itemView.findViewById(R.id.tv_achievementid);
            complete = itemView.findViewById(R.id.iv_complete);
            complete.setVisibility(View.INVISIBLE);

            btncomplete = itemView.findViewById(R.id.btn_complete);

            btncomplete.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    complete.setVisibility(View.VISIBLE);
                    queue = Volley.newRequestQueue(context);
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("error",error.toString());


                        }
                    })
                    {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("achievementid" , achievementid.getText().toString());
                            params.put("userid" , "1");
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
            });

        }

        }

}
