package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.encrypetedtextmodule.EncryptedTextModule;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = EncryptedTextModule.class, version = 1)
public abstract class EncryptedTextDb extends RoomDatabase {
    public abstract EncryptedTextDao encryptedTextDao();
    private static volatile EncryptedTextDb INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService mExecutorDatabaseWriter = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static synchronized EncryptedTextDb getEncryptedTextDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (EncryptedTextDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EncryptedTextDb.class,
                                    "encrypted_text_db")
                            .fallbackToDestructiveMigration()  // Add this line
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
