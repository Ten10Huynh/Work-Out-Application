package spring2024.cs477.project2_thuynh31;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // User is choosing to track a workout
    public void trackRoutineActivity(View view) {
        Intent intent = new Intent(this, track_routine.class);
        startActivity(intent);
    }

    // User is accessing the DB that holds all current exercises
    public void setUpActivity(View view) {
        Intent intent = new Intent(this, setup_edit.class);
        startActivity(intent);
    }
}