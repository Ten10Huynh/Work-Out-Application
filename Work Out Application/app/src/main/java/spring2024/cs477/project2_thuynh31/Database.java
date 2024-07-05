package spring2024.cs477.project2_thuynh31;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {
    final static String TABLE_NAME = "exercises";
    final static String _ID = "_id";
    final static String ITEM = "exercise";
    final static String REP = "reps";
    final static String SET = "sets";
    final static String WEIGHT = "weight";
    final static String NOTE = "note";
    final String[] columns = {_ID, ITEM, REP, SET, WEIGHT, NOTE};
    final private static String DBNAME = "exercise.db";
    final private static Integer VERSION = 2;
    final Context context;
    public Database (Context context) {
        super(context, DBNAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating query
        String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEM + " TEXT NOT NULL, " +
                REP + " INTEGER, " +
                SET + " INTEGER, " +
                WEIGHT + " INTEGER, " +
                NOTE + " TEXT)";
        db.execSQL(CREATE_CMD);

        // Set three default values to be in the database
        ContentValues values = new ContentValues();
        values.put(ITEM, "Squats");
        values.put(REP, 10);
        values.put(SET, 5);
        values.put(WEIGHT, 15);
        values.put(NOTE, "");
        db.insert(TABLE_NAME, null, values);
        values.clear();
        values.put(ITEM, "Lunges");
        values.put(REP, 20);
        values.put(SET, 3);
        values.put(WEIGHT, 0);
        values.put(NOTE, "");
        db.insert(TABLE_NAME, null, values);
        values.clear();
        values.put(ITEM, "Burpees");
        values.put(REP, 10);
        values.put(SET, 3);
        values.put(WEIGHT, 0);
        values.put(NOTE, "");
        db.insert(TABLE_NAME, null, values);
        values.clear();
    }

    // Read the whole database to account for any changes
    public Cursor readAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor newCursor = null;
        newCursor = db.query("exercises", new String[]{"_id", "exercise", "reps", "sets", "weight", "note"},
                null, null, null, null, null);
        return newCursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add an exercise to the database based on the given information (name, reps, sets, weight, notes)
    public void add(String exerciseName, int numOfReps, int numOfSets, int weight, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM, exerciseName);
        values.put(REP, numOfReps);
        values.put(SET, numOfSets);
        values.put(WEIGHT, weight);
        values.put(NOTE, notes);
        db.insert(TABLE_NAME, null, values);
        values.clear();
    }

    // Delete an exercise from the database based on the long click
    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("exercises", "_id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Update the specific exercise based on the new information given by user
    public void update(String exerciseName, int newRep, int newSet, int newWeight, String newNotes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM, exerciseName);
        values.put(REP, newRep);
        values.put(SET, newSet);
        values.put(WEIGHT, newWeight);
        values.put(NOTE, newNotes);
        int status = db.update(TABLE_NAME, values, ITEM + "=?", new String[]{exerciseName});
        if(status == 0) Toast.makeText(context.getApplicationContext(), "No updates needed", Toast.LENGTH_SHORT).show();
        values.clear();
        db.close();
    }
}
