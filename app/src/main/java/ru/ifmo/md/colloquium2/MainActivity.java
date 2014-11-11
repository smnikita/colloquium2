package ru.ifmo.md.colloquium2;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    DatabaseHelper dbHelper;
    EditText editText;
    ListView listView;
    boolean voting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        new DisplayCandidatesTask(dbHelper, listView).execute();
        stop();

        /*Cursor c = dbHelper.getAllCandidates();
        c.moveToFirst();
        while (!c.isAfterLast() && !c.isBeforeFirst()) {
            Log.d("mydebug", c.getString(c.getColumnIndex("_id")));
            c.moveToNext();
        }*/
    }

    public void onClickAddCandidate(View view) {
        if (!voting) {
            String name = editText.getText().toString();
            dbHelper.insertCandidate(new Candidate(name, 0));
            new DisplayCandidatesTask(dbHelper, listView).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Voting already started!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickStart(View view) {
        start();
    }

    public void onClickStop(View view) {
        stop();
    }


    public void onClickReset(View view) {
        dbHelper.clear();
        stop();
        new DisplayCandidatesTask(dbHelper, listView).execute();
    }

    private void start() {
        voting = true;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long x) {
                Candidate c = (Candidate)adapter.getItemAtPosition(position);
                dbHelper.vote(c.getName());
                new DisplayCandidatesTask(dbHelper, listView).execute();
            }
        });
    }

    private void stop() {
        voting = false;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long x) {
                Candidate c = (Candidate)adapter.getItemAtPosition(position);
                dbHelper.delete(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
