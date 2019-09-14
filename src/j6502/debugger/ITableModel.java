package j6502.debugger;

import j6502.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.io.*;

class ITableModel extends AbstractTableModel
    implements MemoryModel, CPUEventListener {

    public void cpuChanged (CPUEvent cpe) {
    }

    public void cpuReset (CPUEvent cpe) {
        resetPatch ();
    }

    public void resetPatch () {
        memPatch = new MemoryPatch ();
    }

    MemoryPatch memPatch = new MemoryPatch ();

    public int load (File f) 
        throws Exception
    {
        DataInputStream in = new DataInputStream
            (new FileInputStream (f));

        if (in.available () < 2) {
            throw new Exception ("file is truncated");
        }

        int pc;

        pc = in.readUnsignedByte ();
        pc += (in.readUnsignedByte () << 8);

        byte[] objectCode = new byte[in.available ()];

        if ((pc + objectCode.length >= memory.length)) {
            throw new Exception ("file size: " + objectCode.length
                                 + " is too big to be loaded at "
                                 + " address " + ((int)pc));
        }

        in.read (objectCode, 0, objectCode.length);

        java.util.Arrays.fill (memory, (byte)0);
        System.arraycopy (objectCode, 0, memory, pc, objectCode.length);
        makeCode ();
        table.tableChanged (new TableModelEvent (this));
        return pc;
    }

    JTable table = null;

    public void setTable (JTable t) {
        table = t;
    }

    public ITableModel () {
        makeCode ();
    }

    public static final int PAGE_SIZE = 256;
    public static final int NUM_PAGES = 256;

    int currentPage = 0;

    byte[] memory = new byte[65536];

    public int getSize () {
        return memory.length;
    }

    public char addressOfRow (int row) {
        if ((row < 0) || (row >= PAGE_SIZE)) {
            throw new IllegalArgumentException ();
        }

        return (char)(getPage () * PAGE_SIZE + row);
    }

    public int rowOfAddress (char addr) {
        int page = addr / PAGE_SIZE;
        if (page != getPage ()) {
            return -1;
        }

        return addr % PAGE_SIZE;
    }

    public int pageOfAddress (char addr) {
        return addr / PAGE_SIZE;
    }

    public int getPage () {
        return currentPage;
    }

    private final static String EMPTY_STRING = "";

    final String[] code = new String[PAGE_SIZE];

    public void updateCode (char pc) {
        int startRow = rowOfAddress (pc);
        makeCode (startRow);
        table.tableChanged (new TableModelEvent (this));
    }

    public void updateCode () {
        makeCode ();
        table.tableChanged (new TableModelEvent (this));
    }

    private void makeCode () {
        makeCode (0);
    }

    private void makeCode (int startOff) {
        char baseAddress = addressOfRow (0);
        char off = 0;

        if ((startOff < 0) || (startOff > 255)) {
            throw new IllegalArgumentException ();
        }

        for (int count = 0; count < startOff; count++) {
            code[count] = EMPTY_STRING;
        }
        off = (char)(startOff);

        Disassembler d = new Disassembler (this);

        while (off < 256) {
            int opcode = readMemory ((char)(off + baseAddress)) & 0xff;
            int size = J6502Constants.sizes[opcode];

            if (0 == size) {
                code[off] = EMPTY_STRING;
                off++;
            } else {
                code[off] = d.getCode ((char)(baseAddress + off));
                for (int count = 1; (count < size) && (off < 255); count++) {
                    off++;
                    code[off] = EMPTY_STRING;
                }
                off++;
            }
        }
    } // makeCode
                

    public void setPage (int page) {
        if ((page < 0) || (page >= PAGE_SIZE)) {
            throw new IllegalArgumentException ();
        }

        if (page != currentPage) {
            currentPage = page;
            table.tableChanged (new 
                TableModelEvent (this, 0, getRowCount () - 1));
            makeCode ();
        }
    }
    
    public void reset () {
        java.util.Arrays.fill (memory, (byte)0);
    }
 
    public byte readMemory (char address) {
        return memory[address];
    }

    public void writeMemory (char address, byte data) {

        memPatch.add (address, readMemory (address), data);

        int page = address / PAGE_SIZE;

        if (page == currentPage) {
            int row = address % PAGE_SIZE;
            if (table != null) {
                table.tableChanged (new TableModelEvent (this, row, row));
            }
        }

        memory[address] = data;
    }

    public MemoryPatch getMemoryPatch () {
        MemoryPatch val = memPatch;
        resetPatch ();
        return val;
    }

    public String getColumnName (int idx) {

        switch (idx) {
        case 0:
            return "address";
        case 1:
            return "memory";
        case 2:
            return "instruction";
        }
        return null;
    }

    public int getRowCount () {
        return PAGE_SIZE;
    }

    public int getColumnCount () {
        return 3;
    }

    String formatByte (byte b) {
        StringBuffer buf = new StringBuffer (10);
        buf.append (Integer.toHexString (b & 0xff));

        while (buf.length () < 2) {
            buf.insert (0, '0');
        }

        buf.insert (0, "0x");
        return buf.toString ();
    }

    String formatWord (char c) {
        StringBuffer buf = new StringBuffer (10);
        buf.append (Integer.toHexString (c & 0xffff));

        while (buf.length () < 4) {
            buf.insert (0, '0');
        }
        buf.insert (0, "0x");
        return buf.toString ();
    }

    public Object getValueAt (int row, int col) {
        switch (col) {
        case 0:
            return formatWord ((char)(getPage () * PAGE_SIZE + row));
        case 1:
            return formatByte (memory[currentPage * PAGE_SIZE + row]);
        case 2:
            return code[row];
        }
        return null;
    }

} // class ITableModel
