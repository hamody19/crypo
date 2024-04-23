package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.MyEncryptedTextViewModel;
import com.example.myapplication.encrypetedtextmodule.EncryptedTextModule;
import com.example.myapplication.encryptedtextadapter.EncryptedTextAdapter;
import com.example.myapplication.encryptionalgorithm.AESCipher;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //declaring the encrypted text view model
    private MyEncryptedTextViewModel mEncryptedTextViewModel;
    ActivityResultLauncher<Intent> intentActivityResultLauncherForAddEncryptedNote;
//    ActivityResultLauncher<Intent> intentActivityResultLauncherForDecryptedNote;

    // Initiate the required UI elements and variables
    FloatingActionButton fabAddNote, fabAddFile;
    ExtendedFloatingActionButton fabAdd;
    TextView textViewAddNote, textViewAddFile;
    Boolean isAllFabVisible;
    // ending of initiate UI elements and the variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to register activity
        registerActivityForAddEncryptedNote();
        //end



        // initiate the encrypted text view model
        RecyclerView recyclerView = findViewById(R.id.EncryptedTextRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EncryptedTextAdapter adapter = new EncryptedTextAdapter();
        recyclerView.setAdapter(adapter);
        mEncryptedTextViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyEncryptedTextViewModel.class);


        mEncryptedTextViewModel.getAllEncryptedText().observe(this, new Observer<List<EncryptedTextModule>>() {
            @Override
            public void onChanged(List<EncryptedTextModule> encryptedTextModules) {
                //update the recycler view
                adapter.setEncryptedTextList(encryptedTextModules);
            }
        });

        //----------------------------------------end

        // deleting note from the recycler view and the database
        new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mEncryptedTextViewModel.deleteText(adapter.getEncryptText(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "the note deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        //--------------------------------------------------- end

        adapter.setOnItemClickListener(new EncryptedTextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EncryptedTextModule encryptedTextModule) {
                Intent intent = new Intent(MainActivity.this, DecryptTextActivity.class);
                intent.putExtra("encrypted-text", encryptedTextModule.getText());
                startActivity(intent);
            }
        });

        //declaring the UI elements
        //------------------------------------------------ declaring the floating buttons elements
        fabAdd          = findViewById(R.id.float_add_button);
        fabAddNote      = findViewById(R.id.float_note_button);
        fabAddFile      = findViewById(R.id.float_file_button);
        //------------------------------------------------ declaring the text view elements
        textViewAddNote = findViewById(R.id.add_note_text);
        textViewAddFile = findViewById(R.id.add_file_text);
        //ending of declaring UI element

        // hiding the unnecessary UI elements to using later
        //------------------------------------------------- hiding the floating buttons
        fabAddNote.setVisibility(View.GONE);
        fabAddFile.setVisibility(View.GONE);
        //------------------------------------------------- hiding the floating buttons text
        textViewAddFile.setVisibility(View.GONE);
        textViewAddNote.setVisibility(View.GONE);
        // ending
        isAllFabVisible = false;

        fabAdd.shrink();




        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {setUiElementsVisibility();}
        });

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO add new activity intent write text to encrypt
                Intent intent = new Intent(MainActivity.this, NoteToEncrypt.class);
                intentActivityResultLauncherForAddEncryptedNote.launch(intent);
                setUiElementsVisibility();
            }
        });

        fabAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EncryptTextFromFile.class);
                intentActivityResultLauncherForAddEncryptedNote.launch(intent);
                setUiElementsVisibility();
                // TODO new activity intent select from file to encrypt
                Toast.makeText(MainActivity.this, "File added", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    public void registerActivityForDecryptNote(){
//        intentActivityResultLauncherForAddEncryptedNote = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult o) {
//
//                    }
//                }
//                );
//
//    }

    public void registerActivityForAddEncryptedNote(){
        intentActivityResultLauncherForAddEncryptedNote = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                       int resultCode = o.getResultCode();
                       Intent data = o.getData();
                       if (resultCode == RESULT_OK && data != null){
                           String plainText     = data.getStringExtra("plainText");
                           String userSecretKey = data.getStringExtra("userSecretKey");
                           encryptNote(plainText, userSecretKey);

                       }
                    }
                }
        );
    }

    public void encryptNote(String plainText, String userSecretKey){
        long startTime = System.nanoTime();
        String cipherText = AESCipher.msgEncrypt(plainText, userSecretKey);
        long endTime = System.nanoTime();
        saveToDataBase(cipherText, (endTime - startTime));
    }



    public void saveToDataBase(String EncryptedNote, long finalTime){
        EncryptedTextModule encryptedTextModule = new EncryptedTextModule(EncryptedNote, finalTime);
        mEncryptedTextViewModel.insertText(encryptedTextModule);
    }



    // create a method to hide and show UI elements when the user click tha addFab button
    void setUiElementsVisibility(){
        int iconResourceIdAdd = R.drawable.ic_action_name;
        Drawable iconDrawableAdd = ResourcesCompat.getDrawable(getResources(), iconResourceIdAdd, null);
        int iconResourceIdClose = R.drawable.close_icon;
        Drawable iconDrawableClose = ResourcesCompat.getDrawable(getResources(), iconResourceIdClose, null);
        if (!isAllFabVisible){
            fabAdd.setIcon(iconDrawableClose);
            fabAdd.setText("close");
            // showing the UI elements for the user after click the fabAdd floating button
            //------------------------------------------- showing the add note button and it's text
            fabAddNote.show();
            textViewAddNote.setVisibility(View.VISIBLE);
            //------------------------------------------- showing the add file button and it's text
            fabAddFile.show();
            textViewAddFile.setVisibility(View.VISIBLE);
            // ending
            //extend the fab add button
            fabAdd.extend();
            // set the visibility of the buttons and it's text to true
            isAllFabVisible = true;
        }else {
            fabAdd.setIcon(iconDrawableAdd);
            // hiding the unnecessary UI elements to using later
            //------------------------------------------------- hiding the floating buttons
            fabAddNote.hide();
            fabAddFile.hide();
            //------------------------------------------------- hiding the floating buttons text
            textViewAddFile.setVisibility(View.GONE);
            textViewAddNote.setVisibility(View.GONE);
            // ending
            // set the visibility of the buttons and it's text to false
            isAllFabVisible = false;
            // shrinking the fab add button
            fabAdd.shrink();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}