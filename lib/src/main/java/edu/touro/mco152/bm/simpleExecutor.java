package edu.touro.mco152.bm;

/**
 * Executor that implements runnable
 */
public class simpleExecutor{

    Runnable e;

    public simpleExecutor(Runnable e) {
        this.e = e;
    }

    public void start() {
        this.e.run();
    }
}
