package cs240.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PersonActivity extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        gender = findViewById(R.id.personGender);

        Intent intent = getIntent();

        if (intent != null) {
            firstName.setText(intent.getStringExtra("firstName"));
            lastName.setText(intent.getStringExtra("lastName"));
            gender.setText(intent.getStringExtra("gender"));
        }

    }
}