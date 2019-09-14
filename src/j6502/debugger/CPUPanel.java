package j6502.debugger;

import j6502.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CPUPanel extends JPanel
    implements CPUEventListener
{

    public void cpuChanged (CPUEvent cpue) {
        update ();
    }

    public void cpuReset (CPUEvent cpue) {
    }

    //Box labels = new Box (BoxLayout.Y_AXIS);
    //Box fields = new Box (BoxLayout.Y_AXIS);

    JPanel me;

    int numLabeledFields = 0;

    class LabeledField {
        JLabel label;
        JTextField field;

        public LabeledField (String labelText,
                             String fieldText) {
            label = new JLabel (labelText);
            field = new JTextField (fieldText);
            field.setEditable (false);
            field.setEnabled (false);
            field.setDisabledTextColor (Color.black);

            label.setHorizontalAlignment (SwingConstants.RIGHT);

            field.addMouseListener (popupMenuListener);
            field.setColumns (16);

            //labels.add (label);
            //fields.add (field);
            
            GridBagConstraints labelC = new GridBagConstraints ();
            labelC.gridx = 0;
            labelC.gridy = numLabeledFields;
            labelC.fill = GridBagConstraints.BOTH;
            me.add (label, labelC);

            GridBagConstraints fieldC = new GridBagConstraints ();
            fieldC.gridx = 1;
            fieldC.gridy = numLabeledFields;
            fieldC.fill = GridBagConstraints.BOTH;
            me.add (field, fieldC);

            numLabeledFields++;

            /**
            add (label);
            add (field);
            */
        }

        public JLabel getLabel () {
            return label;
        }

        public JTextField getField () {
            return field;
        }

        public void update (String s) {
            field.setText (s);
        }
    } // class LabeledField

    Box mainPanel;
    J6502 cpu;

    LabeledField a, x, y, sp, p, pc;

    final void pad (StringBuffer buf, int width) {
        while (buf.length () < width) {
            buf.insert (0, '0');
        }
    }

    final String formatHexField (int i , int width) {
        StringBuffer buf = new StringBuffer ();
        buf.append (Integer.toHexString (i));

        pad (buf, width);
        buf.insert (0, "0x");
        return buf.toString ();
    }

    final String formatBinaryField (int i, int width) {
        StringBuffer buf = new StringBuffer ();
        buf.append (Integer.toBinaryString (i));
        pad (buf, width);
        return buf.toString ();
    }

    boolean binaryMode = true;

    String formatByte (byte b) {
        if (binaryMode) {
            return formatBinaryField (b & 0xff, 8);
        }
        return formatHexField (b & 0xff, 2);
    }

    String formatWord (char c) {
        if (binaryMode) {
            return formatBinaryField (c & 0xffff, 16);
        }
        return formatHexField (c & 0xffff, 4);
    }
    
    ActionListener toHex = new ActionListener () {
            public void actionPerformed (ActionEvent ae) {
                if (!binaryMode) return;
                
                binaryMode = false;
                update ();
            }
        };

    ActionListener toBinary = new ActionListener () {
            public void actionPerformed (ActionEvent ae) {
                if (binaryMode) return;

                binaryMode = true;
                update ();
            }
        };

    public JPopupMenu makePopupMenu () {
        JPopupMenu popupMenu = new JPopupMenu ("Register Format");

        JMenuItem hex = new JMenuItem ("hexadecimal");
        JMenuItem bin = new JMenuItem ("binary");

        hex.addActionListener (toHex);
        bin.addActionListener (toBinary);

        popupMenu.add (hex);
        popupMenu.add (bin);
        return popupMenu;
    }

    public void update () {
        a.update (formatByte (cpu.getAccumulator ()));
        x.update (formatByte (cpu.getX ()));
        y.update (formatByte (cpu.getY ()));
        sp.update (formatByte (cpu.getSP ()));
        p.update (formatByte (cpu.getP ()));
        pc.update (formatWord (cpu.getPC ()));
    }

    GridLayout grid;

    JPanel thePanel;

    JPopupMenu popupMenu;

    MouseListener popupMenuListener = new MouseAdapter () {

            int buttonsLiked = InputEvent.BUTTON2_MASK
                | InputEvent.BUTTON3_MASK;

            public void mousePressed (MouseEvent event) {
                if ((event.getModifiers () & buttonsLiked)
                    == 0) return;
                Point p = event.getPoint ();
                popupMenu.show ((JComponent)event.getSource (), p.x, p.y);
            }
        };
    
    public CPUPanel (J6502 cpu) {
        super ();

        me = this;
        thePanel = this;

        //grid = new GridLayout (6, 2);

        setLayout (new GridBagLayout ());

        this.cpu = cpu;

        //mainPanel = new Box (BoxLayout.Y_AXIS);

        a = new LabeledField ("A", "");
        a.getField ().setToolTipText ("accumulator");
        x = new LabeledField ("X", "");
        x.getField ().setToolTipText ("x index register");
        y = new LabeledField ("Y", "");
        y.getField ().setToolTipText ("y index register");
        sp = new LabeledField ("SP", "");
        sp.getField ().setToolTipText ("stack pointer");
        p = new LabeledField ("P", "");
        p.getField ().setToolTipText ("status register");
        pc = new LabeledField ("PC", "");
        pc.getField ().setToolTipText ("program counter");

        popupMenu = makePopupMenu ();

        addMouseListener (popupMenuListener);
        setToolTipText ("right click for options");
        /**
        add (a);
        add (x);
        add (y);
        add (sp);
        add (p);
        add (pc);
        */

        //add (labels);
        //add (fields);
        update ();
    }

} // class CPUPanel

