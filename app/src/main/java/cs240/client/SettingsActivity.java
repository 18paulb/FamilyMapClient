package cs240.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    Switch lifeStory;
    Switch familyLines;
    Switch spouseLines;
    Switch fatherSide;
    Switch motherSide;
    Switch maleEvents;
    Switch femaleEvents;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lifeStory = findViewById(R.id.lifeStoryLines);
        familyLines = findViewById(R.id.familyLines);
        spouseLines = findViewById(R.id.spouseLines);
        fatherSide = findViewById(R.id.fatherSide);
        motherSide = findViewById(R.id.motherSide);
        maleEvents = findViewById(R.id.maleEvents);
        femaleEvents = findViewById(R.id.femaleEvents);
        logout = findViewById(R.id.logout);

        DataCache cache = DataCache.getInstance();

        lifeStory.setChecked(cache.getSettings().get("LifeStory"));
        familyLines.setChecked(cache.getSettings().get("FamilyLines"));
        spouseLines.setChecked(cache.getSettings().get("SpouseLines"));
        fatherSide.setChecked(cache.getSettings().get("FatherLines"));
        motherSide.setChecked(cache.getSettings().get("MotherLines"));
        maleEvents.setChecked(cache.getSettings().get("MaleEvents"));
        femaleEvents.setChecked(cache.getSettings().get("FemaleEvents"));

        lifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSettings("LifeStory", isChecked);
            }
        });

        familyLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSettings("FamilyLines", isChecked);
            }
        });

        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSettings("SpouseLines", isChecked);
            }
        });

        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSettings("FatherLines", isChecked);
            }
        });

        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSettings("MotherLines", isChecked);
            }
        });

        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSettings("MaleEvents", isChecked);
            }
        });

        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cache.setSettings("FemaleEvents", isChecked);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}