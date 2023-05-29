package com.medicine.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.medicine.app.Activites.ProfileViewActivity;
import com.medicine.app.Model.Comments;
import com.medicine.app.R;
import com.medicine.app.databinding.ShowCommentsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class AdapterShowComments extends RecyclerView.Adapter<AdapterShowComments.viewHolder> {
    private Context context;
    private List<Comments> list;


    private ShowCommentsBinding binding;

    public AdapterShowComments() {
    }

    public AdapterShowComments(Context context, List<Comments> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterShowComments.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ShowCommentsBinding.inflate(LayoutInflater.from(context));
        return new viewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShowComments.viewHolder holder, int position) {
        Comments comments = list.get(position);
        holder.namePerson.setText(comments.getFirstName() + " " + comments.getMiddleName() + " " + comments.getLastName());
        Log.d("Timagesasa", "onBindViewHolder: "+comments.getTimeStamp());
        try {
            Locale LocaleBylanguageTag = Locale.forLanguageTag("ps");
            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
            Long time = Long.parseLong(comments.getTimeStamp()+"");
            String text = TimeAgo.using(time, messages);
            holder.timeStamp.setText(text);
        }catch (Exception e){

        }

        holder.comments.setText(comments.getComments());
        try {
            Picasso.get().load(comments.getImageView()).placeholder(R.drawable.spinner).into(holder.imageView);
        }catch (Exception e){

        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileViewActivity.class);
                i.putExtra("idUser",comments.getUidUser());
                context.startActivity(i);
            }
        });

        holder.namePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileViewActivity.class);
                i.putExtra("idUser",comments.getUidUser());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView namePerson;
        TextView comments;
        TextView timeStamp;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = binding.imagePersonComments;
            namePerson = binding.namePerson;
            comments = binding.commentsPerson;
            timeStamp = binding.timeStamp;
        }
    }
}
