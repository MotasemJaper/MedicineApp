package com.medicine.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medicine.app.Activites.DetailsTopicActivity;
import com.medicine.app.Activites.EditTopicActivity;
import com.medicine.app.Model.TopicPatient;
import com.medicine.app.R;
import com.medicine.app.databinding.ShowTopicRecyclerHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterForHomeShowTopic extends RecyclerView.Adapter<AdapterForHomeShowTopic.viewHolder>{

    private ShowTopicRecyclerHomeBinding binding;
    private Context context;
    private List<TopicPatient> list;
    String type;

    public AdapterForHomeShowTopic() {
    }

    public AdapterForHomeShowTopic( Context context, List<TopicPatient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterForHomeShowTopic.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ShowTopicRecyclerHomeBinding.inflate(LayoutInflater.from(context));
        return new viewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForHomeShowTopic.viewHolder holder, int position) {
        TopicPatient topicPatient = list.get(position);
        holder.titleTopic.setText(topicPatient.getTitleTopic());
        Picasso.get().load(topicPatient.getImageTopic()).placeholder(R.drawable.spinner).into(holder.imageTopic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailsTopicActivity.class);
                i.putExtra("idTopic",topicPatient.getId());
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView imageTopic;
        private TextView titleTopic;
        private ImageButton moreMenu;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageTopic = binding.imagePatient;
            titleTopic = binding.titleTopic;

        }
    }
    public void filterList( ArrayList<TopicPatient> listFilter){
        list=listFilter;
        notifyDataSetChanged();
    }

    private void getTypeUser(String id){

    }
}
