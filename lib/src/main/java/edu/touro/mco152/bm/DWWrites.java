package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;
import jakarta.persistence.EntityManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.msg;
import static edu.touro.mco152.bm.DiskMark.MarkType.WRITE;

/**
 * Does the writes for a disk worker
 */
public class DWWrites implements Runnable{
    public ArrayList<IObserver> observerList = new ArrayList();

    public boolean readTest;
    public boolean writeTest;
    public int numOfMarks;      // desired number of marks
    public int numOfBlocks;
    public int blockSizeKb;
    DiskWorker dw;
    //int blockSize = blockSzizeKb * KILOBYTE;
    //Sequence of IO operations. (random vs. seq.)
    public DiskRun.BlockSequence blockSequence = DiskRun.BlockSequence.SEQUENTIAL;

    public DWWrites(DiskWorker dw, boolean readTest, boolean writeTest, int numOfMarks, int numOfBlocks, int blockSizeKb, DiskRun.BlockSequence blockSequence) {
        this.dw = dw;
        this.readTest = readTest;
        this.writeTest = writeTest;
        this.numOfMarks = numOfMarks;
        this.numOfBlocks = numOfBlocks;
        this.blockSizeKb = blockSizeKb;
        this.blockSequence = blockSequence;
    }


    /**
     * register a observer
     * @param observer takes in a observer to register
     */
    public void register(IObserver observer){
        observerList.add(observer);

    }

    /**
     * updates all registered observers
     * @param run takes in a diskRun
     */
    private void updateObservers(DiskRun run){
        for (IObserver o : observerList) {
            o.update(run);
        }
    }




    public void write(DiskWorker dw) {

        int wUnitsComplete = 0, rUnitsComplete = 0, unitsComplete;
        int wUnitsTotal = this.writeTest ? numOfBlocks * numOfMarks : 0;
        int rUnitsTotal = this.readTest ? numOfBlocks * numOfMarks : 0;
        int unitsTotal = wUnitsTotal + rUnitsTotal;
        float percentComplete;

        int blockSize = blockSizeKb * KILOBYTE;
        byte[] blockArr = new byte[blockSize];
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }

        DiskMark wMark;  // declare vars that will point to objects used to pass progress to UI

        Gui.updateLegend();  // init chart legend info

        if (App.autoReset) {
            App.resetTestData();
            Gui.resetTestData();
        }

        int startFileNum = App.nextMarkNumber;

        //-------------
        DiskRun run = new DiskRun(DiskRun.IOMode.WRITE, blockSequence);
        run.setNumMarks(this.numOfMarks);
        run.setNumBlocks(this.numOfBlocks);
        run.setBlockSize(this.blockSizeKb);
        run.setTxSize(App.targetTxSizeKb());
        run.setDiskInfo(Util.getDiskInfo(dataDir));

        // Tell logger and GUI to display what we know so far about the Run
        msg("disk info: (" + run.getDiskInfo() + ")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());

        // Create a test data file using the default file system and config-specified location
        if (!App.multiFile) {
            testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata.jdm");
        }

            /*
              Begin an outer loop for specified duration (number of 'marks') of benchmark,
              that keeps writing data (in its own loop - for specified # of blocks). Each 'Mark' is timed
              and is reported to the GUI for display as each Mark completes.
             */
        for (int m = startFileNum; m < startFileNum + this.numOfMarks && !dw.inter.DWisCancelled(); m++) {

            if (App.multiFile) {
                testFile = new File(dataDir.getAbsolutePath()
                        + File.separator + "testdata" + m + ".jdm");
            }
            wMark = new DiskMark(WRITE);    // starting to keep track of a new bench Mark
            wMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesWrittenInMark = 0;

            String mode = "rw";
            if (App.writeSyncEnable) {
                mode = "rwd";
            }

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, mode)) {
                    for (int b = 0; b < numOfBlocks; b++) {
                        if (blockSequence == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, numOfBlocks - 1);
                            rAccFile.seek((long) rLoc * blockSize);
                        } else {
                            rAccFile.seek((long) b * blockSize);
                        }
                        rAccFile.write(blockArr, 0, blockSize);
                        totalBytesWrittenInMark += blockSize;
                        wUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) unitsTotal * 100f;

                            /*
                              Report to GUI what percentage level of Entire BM (#Marks * #Blocks) is done.
                             */
                        dw.inter.DWsetProgress((int) percentComplete);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

                /*
                  Compute duration, throughput of this Mark's step of BM
                 */
            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbWritten = (double) totalBytesWrittenInMark / (double) MEGABYTE;
            wMark.setBwMbSec(mbWritten / sec);
            msg("m:" + m + " write IO is " + wMark.getBwMbSecAsString() + " MB/s     "
                    + "(" + Util.displayString(mbWritten) + "MB written in "
                    + Util.displayString(sec) + " sec)");
            App.updateMetrics(wMark);

                /*
                  Let the GUI know the interim result described by the current Mark
                 */
            dw.inter.DWpublish(wMark);

            // Keep track of statistics to be displayed and persisted after all Marks are done.
            run.setRunMax(wMark.getCumMax());
            run.setRunMin(wMark.getCumMin());
            run.setRunAvg(wMark.getCumAvg());
            run.setEndTime(new Date());
        } // END outer loop for specified duration (number of 'marks') for WRITE bench mark

            /*
              Persist info about the Write BM Run (e.g. into Derby Database) and add it to a GUI panel
             */
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();

        Gui.runPanel.addRun(run);

         /*
              Persist info about the Write BM Run and add it to a GUI
             */
        updateObservers(run);
    }


    @Override
    public void run() {
        this.write(this.dw);
    }
}
