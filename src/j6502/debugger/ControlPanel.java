package j6502.debugger;

import j6502.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ControlPanel extends JPanel
    implements CPUEventListener
{

    public void cpuChanged (CPUEvent ce) {
        clocksField.setText (Integer.toString (cpu.getLastClocks()));
        elapsedClocksField.setText (Integer
                                    .toString (cpu.getElapsedClocks ()));
        regPanel.cpuChanged (ce);
    }

    public void cpuReset (CPUEvent ce) {
    }

    J6502 cpu;

    JTextField clocksField;
    JTextField elapsedClocksField;
    CPUPanel   regPanel;
    
    JButton stepButton;
    JButton undoButton;

    public ControlPanel (J6502 cpu) {
        this.cpu = cpu;

        regPanel = new CPUPanel (cpu);

        clocksField = new JTextField (Integer.toString (cpu.getLastClocks ()));
        clocksField.setEditable (false);
        clocksField.setColumns (8);
        elapsedClocksField = new
            JTextField (Integer.toString (cpu.getElapsedClocks ()));
        elapsedClocksField.setEditable (false);
        elapsedClocksField.setColumns (8);

        stepButton = new JButton ("step");
        undoButton = new JButton ("undo");
        
        JPanel infoPanel = new JPanel ();
        JPanel ctlPanel = new JPanel ();

        ctlPanel.setLayout (new BoxLayout (ctlPanel, BoxLayout.Y_AXIS));

        JLabel ctlPanelLabel = new JLabel ("CPU Controls");
        JPanel ctlPanelLabelPanel = new JPanel ();
        //ctlPanelLabelPanel.setLayout (new BorderLayout ());
        ctlPanelLabelPanel.add (ctlPanelLabel);
        ctlPanel.add (ctlPanelLabelPanel);

        JPanel ctlSubPanel = new JPanel ();
        ctlSubPanel.add (undoButton);
        ctlSubPanel.add (stepButton);

        ctlPanel.add (ctlSubPanel);

        infoPanel.setLayout (new BoxLayout (infoPanel, BoxLayout.Y_AXIS));
        JLabel infoPanelLabel  = new JLabel ("CPU Information");
        JPanel infoPanelLabelPanel = new JPanel ();
        infoPanelLabelPanel.add (infoPanelLabel);
        infoPanel.add (infoPanelLabelPanel);

        JPanel clocksPanel = new JPanel ();

        clocksPanel.setLayout (new GridBagLayout ());
        
        JLabel clocksFieldLabel = new JLabel ("Clocks");
        clocksFieldLabel.setHorizontalAlignment (SwingConstants.RIGHT);
        JLabel elapsedClocksFieldLabel = new JLabel ("Elapsed Clocks");
        elapsedClocksFieldLabel.setHorizontalAlignment
            (SwingConstants.RIGHT);

        GridBagConstraints cFC, cFLC, eFC, eFLC;
        cFC = new GridBagConstraints ();
        cFLC = new GridBagConstraints ();
        eFC = new GridBagConstraints ();
        eFLC = new GridBagConstraints ();

        cFLC.gridx = 0; cFLC.gridy = 0;
        cFC.gridx = 1; cFLC.gridy = 0;

        eFLC.gridx = 0; eFLC.gridy = 1;
        eFC.gridx = 1; eFC.gridy = 1;

        clocksPanel.add (clocksFieldLabel, cFLC);
        clocksPanel.add (clocksField, cFC);
        clocksPanel.add (elapsedClocksFieldLabel, eFLC);
        clocksPanel.add (elapsedClocksField, eFC);

        infoPanel.add (clocksPanel);

        JLabel infoPanelRegLabel = new JLabel ("Registers");
        JPanel infoPanelRegLabelPanel =  new JPanel ();
        infoPanelRegLabelPanel.add (infoPanelRegLabel);
        infoPanel.add (infoPanelRegLabelPanel);

        infoPanel.add (regPanel);

        Box box = new Box (BoxLayout.Y_AXIS);
        setLayout (new BorderLayout ());
        box.add (ctlPanel);
        box.add (infoPanel);
        add (box);
    } // ControlPanel

    public JButton getStepButton () {
        return stepButton;
    }

    public JButton getUndoButton () {
        return undoButton;
    }

} // class ControlPanel
