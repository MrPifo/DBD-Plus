package sperlich;

import javafx.concurrent.Task;

public class ParseTask extends Task<Void> {

    private long progress;

    public ParseTask() {
        progress = 0;
    }

    public Void call() {

        

        return null ;
    }

    public long getTotalLines() {
        return progress;
    }
}
