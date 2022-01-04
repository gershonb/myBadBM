package edu;
import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskMark;
import edu.touro.mco152.bm.DiskWorker;
import edu.touro.mco152.bm.IUserInterface;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.BeforeAll;
import org.testng.annotations.Test;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class DiskWorkerTest implements IUserInterface {

        DiskWorker diskWorker = new DiskWorker(this);
        int percentCompleted;
        DiskMark marker;
        boolean temp;

    /**
     * Bruteforce setup of static classes/fields to allow DiskWorker to run.
     *
     * @author lcmcohen
     */
    @BeforeAll
    static void setupDefaultAsPerProperties()
    {


        /// Do the minimum of what  App.init() would do to allow to run.
        Gui.mainFrame = new MainFrame();
        App.p = new Properties();
        App.loadConfig();
        System.out.println(App.getConfigString());
        Gui.progressBar = Gui.mainFrame.getProgressBar(); //must be set or get Nullptr

        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", App.APP_CACHE_DIR);

        // code from startBenchmark
        //4. create data dir reference
        App.dataDir = new File(App.locationDir.getAbsolutePath()+File.separator+App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                App.msg("removed existing data dir");
            } else {
                App.msg("unable to remove existing data dir");
            }
        }
        else
        {
            App.dataDir.mkdirs(); // create data dir if not already present
        }
    }


    /**
     * Test Methods
     */

    @Test
    void testprogressSetter() throws Exception {
        diskWorker.execute();
        temp = percentCompleted > 0;
        System.out.println(percentCompleted + "this is percent complete");
        assertTrue(temp);
        System.out.println("yay the test works");
    }

    @Test
    void testpublishData() throws Exception {
        diskWorker.execute();
        double temp = marker.getCumAvg();
        assertTrue(temp > 0);

    }

    @Test
    public void testDWsetProgress(int i) {
        assertTrue(i >= 0 && i <= 100);
    }


    @Test
    public void testDWcancel(boolean bool) {
        assertTrue(this.DWcancel(true));
        assertFalse(this.DWcancel(false));
    }



    @Override
    public void DWsetProgress(int i) {

    }

    @Override
    public boolean DWisCancelled() {
        return true;
    }

    @Override
    public void DWpublish(DiskMark m) {

    }

    @Override
    public void DWaddPropertyChangeListener(PropertyChangeListener prop) {

    }

    @Override
    public boolean DWcancel(boolean bool) {
        return bool;
    }

    @Override
    public void DWexecute() {

    }

    @Override
    public void run() {

    }

    @Override
    public void setPassedDW(DiskWorker dw) {
        this.diskWorker = dw;
    }
}
