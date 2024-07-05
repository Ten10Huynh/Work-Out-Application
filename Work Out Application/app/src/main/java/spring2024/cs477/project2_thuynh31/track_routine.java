package spring2024.cs477.project2_thuynh31;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class track_routine extends AppCompatActivity {
    ListView workoutList;
    ArrayAdapter<String> myAdapter;
    Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_routine);

        workoutList = findViewById(R.id.workoutList);

        // Get all data from the database
        dbHelper = new Database(this);
        Cursor dataCursor = dbHelper.readAll();

        // Create an an adapter that will control the list view
        myAdapter = new ArrayAdapter<String>(this, R.layout.line);
        workoutList.setAdapter(myAdapter);

        // Creates a list of all the exercise names in the database
        ArrayList<String> dataList = new ArrayList<String>();
        dataCursor.moveToFirst();
        while(!dataCursor.isAfterLast()) {
            if(dataCursor.getString(1).length() != 0) {
                dataList.add(dataCursor.getString(1));
            }
            dataCursor.moveToNext();
        }

        // Add the list of exercise names to the list view in the UI
        for(int i = 0; i < dataList.size(); i++) {
            myAdapter.add(dataList.get(i));
        }

        Context context = this;

        // When a user clicks on the exercise, a toast message will pop up showing the exercise info (reps, sets, weights, notes)
        workoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = "";
                int rep = 0;
                int set = 0;
                int weight = 0;
                String note = "";
                // Get all the data in the database
                Cursor findExerciseCursor = dbHelper.readAll();
                findExerciseCursor.moveToNext();
                // Find the database exercise that matches with the exercise name, once found, get all the exercise information
                while(!findExerciseCursor.isAfterLast()) {
                    if (findExerciseCursor.getString(1).equals(myAdapter.getItem(position))) {
                        name = findExerciseCursor.getString(1);
                        rep = findExerciseCursor.getInt(2);
                        set = findExerciseCursor.getInt(3);
                        weight = findExerciseCursor.getInt(4);
                        note = findExerciseCursor.getString(5);
                    }
                    findExerciseCursor.moveToNext();
                }
                // Display the toast message
                String message = name + ": \nReps: " + rep + " Sets: " + set + " Weight: " + weight + "\nNote: " + note;
                Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // Long click to delete an item from the list 
        workoutList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Added a long id to delete the item if requested
                alertView("Single Item Deletion", position, id);
                return true;
            }
        });
    }

    private void alertView(String message ,final int position, long id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(track_routine.this);
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
                        myAdapter.remove(myAdapter.getItem(position));
                    }
                }).show();
    }
}