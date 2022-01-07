package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

public interface IObserver {

    void update(DiskRun run);
}
