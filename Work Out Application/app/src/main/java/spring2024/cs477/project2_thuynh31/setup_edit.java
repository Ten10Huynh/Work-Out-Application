package spring2024.cs477.project2_thuynh31;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class setup_edit extends AppCompatActivity {
    ListView exerciseList;
    Database dbHelper;
    SimpleCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_edit);

        // Setting up the exercise list to be in the db
        exerciseList = findViewById(R.id.listOfExercises);
        dbHelper = new Database(this);
        Cursor c = dbHelper.readAll();

        // Setting the adapter to use
        myAdapter = new SimpleCursorAdapter(this, R.layout.datalist, c, dbHelper.columns, new int[]{R.id._id, R.id.name}, 1);
        exerciseList.setAdapter(myAdapter);

        Context setupContext = this;

        // When a user clicks on an exercise, they are able to edit the information of that exercise (cannot edit name)
        // Sends the new activity with the old information to be used as default values
        exerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Getting the exercise info to set hints
                String itemName = myAdapter.getCursor().getString(1);
                int itemNumRep = myAdapter.getCursor().getInt(2);
                int itemNumSet = myAdapter.getCursor().getInt(3);
                int itemWeight = myAdapter.getCursor().getInt(4);
                String itemNotes = myAdapter.getCursor().getString(5);

                // Sending the exercise info to activity_edit_exercise
                Intent intent = new Intent(setupContext, edit_exercise.class);
                intent.putExtra("exerciseName", itemName);
                intent.putExtra("oldRep", itemNumRep);
                intent.putExtra("oldSet", itemNumSet);
                intent.putExtra("oldWeight", itemWeight);
                intent.putExtra("oldNotes", itemNotes);
                startActivity(intent);
            }
        });

        // Long click to delete specific items
        exerciseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Added a long id to delete the item if requested
                alertView("Single Item Deletion", position, id);
                return true;
            }
        });
    }

    // Add a new exercise to the database, launches new activity to collect data
    // Before starting new activity, creates an ArrayList of all exercise names in the database (used to check for duplicates)
    public void addExercise(View view) {
        // Creating list of exercise names
        ArrayList<String> exerciseList = new ArrayList<String>();
        myAdapter.getCursor().moveToFirst();
        while(!myAdapter.getCursor().isAfterLast()) {
            if(myAdapter.getCursor().getString(1).length() != 0) {
                exerciseList.add(myAdapter.getCursor().getString(1));
            }
            myAdapter.getCursor().moveToNext();
        }

        Intent intent = new Intent(this, add_exercise.class);
        intent.putExtra("exerciseList", exerciseList);
        startActivity(intent);
    }

    // After returning from an activity, "refresh" the database to show current exercises in it
    @Override
    public void onResume() {
        super.onResume();
        Cursor newC = dbHelper.readAll();
        myAdapter.swapCursor(newC);
    }

    // Used to notify the user if they wish to delete or cancel their request
    private void alertView(String message ,final int position, long id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(setup_edit.this);
        dialog.setTitle( message )
                .setIcon(R.drawable.ic_launcher_background)
                .setMessage("Are you sure you want to do this?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        // Delete the exercise if requested
                        dbHelper.delete(id);
                        // Reset the cursor
                        Cursor newC = dbHelper.readAll();
                        myAdapter.swapCursor(newC);
                    }
                }).show();
    }
}