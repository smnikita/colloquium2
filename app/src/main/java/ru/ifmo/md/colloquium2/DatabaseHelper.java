package ru.ifmo.md.colloquium2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "candidates_db";
    private static final int VERSION = 1;

    private static final String TABLE_CANDIDATES = "candidates";
    private static final String COLUMN_CANDIDATE_ID = "_id";
    private static final String COLUMN_CANDIDATE_NAME = "name";
    private static final String COLUMN_CANDIDATE_VOTES = "votes";

    private static final String INIT_CANDIDATE_TABLE =
            "CREATE TABLE " + TABLE_CANDIDATES + " (" +
                    COLUMN_CANDIDATE_ID + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CANDIDATE_NAME+ " TEXT, " +
                    COLUMN_CANDIDATE_VOTES + " TEXT);";


    private SQLiteDatabase mReadableDB;
    private SQLiteDatabase mWritableDB;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mReadableDB = getReadableDatabase();
        mWritableDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_CANDIDATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_CANDIDATES);
        onCreate(db);
    }

    public long insertCandidate(Candidate candidate) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CANDIDATE_NAME, candidate.getName());
        cv.put(COLUMN_CANDIDATE_VOTES, candidate.getVotes());
        return mWritableDB.insert(TABLE_CANDIDATES, null, cv);
    }

    public Cursor getAllCandidates() {
        return mReadableDB.query(
                TABLE_CANDIDATES,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public void clear() {
        mWritableDB.delete(
                TABLE_CANDIDATES,
                null,
                null
        );
    }

    public void delete(int id) {
        mWritableDB.delete(
          TABLE_CANDIDATES,
          COLUMN_CANDIDATE_ID + " = " + Integer.toString(id),
          null
        );
    }

    public Cursor get(String name) {
        Cursor c = mReadableDB.query(
            TABLE_CANDIDATES,
            null,
            COLUMN_CANDIDATE_NAME + " = ?",
            new String[]{name},
            null,
            null,
            null,
            "1"
        );
        return c;
    }

    public void vote(String name) {
        ContentValues cv = new ContentValues();
        Cursor c = get(name);
        Candidate candidate = null;
        c.moveToFirst();
        if (!c.isBeforeFirst() && !c.isAfterLast()) {
            candidate = new Candidate(c.getString(c.getColumnIndex(COLUMN_CANDIDATE_NAME)), c.getInt(c.getColumnIndex(COLUMN_CANDIDATE_VOTES)));
            cv.put(COLUMN_CANDIDATE_NAME, candidate.getName());
            cv.put(COLUMN_CANDIDATE_VOTES, candidate.getVotes() + 1);
            mWritableDB.update(
                    TABLE_CANDIDATES,
                    cv,
                    COLUMN_CANDIDATE_NAME + " = ?",
                    new String[]{name}
            );
        }
    }
}
