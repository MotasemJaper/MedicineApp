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

import com.medicine.app.Activites.MessagesActivity;
import com.medicine.app.Model.TopicPatient;
import com.medicine.app.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.ViewHolder> {
    private Context context ;
    private List<TopicPatient> chatLists;
    private HashMap<String,String> LastMessage;
    private HashMap<String,String> LastTimeStampMessage;

    public AdapterChatList() {
    }

    public AdapterChatList(Context context, List<TopicPatient> chatLists) {
        this.context = context;
        this.chatLists = chatLists;
        LastMessage = new HashMap<>();
        LastTimeStampMessage = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chat_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TopicPatient topicPatient = chatLists.get(position);
        String hisUid = topicPatient.getId();
        String hisUidTimeStamp = topicPatient.getId();
        String lastmessage = LastMessage.get(hisUid);
        String lastmessageTimeStamp = LastTimeStampMessage.get(hisUidTimeStamp);
        holder.fullNameChatList.setText(topicPatient.getFirstName());

        if (topicPatient.getImageTopic() == null){
            holder.imageChatList.setImageResource(R.drawable.usera);
        }else {
            Picasso.get().load(topicPatient.getImageTopic()).placeholder(R.drawable.spinner).into(holder.imageChatList);
        }
        if (lastmessage == null || lastmessage.equals("default")){
            holder.lastMessage.setVisibility(View.GONE);
        }else {
            holder.lastMessage.setVisibility(View.VISIBLE);
            holder.lastMessage.setText(lastmessage);
        }
        if (lastmessageTimeStamp == null || lastmessageTimeStamp.equals("default")){
            holder.timeStampLastMessage.setVisibility(View.GONE);
        }else {
            holder.timeStampLastMessage.setVisibility(View.VISIBLE);
            holder.timeStampLastMessage.setText(lastmessageTimeStamp);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessagesActivity.class);
                intent.putExtra("idUser",topicPatient.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public void LastMessage(String Uid, String lastmessage){
        LastMessage.put(Uid,lastmessage);
    }
    public void LastTimeStampMessagee(String Uid, String time){
        LastTimeStampMessage.put(Uid,time);
    }


    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView status ;
        ImageView imageChatList ;
        TextView fullNameChatList;
        TextView lastMessage;
        TextView timeStampLastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageChatList = itemView.findViewById(R.id.imageChatList);
            status = itemView.findViewById(R.id.status);
            fullNameChatList = itemView.findViewById(R.id.fullNameChatList);
            lastMessage = itemView.findViewById(R.id.lastMessageChatList);
            timeStampLastMessage = itemView.findViewById(R.id.timeStampLastMessage);
        }
    }
}
