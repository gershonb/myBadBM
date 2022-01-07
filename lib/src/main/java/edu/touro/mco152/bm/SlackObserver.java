package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

public class SlackObserver implements IObserver{

    /**
     * sends a slack message
     * @param run takes in a DiskRun
     */
    @Override
    public void update(DiskRun run) {
        if(run.getRunMax() > run.getRunAvg() * 1.03){
            SlackManager sm = new SlackManager("YS BadBM");
            sm.postMsg2OurChannel(":astonished: Benchmark run was more than the average run :astonished:");

        }

    }
}
