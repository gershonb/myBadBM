package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.List;

import static edu.touro.mco152.bm.App.dataDir;

public class SOLIDUserInterface extends SwingWorker <Boolean, DiskMark> implements IUserInterface {


    @Override
    public void DWsetProgress(int i) {
            super.setProgress(i);
    }

    @Override
    public boolean DWisCancelled() {
            super.isCancelled();
        return false;
    }

    @Override
    public void DWpublish(DiskMark m) {
            super.publish(m);
    }

    @Override
    public void DWaddPropertyChangeListener(PropertyChangeListener prop) {
            super.addPropertyChangeListener(prop);
    }

    @Override
    public boolean DWcancel(boolean bool) {
                super.cancel(bool);
        return bool;
    }

    @Override
    public void DWexecute() {
        super.execute();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return null;
    }



    //Swing methods copied from Diskworker


    /**
     * Process a list of 'chunks' that have been processed, ie that our thread has previously
     * published to Swing. For my info, watch Professor Cohen's video -
     * Module_6_RefactorBadBM Swing_DiskWorker_Tutorial.mp4
     * @param markList a list of DiskMark objects reflecting some completed benchmarks
     */
    protected void process(List<DiskMark> markList) {
        markList.stream().forEach((dm) -> {
            if (dm.type == DiskMark.MarkType.WRITE) {
                Gui.addWriteMark(dm);
            } else {
                Gui.addReadMark(dm);
            }
        });
    }

    protected void done() {
        if (App.autoRemoveData) {
            Util.deleteDirectory(dataDir);
        }
        App.state = App.State.IDLE_STATE;
        Gui.mainFrame.adjustSensitivity();
    }

}
