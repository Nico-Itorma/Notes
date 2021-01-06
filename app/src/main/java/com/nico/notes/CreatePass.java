package com.nico.notes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePass extends AppCompatActivity {

    EditText pin1, pin2;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass);

        pin1 = findViewById(R.id.create_pin1);
        pin2 = findViewById(R.id.create_pin2);
        confirm = findViewById(R.id.confirm);

    }

    public void confirm(View view) {
        String text1 = pin1.getText().toString();
        String text2 = pin2.getText().toString();

        if ((text1.equals("")) || (text2.equals("")))
        {
            Toast.makeText(CreatePass.this, "No PIN entered", Toast.LENGTH_SHORT).show();
        }
        else if (pin1.length() == 4 && pin2.length() == 4)
        {
            if (text1.equals(text2)) {
                //save pin to password
                SharedPreferences sp = getSharedPreferences("PIN", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("password", text2);
                editor.apply();

                Intent intent = new Intent(CreatePass.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        if (pin1.length() < 4 && pin2.length() < 4) {
            pin1.setError("PIN must be at least 4 characters");
            pin2.setError("PIN must be at least 4 characters");

            if ((pin1.length() == 4 && pin2.length() == 4) && (!text1.equals(text2)))
            {
                pin2.setError("PIN do not match");
            }
        }
    }
}