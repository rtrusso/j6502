package j6502.debugger;

import javax.swing.*;

public class PrivateListSelectionModel 
    extends DefaultListSelectionModel {

    public PrivateListSelectionModel () {
        super ();
        setSelectionMode (SINGLE_SELECTION);
    }

    public void setSelectionInterval (int index0, int index1) {
        return;
    }

    public void privateSetSelectionInterval (int index0, int index1) {
        super.setSelectionInterval (index0, index1);
    }

} // class PrivateListSelectionModel
