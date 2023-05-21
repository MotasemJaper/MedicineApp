package com.medicine.app.Adapter;

import android.content.Context;
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
import com.medicine.app.databinding.ShowTopicRecyclerHomePatientTowBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterForHomePatientShowTopicTow extends RecyclerView.Adapter<AdapterForHomePatientShowTopicTow.viewHolder>{

    private ShowTopicRecyclerHomePatientTowBinding binding;
    private Context context;
    private List<TopicPatient> list;

    public AdapterForHomePatientShowTopicTow() {
    }

    public AdapterForHomePatientShowTopicTow(Context context, List<TopicPatient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterForHomePatientShowTopicTow.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ShowTopicRecyclerHomePatientTowBinding.inflate(LayoutInflater.from(context));
        return new viewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForHomePatientShowTopicTow.viewHolder holder, int position) {
        TopicPatient topicPatient = list.get(position);
        holder.titleTopic.setText(topicPatient.getTitleTopic());
        Picasso.get().load(topicPatient.getImageTopic()).placeholder(R.drawable.spinner).into(holder.imageTopic);
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
