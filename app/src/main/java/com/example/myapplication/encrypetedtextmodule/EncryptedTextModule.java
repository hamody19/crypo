package com.example.myapplication.encrypetedtextmodule;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "encrypted_text_table")
public class EncryptedTextModule {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID_column")
    private int id;
    @ColumnInfo(name = "encrypted_text_column")
    private String text;
    @ColumnInfo(name = "time_require_to_encrypt_column")
    private long time;

    public EncryptedTextModule(String text, long time) {
        this.text = text;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }
}
