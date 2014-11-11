package ru.ifmo.md.colloquium2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CandidatesListAdapter extends BaseAdapter {
    List<Candidate> candidates;

    public CandidatesListAdapter(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    @Override
    public int getCount() {
        return candidates.size();
    }

    @Override
    public Object getItem(int i) {
        return candidates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.candidate, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.candidate_name)).setText(candidates.get(i).getName());
        ((TextView) view.findViewById(R.id.candidate_votes)).setText(Integer.toString(candidates.get(i).getVotes()));
        ((TextView) view.findViewById(R.id.candidate_percent)).setText(Float.toString(candidates.get(i).getPercent()));
        return view;
    }
}
