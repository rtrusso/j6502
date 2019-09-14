package j6502.debugger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class MemoryPatchPanel extends JPanel {

    MemoryPatch mp = null;

    JScrollPane tablePane;
    JTable table = null;
    MPModel model;

    public MemoryPatch getMemoryPatch () {
        return mp;
    }

    void setTable (JTable t) {
        this.table = t;
    }

    void update (MemoryPatch mp) {
        this.mp = mp;

        table.tableChanged (new TableModelEvent (model));
    } // update

    class MPModel extends AbstractTableModel {

        public MPModel () {

        } // MPModel

        public int getColumnCount () {
            return 3;
        }

        public String getColumnName (int col) {
            switch (col) {
            case 0:
                return "address";
            case 1:
                return "old value";
            case 2:
                return "new value";
            default:
                throw new Error ();
            }
        } // getColumnName

        public int getRowCount () {
            if (null == getMemoryPatch ()) {
                return 0;
            }

            return getMemoryPatch ().size ();
        }

        String format (int i, int width) {
            StringBuffer buf = new StringBuffer ();
            buf.append (Integer.toHexString (i));

            while (buf.length () < width) {
                buf.insert (0, '0');
            }
            return buf.toString ();
        }

        String formatByte (byte b) {
            return "$" + format (b & 0xff, 2);
        }

        String formatWord (char c) {
            return "0x" + format (c, 4);
        }

        public Object getValueAt (int row, int col) {
            switch (col) {
            case 0:
                return formatWord (mp.get (row).getAddress ());
            case 1:
                return formatByte (mp.get (row).getValue ());
            case 2:
                return formatByte (mp.get (row).getNewValue ());
            default:
                throw new Error ();
            }
        }
    } // class MPModel

    public MemoryPatchPanel () {

        mp    = null;
        model = new MPModel ();

        table = new JTable (model);
        tablePane = new JScrollPane (table);
        add (tablePane);

        table.setToolTipText ("Memory that was written to");
        table.setPreferredSize (new Dimension (250, 80));
        tablePane.setPreferredSize (new Dimension (250, 80));
        setPreferredSize (new Dimension (250, 80));
    } // MemoryPatchPanel

} // class MemoryPatchPanel
