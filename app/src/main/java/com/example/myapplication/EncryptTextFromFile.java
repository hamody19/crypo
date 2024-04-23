package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EncryptTextFromFile extends AppCompatActivity {

    Button buttonConfirmFilePath;

    Button buttonBrows, buttonBrowsFileCancelOperation;
    EditText editTextUserSecretKey;
    EditText editTextFilePath;

    private static final int PICK_FILE_REQUEST_CODE = 1;
    String fileContent = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_text_from_file);

        buttonBrows                    = findViewById(R.id.buttonBrowsFile);
        editTextFilePath               = findViewById(R.id.editTextFilePath);
        buttonConfirmFilePath          = findViewById(R.id.buttonConfirmPath);
        editTextUserSecretKey          = findViewById(R.id.editTextUserSecretKey);
        buttonBrowsFileCancelOperation = findViewById(R.id.buttonBrowsFileCancelOperation);

        buttonBrows.setOnClickListener(v -> openFilePicker());
        buttonBrowsFileCancelOperation.setOnClickListener(v -> finish());
        buttonConfirmFilePath.setOnClickListener(v -> sendTextToMainActivityToEncrypt());

    }


    void sendTextToMainActivityToEncrypt(){
        String userSecretKey = editTextUserSecretKey.getText().toString();
        if (userSecretKey.isEmpty()){
            Toast.makeText(this, "you should enter key to encrypt", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fileContent == null){
            Toast.makeText(this, "no file content found", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("plainText", fileContent);
        intent.putExtra("userSecretKey", userSecretKey);
        setResult(RESULT_OK, intent);
        finish();
    }



    public void openFilePicker(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST_CODE);
        }catch (Exception exception){
            Toast.makeText(EncryptTextFromFile.this, "please install a file manager!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                assert uri != null;
                @SuppressLint("Recycle")
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                fileContent = stringBuilder.toString();
                editTextFilePath.setText(fileContent);
                reader.close();
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}