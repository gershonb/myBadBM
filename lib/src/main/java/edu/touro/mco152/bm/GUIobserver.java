package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

public class GUIobserver implements IObserver {

    @Override
    public void update(DiskRun run) {
        Gui.runPanel.addRun(run);
    }
}
