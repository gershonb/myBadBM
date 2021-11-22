package lcmbadbm;

import edu.touro.mco152.bm.App;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    /**
     * test for targetMarkSizeKb
     */
    @Test
    void targetMarkSizeKb() {
        long target = App.targetMarkSizeKb();
        assertEquals((long) App.blockSizeKb * App.numOfBlocks, target);
    }

    /**
     * Cross checked targetMarkSizeKb with for loop
     */
    @Test
    void crossCheckTesttargetMarkSizeKb(){
        int totalMarkerSize = 0;
        for (int i = 0; i < App.numOfBlocks; i++) {
            totalMarkerSize += App.blockSizeKb;
        }

        assertEquals(totalMarkerSize, App.targetMarkSizeKb());
    }
    /**
     * T - Time targetTxSizeKb for acceptable run time
     */
    @Test
    void targetTxSizeKb() {
        long start = System.currentTimeMillis();
        long runTime = App.targetTxSizeKb();
        long end = System.currentTimeMillis();
        assertEquals(true,end - start < 30);
    }

    /**
     * Test reset data
     */
@Test
void testresetTestData(){
    App.nextMarkNumber = 4;
    App.wAvg = 2;
    App.wMax = 3;
    App.wMin = 4;
    App.rAvg = 4;
    App.rMax = 5;
    App.rMin = 6;
    App.resetTestData();
    double[] values = {App.wAvg, App.wMax, App.wMin, App.rAvg, App.rMax, App.rMin};
    boolean test = true;
    if (App.nextMarkNumber != 1){
        test = false;
    }
    for (Double i: values) {
        if(i != -1){
            test = false;
            break;
        }
    }
    assertEquals(true, test);


}

}
