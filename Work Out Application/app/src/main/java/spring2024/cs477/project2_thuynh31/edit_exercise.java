package spring2024.cs477.project2_thuynh31;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class edit_exercise extends AppCompatActivity {

    TextView name;
    EditText repInput;
    EditText setInput;
    EditText weightInput;
    EditText notesInput;
    String ExerciseName;
    int oldRep;
    int oldSet;
    int oldWeight;
    String oldNotes;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);

        name = findViewById(R.id.ExerciseName);
        repInput = findViewById(R.id.repInput);
        setInput = findViewById(R.id.setInput);
        weightInput = findViewById(R.id.weightInput);
        notesInput = findViewById(R.id.notesInput);

        // Getting the original information of the exercise being edited
        // Having this information is used as default values
        Intent intent = getIntent();
        ExerciseName = intent.getStringExtra("exerciseName");
        oldRep = intent.getIntExtra("oldRep", 0);
        oldSet = intent.getIntExtra("oldSet", 0);
        oldWeight = intent.getIntExtra("oldWeight", 0);
        oldNotes = intent.getStringExtra("oldNotes");

        // Using original information to set the hints
        name.setText("Name: " + ExerciseName);
        repInput.setHint("Previous: " + oldRep);
        setInput.setHint("Previous: " + oldSet);
        weightInput.setHint("Previous: " + oldWeight);
        notesInput.setHint("Previous: " + oldNotes);

        dbHelper = new Database(this);
    }

    public void updateExercise(View view) {
        int newRep;
        int newSet;
        int newWeight;
        String newNotes;

        // Check if new number of reps is given, if not, use original value
        if(repInput.getText().length() != 0) {
            newRep = Integer.parseInt(repInput.getText().toString());
        }
        else {
            newRep = oldRep;
        }
        // Check if new number of sets is given, if not, use original value
        if(setInput.getText().length() != 0) {
            newSet = Integer.parseInt(setInput.getText().toString());
        }
        else {
            newSet = oldSet;
        }
        // Check if new number of weights is given, if not, use original value
        if(weightInput.getText().length() != 0) {
            newWeight = Integer.parseInt(weightInput.getText().toString());
        }
        else {
            newWeight = oldWeight;
        }
        // Check if new notes is given, if not, use original value
        if(notesInput.getText().length() != 0) {
            newNotes = notesInput.getText().toString();
        }
        else {
            newNotes = oldNotes;
        }

        // Update the item in the db
        dbHelper.update(ExerciseName, newRep, newSet, newWeight, newNotes);
        // Finish activity
        finish();
    }

    // Return to caller activity as no action was done
    public void cancelExercise(View view) {
        finish();
    }
}