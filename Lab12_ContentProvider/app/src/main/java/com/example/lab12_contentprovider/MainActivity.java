package com.example.lab12_contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContactName = findViewById(R.id.edt_ContactName);
    }

    public void addName(View view) {
        String name = etContactName.getText().toString();

        ContentValues values = new ContentValues();

        values.put(ContactProvider.name, name);

        Uri uri = getContentResolver().insert(ContactProvider.CONTENT_URL, values);

        Toast.makeText(getBaseContext(),"New Contact Added", Toast.LENGTH_SHORT).show();
        etContactName.setText("");
    }
}
