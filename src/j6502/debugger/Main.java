package j6502.debugger;

import j6502.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.filechooser.*;

import java.io.*;

public class Main extends JFrame 
    implements CPUEventListener
{


    public void cpuChanged (CPUEvent e) {
    }

    public void cpuReset (CPUEvent e) {
        cpu.setLastClocks (0);
        cpu.setElapsedClocks (0);
        cpu.setRegisters (new J6502Registers ());
        undoInformation.removeAllElements ();
    } // cpuReset

    class CPUState {
        J6502Registers r;
        int clocks;
        int elapsedClocks;
        MemoryPatch patch;
        MemoryPatch lastPatch;

        public CPUState (J6502 cpu) {
            this.r = cpu.getRegisters ();
            clocks = cpu.getLastClocks ();
            elapsedClocks = cpu.getElapsedClocks ();
            
            this.patch = null;
        }

        public void setPatch (MemoryPatch p) {
            this.patch = p;
        }
        
        void restore () {
            patch.apply (itableModel);
            cpu.setRegisters (r);
            cpu.setLastClocks (clocks);
            cpu.setElapsedClocks (elapsedClocks);
        }
    }

    private final static int UNDO_LIMIT = 64;
    Stack undoInformation = new Stack ();

    private void undo () {
        if (undoInformation.size () == 0) {
            mpPanel.update ((MemoryPatch)null);
            itableModel.resetPatch ();
            return;
        }
        CPUState s = (CPUState)undoInformation.pop ();
        mpPanel.update (s.lastPatch);
        lastMemoryPatch = s.lastPatch;
        s.restore ();
        itableModel.resetPatch ();
        fireCPUEvent ();
    }

    Vector cpuEventListeners = new Vector ();

    public void addCPUEventListener (CPUEventListener cpuel) {

        synchronized (this) {
            cpuEventListeners.add (cpuel);
        }
    }

    public void removeCPUEventListener (CPUEventListener cpuel) {
        synchronized (this) {
            cpuEventListeners.remove (cpuel);
        }
    }

    public void setPC (char addr) {
        cpu.setPC (addr);
        fireCPUEvent ();
    }

    private void fireCPUEvent () {
        Vector listeners = null;
        synchronized (this) {
            listeners = (Vector)cpuEventListeners.clone ();
        }

        CPUEvent cpue = new CPUEvent (this);

        Enumeration e = listeners.elements ();
        while (e.hasMoreElements ()) {
            CPUEventListener cpuel = (CPUEventListener)e.nextElement ();

            cpuel.cpuChanged (cpue);
        }
    } // fireCPUEvent 

    private void fireCPUEvent_reset () {
        Vector listeners = null;
        synchronized (this) {
            listeners = (Vector)cpuEventListeners.clone ();
        }

        CPUEvent cpue = new CPUEvent (this);

        Enumeration e = listeners.elements ();
        while (e.hasMoreElements ()) {
            CPUEventListener l = (CPUEventListener)e.nextElement ();
            l.cpuReset (cpue);
        }
    } // fireCPUEvent_reset

    public static final String VERSION = "0.1";

    J6502       cpu;
    MemoryModel mem;

    ITableModel itableModel;
    MemoryTable table;
    JScrollPane tablePane;

    MemoryPatchPanel mpPanel;

    Box         mainPanel;

    //JButton     stepButton;

    //CPUPanel    cpuPanel;

    class JFrameMonitor extends WindowAdapter 
        implements ActionListener {

        String origText;

        public void windowClosing (WindowEvent we) {
            source.setText (origText);
            hidden = true;
        }

        boolean hidden = true;


        public void actionPerformed (ActionEvent ae) {

            if (hidden) {
                hidden = false;
                origText = source.getText ();
                source.setText ("hide " + origText);
                frame.show ();
            } else {
                hidden = true;
                source.setText (origText);
                frame.hide ();
            }
        }
        JFrame frame;
        JButton source;

        public JFrameMonitor (JButton source, JFrame frame) {
            this.frame = frame;
            this.source = source;

            frame.addWindowListener (this);
            source.addActionListener (this);
            origText = source.getText ();
        }
    } // class JFrameMonitor

    JMenuBar makeMenuBar () {
        JMenuBar menuBar = new JMenuBar ();

        JMenu mainMenu = new JMenu ("Main");

        JMenuItem quitItem = new JMenuItem ("quit");
        JMenuItem resetItem = new JMenuItem ("reset");
        JMenuItem openItem = new JMenuItem ("open ...");

        openItem.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {

                    String errorMessage = null;

                    JFileChooser chooser = new JFileChooser (".");

                    chooser.setFileFilter (new 
                        javax.swing.filechooser.FileFilter () {
                            public boolean accept (File file) {

                                if (file.isDirectory ()) {
                                    return true;
                                }
                                String name = file.getName ();
                                return name.endsWith (".o")
                                    || name.equalsIgnoreCase ("a.out");
                            }
                            
                            public String getDescription () {
                                return "Object code - *.o,a.out";
                            }
                        });

                    int retVal = chooser.showOpenDialog (mainFrame);

                    if (retVal != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    File f = chooser.getSelectedFile ();

                    if (!f.exists ()) {
                        errorMessage = "file " + f.getName ()
                            + " not found";
                    }

                    if ((null == errorMessage) && (f.isDirectory ())) {
                        errorMessage = f.getName () + " is a directory";
                    }

                    if ((null == errorMessage) && (!f.canRead ())) {
                        errorMessage = "file: " + f.getName ()
                            + ": no read permission";
                    }

                    int newPC = -1;
                    if (null == errorMessage) {
                        try {
                            newPC = itableModel.load (f);
                        } catch (Exception e) {
                            errorMessage = e.getMessage ();
                        }
                    }

                    if (null != errorMessage) {
                        ErrorDialog err = new ErrorDialog (mainFrame,
                                                           "Error:\n\n"
                                                           + errorMessage);
                        return;
                    }

                    fireCPUEvent_reset ();
                    cpu.setPC ((char)newPC);
                    fireCPUEvent ();

                    //System.out.println (retVal);
                }
            });

        resetItem.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    cpu.reset ();
                    fireCPUEvent_reset ();
                    fireCPUEvent ();
                }
            });

        quitItem.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    System.exit (0);
                }
            });

        mainMenu.add (openItem);
        mainMenu.add (resetItem);
        mainMenu.addSeparator ();
        mainMenu.add (quitItem);

        menuBar.add (mainMenu);

        return menuBar;
    }

    //JButton regFrameButton;
    //JFrame  regFrame;

    JFrame mainFrame;

    MemoryPatch lastMemoryPatch = null;

    public Main () {
        super ("J6502 debugger " + VERSION);
        mainFrame = this;

        setJMenuBar (makeMenuBar ());

        ITableModel model = new ITableModel ();
        itableModel = model;
        mem = model;

        /**
        itableModel.writeMemory ((char)2, (byte)0xfe);
        itableModel.writeMemory ((char)5, (byte)0x4c);
        itableModel.writeMemory ((char)6, (byte)0x02);
        itableModel.writeMemory ((char)7, (byte)0x10);
        */
        
        cpu = new J6502 (model);

        table = new MemoryTable (cpu, itableModel, this);

        table.addMouseListener (new MouseAdapter () {
                public void mouseFlicked (MouseEvent me) {

                    if ((me.getModifiers () & InputEvent.BUTTON1_MASK)
                        == 0) return;

                    Point p = me.getPoint ();
                    int row = table.rowAtPoint (p);
                    int col = table.columnAtPoint (p);

                    if ((row < 0) || (row >= table.getRowCount ())) {
                        row = table.getSelectedRow ();
                    }

                    cpu.setPC (table.addressOfRow (row));
                    fireCPUEvent ();
                }

                public void mousePressed (MouseEvent me) {
                    mouseFlicked (me);
                }

                public void mouseReleased (MouseEvent me) {
                    mouseFlicked (me);
                }

            });

        tablePane = new JScrollPane (table);

        mainPanel = new Box (BoxLayout.X_AXIS);
        Box vBox = new Box (BoxLayout.Y_AXIS);

        table.sizeColumnsToFit (JTable.AUTO_RESIZE_ALL_COLUMNS);

        ControlPanel cp = new ControlPanel (cpu);

        addCPUEventListener (this);
        addCPUEventListener (itableModel);
        addCPUEventListener (table);
        addCPUEventListener (cp);

        cp.getStepButton ().addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    CPUState s = new CPUState (cpu);
                    cpu.execute ();
                    MemoryPatch mp = itableModel.getMemoryPatch ();
                    mpPanel.update (mp);
                    s.setPatch (mp);
                    s.lastPatch = lastMemoryPatch;
                    lastMemoryPatch = mp;

                    if (undoInformation.size () >= UNDO_LIMIT) {
                        undoInformation.remove (0);
                    }

                    undoInformation.push (s);
                    fireCPUEvent ();
                }
            });

        cp.getUndoButton ().addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    undo ();
                }
            });;

        vBox.add (cp);

        mpPanel = new MemoryPatchPanel ();

        vBox.add (mpPanel);

        mainPanel.add (tablePane);
        mainPanel.add (vBox);

        getContentPane ().add (mainPanel);
        pack ();
    }

    public static void main (String[] args) {
        Main m = new Main ();

        m.addWindowListener (new WindowAdapter () {
                public void windowClosing (WindowEvent we) {
                    System.exit (0);
                }
            });

        m.show ();
    }
} // class Main
