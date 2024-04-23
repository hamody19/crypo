package com.example.myapplication.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.encrypetedtextmodule.EncryptedTextModule;

import java.util.List;


public class MyEncryptedTextViewModel extends AndroidViewModel {

    TextRepository textRepository;
    LiveData<List<EncryptedTextModule>> encryptedTextList;
    public MyEncryptedTextViewModel(@NonNull Application application) {
        super(application);
        textRepository = new TextRepository(application);
        encryptedTextList = textRepository.getAllEncryptedText();
    }

    public void insertText(EncryptedTextModule encryptedTextModule){
        textRepository.insertText(encryptedTextModule);
    }

    public void deleteText(EncryptedTextModule encryptedTextModule){
        textRepository.deleteText(encryptedTextModule);
    }
    public void updateText(EncryptedTextModule encryptedTextModule){
        textRepository.updateText(encryptedTextModule);
    }

    public LiveData<List<EncryptedTextModule>> getAllEncryptedText(){
        return encryptedTextList;
    }
}
