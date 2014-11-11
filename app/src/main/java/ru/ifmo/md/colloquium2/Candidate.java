package ru.ifmo.md.colloquium2;

public class Candidate {
    String name;
    int votes;
    float percent;

    public Candidate(String name, int votes) {
        this.name = name;
        this.votes = votes;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
