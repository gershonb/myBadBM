package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

import static edu.touro.mco152.bm.App.KILOBYTE;
import static edu.touro.mco152.bm.App.blockSizeKb;

public class Executor implements IExecutor {
    DiskWorker dw;
    public static final int KILOBYTE = 1024;

    public boolean readTest = false;
    public boolean writeTest = true;
    public int numOfMarks = 25;      // desired number of marks
    public int numOfBlocks = 32;
    public int blockSizeKb = 512;
    int blockSize = blockSizeKb * KILOBYTE;

    //Sequence of IO operations. (random vs. seq.)
    public enum BlockSequence {SEQUENTIAL, RANDOM}

    public DiskRun.BlockSequence blockSequence = DiskRun.BlockSequence.SEQUENTIAL;

    public Executor(DiskWorker dw, boolean readTest, boolean writeTest, int numOfMarks, int numOfBlocks, int blockSizeKb, DiskRun.BlockSequence blockSequence) {
        this.dw = dw;
        this.readTest = readTest;
        this.writeTest = writeTest;
        this.numOfMarks = numOfMarks;
        this.numOfBlocks = numOfBlocks;
        this.blockSizeKb = blockSizeKb;
        this.blockSequence = blockSequence;
    }

    public Executor() {

    }


    @Override
    public void DoReads() {
        DWReads reader = new DWReads(readTest, writeTest, numOfMarks, numOfBlocks, blockSizeKb, blockSequence);
        reader.read(dw);
    }

    @Override
    public void DoWrites() {
        DWWrites writer = new DWWrites(readTest, writeTest, numOfMarks, numOfBlocks, blockSizeKb, blockSequence);
        writer.write(this.dw);
        }

//////////////////////////////////////////////////////////////


        public DiskWorker getDw () {
            return dw;
        }

        public void setDw (DiskWorker dw){
            this.dw = dw;
        }

        public static int getKILOBYTE () {
            return KILOBYTE;
        }

        public boolean isReadTest () {
            return readTest;
        }

        public void setReadTest ( boolean readTest){
            this.readTest = readTest;
        }

        public boolean isWriteTest () {
            return writeTest;
        }

        public void setWriteTest ( boolean writeTest){
            this.writeTest = writeTest;
        }

        public int getNumOfMarks () {
            return numOfMarks;
        }

        public void setNumOfMarks ( int numOfMarks){
            this.numOfMarks = numOfMarks;
        }

        public int getNumOfBlocks () {
            return numOfBlocks;
        }

        public void setNumOfBlocks ( int numOfBlocks){
            this.numOfBlocks = numOfBlocks;
        }

        public int getBlockSizeKb () {
            return blockSizeKb;
        }

        public void setBlockSizeKb ( int blockSizeKb){
            this.blockSizeKb = blockSizeKb;
        }

        public int getBlockSize () {
            return blockSize;
        }

        public void setBlockSize ( int blockSize){
            this.blockSize = blockSize;
        }

        public DiskRun.BlockSequence getBlockSequence () {
            return blockSequence;
        }

        public void setBlockSequence (DiskRun.BlockSequence blockSequence){
            this.blockSequence = blockSequence;
        }
    }
