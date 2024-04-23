package com.example.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.encrypetedtextmodule.EncryptedTextModule;

import java.util.List;

@Dao
public interface EncryptedTextDao {
    @Insert
    void insertText(EncryptedTextModule encryptedTextModule);

    @Update
    void updateText(EncryptedTextModule encryptedTextModule);

    @Delete
    void deleteText(EncryptedTextModule encryptedTextModule);

    @Query("SELECT * FROM encrypted_text_table ORDER BY ID_column ASC")
    LiveData<List<EncryptedTextModule>> getAllEncryptedText();
}
