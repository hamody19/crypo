package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.encryptionalgorithm.AESCipher;

public class NoteToEncrypt extends AppCompatActivity {

    EditText editTextKey, editTextPlainNote;
    Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_to_encrypt);

        editTextKey       = findViewById(R.id.editTextKeyField);
        editTextPlainNote = findViewById(R.id.editTextNoteField);

        btnCancel = findViewById(R.id.buttonCancelNote);
        btnSave   = findViewById(R.id.buttonSaveNote);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteToEncrypt.this, "exiting the process", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEncryptedNoteAndTimeToDataBase();
            }
        });

    }

//    public void encryptNote(){
//        String plainText = editTextPlainNote.getText().toString();
//        if (plainText.isEmpty()){
//            Toast.makeText(this, "you should enter some text to encrypt", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String userSecretKey = editTextKey.getText().toString();
//        if (userSecretKey.isEmpty()){
//            Toast.makeText(this, "you should enter key to encrypt", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        long startTime = System.nanoTime();
//        String cipherText = AESCipher.msgEncrypt(plainText, userSecretKey);
//        long endTime = System.nanoTime();
//
//        saveEncryptedNoteAndTimeToDataBase(cipherText, (endTime - startTime));
//
//    }

    void saveEncryptedNoteAndTimeToDataBase(){
        String plainText = editTextPlainNote.getText().toString();
        if (plainText.isEmpty()){
            Toast.makeText(this, "you should enter some text to encrypt", Toast.LENGTH_SHORT).show();
            return;
        }
        String userSecretKey = editTextKey.getText().toString();
        if (userSecretKey.isEmpty()){
            Toast.makeText(this, "you should enter key to encrypt", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("plainText", plainText);
        intent.putExtra("userSecretKey", userSecretKey);
        setResult(RESULT_OK, intent);
        finish();
    }
}