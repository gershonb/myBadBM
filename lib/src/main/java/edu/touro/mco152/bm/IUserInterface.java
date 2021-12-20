package edu.touro.mco152.bm;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public interface IUserInterface {
    void DWsetProgress(int i);

    boolean DWisCancelled();

    void DWpublish(DiskMark m);

    void DWaddPropertyChangeListener(PropertyChangeListener prop);

    boolean DWcancel(boolean bool);

    void DWexecute();

}
