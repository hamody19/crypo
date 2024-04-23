package com.example.myapplication.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.encrypetedtextmodule.EncryptedTextModule;

import java.util.List;

public class TextRepository {
    EncryptedTextDao encryptedTextDao;

    public TextRepository(Application application) {
        EncryptedTextDb db = EncryptedTextDb.getEncryptedTextDatabase(application);
        encryptedTextDao = db.encryptedTextDao();
    }

    public void insertText(EncryptedTextModule encryptedTextModule){
        EncryptedTextDb.mExecutorDatabaseWriter.execute(new Runnable() {
            @Override
            public void run() {
                encryptedTextDao.insertText(encryptedTextModule);
            }
        });
    }

    public void deleteText(EncryptedTextModule encryptedTextModule){
        EncryptedTextDb.mExecutorDatabaseWriter.execute(new Runnable() {
            @Override
            public void run() {
                encryptedTextDao.deleteText(encryptedTextModule);
            }
        });
    }

    public void updateText(EncryptedTextModule encryptedTextModule){
        EncryptedTextDb.mExecutorDatabaseWriter.execute(new Runnable() {
            @Override
            public void run() {
                encryptedTextDao.updateText(encryptedTextModule);
            }
        });
    }

    public LiveData<List<EncryptedTextModule>> getAllEncryptedText(){
        return encryptedTextDao.getAllEncryptedText();
    }

}

