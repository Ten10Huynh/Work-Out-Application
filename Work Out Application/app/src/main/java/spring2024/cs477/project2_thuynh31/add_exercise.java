package spring2024.cs477.project2_thuynh31;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class add_exercise extends AppCompatActivity {
    EditText name;
    EditText reps;
    EditText sets;
    EditText weight;
    EditText notes;
    Database dbHelper;
    ArrayList<String> exerciseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        name = findViewById(R.id.exerciseName);
        reps = findViewById(R.id.numReps);
        sets = findViewById(R.id.numSets);
        weight = findViewById(R.id.weight);
        notes = findViewById(R.id.notes);

        dbHelper = new Database(this);

        // Get the ArrayList that has all the names of each exercise
        // Used to check for duplicates
        Intent intent = getIntent();
        exerciseList = intent.getStringArrayListExtra("exerciseList");
    }

    // Add an exercise, must have a field in the name input
    // If other fields are left empty, send back default values (which is 0 or empty string for notes)
    // If not name is given, post a toast message letting user know

    public void addToWorkout(View view) {
        String exerciseName = "";
        int numOfReps = 0;
        int numOfSets = 0;
        int weightNum = 0;
        String newNotes = "";
        Boolean sameName = false;

        // Check if the user has given a name, if no name given, show toast message
        if(name.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "There must be a name for the exercise", Toast.LENGTH_SHORT).show();
        }
        else {
            // Check if the name given is a duplicate of exercise in list
            exerciseName = name.getText().toString();
            for(int i = 0; i < exerciseList.size(); i++) {
                if(exerciseList.get(i).equals(exerciseName)) {
                    sameName = true;
                    break;
                }
            }

            // If the name is a duplicate, show toast message and user cannot proceed
            if(sameName) {
                Toast.makeText(getApplicationContext(), "Each exercise must have a unique name", Toast.LENGTH_SHORT).show();
            }
            else {
                // Check if number of reps is given
                if (reps.getText().length() != 0) {
                    numOfReps = Integer.parseInt(reps.getText().toString());
                }
                // Check if number of sets is given
                if (sets.getText().length() != 0) {
                    numOfSets = Integer.parseInt(sets.getText().toString());
                }
                // Check if any weights are given
                if (weight.getText().length() != 0) {
                    weightNum = Integer.parseInt(weight.getText().toString());
                }
                // Check if any notes were written
                if (notes.getText().length() != 0) {
                    newNotes = notes.getText().toString();
                }
                // Add all the information about exercise to database
                dbHelper.add(exerciseName, numOfReps, numOfSets, weightNum, newNotes);
                // Finish activity
                finish();
            }
        }
    }

    // Return to original activity caller, no actions done
    public void cancel(View view) {
        finish();
    }
}