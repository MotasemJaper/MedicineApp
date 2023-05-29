package com.medicine.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.medicine.app.Activites.DetailsTopicActivity;
import com.medicine.app.Model.Notifi;
import com.medicine.app.R;
import com.medicine.app.databinding.ItemNotificationsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.viewHolder> {
    Context context;
    List<Notifi> list;
    ItemNotificationsBinding binding;
    public AdapterNotifications() {
    }

    public AdapterNotifications(Context context, List<Notifi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterNotifications.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemNotificationsBinding.inflate(LayoutInflater.from(context));
        return new viewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotifications.viewHolder holder, int position) {
        Notifi notifi = list.get(position);
        holder.not.setText(notifi.getNot());

        holder.nameNot.setText(notifi.getFirstName());

        try {
            Picasso.get().load(notifi.getImageUrl()).placeholder(R.drawable.spinner).into(holder.imageNot);
            Locale LocaleBylanguageTag = Locale.forLanguageTag("ps");
            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
            Long time = Long.parseLong(notifi.getDateTopic()+"");
            String text = TimeAgo.using(time, messages);
            holder.timeStamp.setText(text);
        }catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailsTopicActivity.class);
                i.putExtra("idTopic",notifi.getIdTopic());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageNot;
        TextView nameNot;
        TextView timeStamp;
        TextView not;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageNot = binding.imageChatList;
            nameNot = binding.fullNameChatList;
            timeStamp = binding.timeStamp;
            not = binding.lastMessageChatList;

        }
    }
}
