package com.example.lab12_contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContactName = findViewById(R.id.contactNameEditText);
    }

    public void addName(View view) {

        // Get the name supplied
        String name = etContactName.getText().toString();

        // Stores a key value pair
        ContentValues values = new ContentValues();
        values.put(ContactProvider.name, name);

        // Provides access to other applications Content Providers
        Uri uri = getContentResolver().insert(ContactProvider.CONTENT_URL, values);

        Toast.makeText(getBaseContext(), "New Contact Added", Toast.LENGTH_LONG)
                .show();

        //add this line to clear your EditText
        etContactName.setText("");
    }
}
