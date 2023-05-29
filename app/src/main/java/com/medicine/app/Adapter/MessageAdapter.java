package com.medicine.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.medicine.app.Model.Chat;
import com.medicine.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MEG_TYPE_LEFT = 0;
    public static final int MEG_TYPE_RIGHT = 1;
    private Context context;
    private List<Chat> list ;
    private String imageUri;
    FirebaseUser fuser;
    FirebaseAuth mAuth;
    public MessageAdapter() {
    }

    public MessageAdapter(Context context, List<Chat> list, String imageUri) {
        this.context = context;
        this.list = list;
        this.imageUri = imageUri;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          if (viewType == MEG_TYPE_RIGHT){
          View v = LayoutInflater.from(context).inflate(R.layout.item_chat_right,parent,false);
         return new ViewHolder(v);
        }else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_chat_left,parent,false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Chat chat = list.get(position);
        holder.ShowMessage.setText(chat.getMessage());
        try {
            if (imageUri != null && URLUtil.isValidUrl(imageUri)){
                Picasso.get().load(imageUri).placeholder(R.drawable.spinner).error(R.drawable.usera).into(holder.imageProfileChatRight);
            }
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfileChatRight;
        TextView ShowMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfileChatRight = itemView.findViewById(R.id.imageProfileChatRight);
            ShowMessage = itemView.findViewById(R.id.ShowMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        mAuth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(fuser.getUid())){
            return MEG_TYPE_RIGHT;
        }else {
            return  MEG_TYPE_LEFT;
        }
    }
}
