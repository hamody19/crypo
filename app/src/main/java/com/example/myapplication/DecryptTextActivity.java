package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.encryptionalgorithm.AESCipher;

import java.util.concurrent.TimeUnit;

public class DecryptTextActivity extends AppCompatActivity {

    EditText editTextDecryptKey;
    Button btnDecrypt;

    TextView textViewDecryptTime;
    TextView textViewDecryptNote;


    String keyToDecrypt;
    String encryptedText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_text);

        editTextDecryptKey  = findViewById(R.id.editTextDecryptKey);
        btnDecrypt          = findViewById(R.id.buttonDecrypt);
        textViewDecryptTime = findViewById(R.id.textViewDecryptTime);
        textViewDecryptNote = findViewById(R.id.textViewDecryptText);
        getData();
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decryptNoteText();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void decryptNoteText(){
        keyToDecrypt = editTextDecryptKey.getText().toString();
        if (keyToDecrypt.isEmpty()){
            Toast.makeText(DecryptTextActivity.this, "Key field is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("DecryptTextActivity", keyToDecrypt);

        long startTime = System.nanoTime();

        String decryptedText = AESCipher.msgDecrypt(encryptedText, keyToDecrypt);
        if (decryptedText != null && decryptedText.isEmpty()) {
            Log.i("DecryptTextActivity", "Decrypted text is empty");
        }

        long endTime   = System.nanoTime();
        long finalTime = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        textViewDecryptTime.setText("Execution time: " + Long.toString(finalTime) + "ms");
        textViewDecryptNote.setText(decryptedText);
    }


    public void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            encryptedText = intent.getStringExtra("encrypted-text");

            if (encryptedText != null && !encryptedText.isEmpty()) {
                // Data is not empty, process it
                Log.i("DecryptTextActivity", "Encrypted Text: " + encryptedText);
            } else {
                // Data is empty, show a toast
                Toast.makeText(this, "The intent data was empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Intent is null, show a toast or handle accordingly
            Toast.makeText(this, "No intent data found", Toast.LENGTH_SHORT).show();
        }
    }

}