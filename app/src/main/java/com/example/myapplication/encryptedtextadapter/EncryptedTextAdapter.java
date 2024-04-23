package com.example.myapplication.encryptedtextadapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.encrypetedtextmodule.EncryptedTextModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EncryptedTextAdapter extends RecyclerView.Adapter<EncryptedTextAdapter.EncryptedTextViewHolder> {

    private static List<EncryptedTextModule> encryptedTextList = new ArrayList<>();
    private static OnItemClickListener listener;

    @NonNull
    @Override
    public EncryptedTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.encrypted_text_item, parent, false);
        return new EncryptedTextViewHolder(view);
    }
    // get the position of the encrypted text back to delete it
    public EncryptedTextModule getEncryptText(int position){
        return encryptedTextList.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EncryptedTextViewHolder holder, int position) {
        EncryptedTextModule encryptedTextModule = encryptedTextList.get(position);
        //convert the nanoseconds to millisecond
        long millisecond = TimeUnit.MILLISECONDS.convert(encryptedTextModule.getTime(), TimeUnit.NANOSECONDS);
        //end
        holder.textViewTime.setText("Execution time: " + Long.toString(millisecond) + "ms");
        holder.textViewEncryptedText.setText(encryptedTextModule.getText());
    }

    @Override
    public int getItemCount() {
        return encryptedTextList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEncryptedTextList(List<EncryptedTextModule> encryptedTextList){
        this.encryptedTextList = encryptedTextList;
        notifyDataSetChanged();
    }

    static class EncryptedTextViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTime, textViewEncryptedText;
        public EncryptedTextViewHolder(@NonNull View itemView){
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewEncryptedText = itemView.findViewById(R.id.textViewEncryptedText);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(encryptedTextList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(EncryptedTextModule encryptedTextModule);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        EncryptedTextAdapter.listener = listener;
    }



}
