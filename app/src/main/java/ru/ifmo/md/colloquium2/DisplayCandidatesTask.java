package ru.ifmo.md.colloquium2;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DisplayCandidatesTask extends AsyncTask<Void, Void, List<Candidate>> {
    DatabaseHelper dbHelper;
    ListView listView;

    protected DisplayCandidatesTask(DatabaseHelper dbHelper, ListView listView) {
        this.dbHelper = dbHelper;
        this.listView = listView;
    }

    @Override
    protected List<Candidate> doInBackground(Void... voids) {
        ArrayList<Candidate> candidates = new ArrayList<Candidate>();
        Cursor c = dbHelper.getAllCandidates();
        c.moveToFirst();
        while (!c.isBeforeFirst() && !c.isAfterLast()) {
            candidates.add(new Candidate(c.getString(c.getColumnIndex("name")), c.getInt(c.getColumnIndex("votes"))));
            c.moveToNext();
        }
        c.close();
        return candidates;
    }

    @Override
    protected void onPostExecute(List<Candidate> candidates) {
        float sum = 0;
        for (int i = 0; i < candidates.size(); i++) {
            sum += candidates.get(i).getVotes();
            //Log.d("mydebug", candidates.get(i).getName());
        }
        for (int i = 0; i < candidates.size(); i++) {
            float votes = candidates.get(i).getVotes();
            if (Math.abs(sum) < 0.001) candidates.get(i).setPercent(0);
                else candidates.get(i).setPercent(votes / sum * 100);
        }
        CandidatesListAdapter adapter = new CandidatesListAdapter(candidates);
        listView.setAdapter(adapter);
    }

}