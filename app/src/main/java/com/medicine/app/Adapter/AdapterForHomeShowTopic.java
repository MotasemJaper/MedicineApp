package com.medicine.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medicine.app.Model.TopicPatient;
import com.medicine.app.R;
import com.medicine.app.databinding.ShowTopicRecyclerHomePatientOneBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterForHomeShowTopic extends RecyclerView.Adapter<AdapterForHomeShowTopic.viewHolder> {

    private ShowTopicRecyclerHomePatientOneBinding binding;
    private Context context;
    private List<TopicPatient> list;

    public AdapterForHomeShowTopic() {
    }

    public AdapterForHomeShowTopic(Context context, List<TopicPatient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterForHomeShowTopic.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       binding = ShowTopicRecyclerHomePatientOneBinding.inflate(LayoutInflater.from(context));
        return new viewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForHomeShowTopic.viewHolder holder, int position) {
        TopicPatient topicPatient = list.get(position);
        holder.titleTopic.setText(topicPatient.getTitleTopic());
        Picasso.get().load(topicPatient.getImageTopic()).placeholder(R.drawable.spinner).into(holder.imageTopic);
        Log.d("getTitle", "onBindViewHolder: "+topicPatient.getTitleTopic());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView imageTopic;
        private TextView titleTopic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageTopic = binding.imagePatient;
            titleTopic = binding.titleTopic;

        }
    }
}
