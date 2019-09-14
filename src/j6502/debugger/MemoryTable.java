package j6502.debugger;

import j6502.*;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class MemoryTable extends JTable 
    implements CPUEventListener
{

    class PageSelector extends JFrame {

        Box box;
        JTextField pageField;
        JTextField status;
        JSlider    pageSlider;
        JButton    set;

        final static String readyText = "press `set' to display";

        public PageSelector () {
            super ("Select memory page");

            box = new Box (BoxLayout.Y_AXIS);

            pageField = new JTextField (Integer.toString (model.getPage ()));
            status = new JTextField (readyText);
            status.setEnabled (false);
            status.setEditable (false);
            set = new JButton ("set");
            pageSlider = new JSlider (0, 255, model.getPage ());

            pageField.addActionListener (new ActionListener () {
                    public void actionPerformed (ActionEvent ae) {
                        int value = -1;
                        boolean valid = true;
                        String text = pageField.getText ();
                        Integer i;

                        try {
                            i = Integer.decode (text);
                        } catch (NumberFormatException nfe) {
                            i = null;
                        }

                        if (i != null) {
                            value = i.intValue ();
                            if ((value < 0) || (value > 255)) {
                                valid = false;
                            }
                        } else {
                            valid = false;
                        }

                        if (!valid) {
                            valid = false;
                            status.setText ("invalid page number");
                            return;
                        }

                        pageSlider.setValue (value);
                    }
                });

            pageSlider.addChangeListener (new ChangeListener () {
                    public void stateChanged (ChangeEvent ce) {
                        String s = "0x" + Integer.toHexString
                            (pageSlider.getValue ());
                        pageField.setText (s);
                        status.setText (readyText);
                    }
                });

            set.addActionListener (new ActionListener () {
                    public void actionPerformed (ActionEvent ae) {
                        int value = -1;
                        boolean valid = true;
                        Integer i;
                        try {
                            i = Integer.decode (pageField.getText ());
                        } catch (NumberFormatException nfe) {
                            i = null;
                        }

                        if (null != i) {
                            valid = true;
                            value = i.intValue ();

                            if ((value < 0) || (value > 255)) {
                                valid = false;
                            }
                        } else {
                            valid = false;
                        }
                        if (!valid) {
                            status.setText ("invalid page number");
                            return;
                        }
                        setPage (value);
                        status.setText (readyText);
                    }
                });

            box.add (pageField);
            Box b = new Box (BoxLayout.X_AXIS);
            b.add (pageSlider);
            b.add (set);
            box.add (b);
            box.add (status);
            getContentPane ().add (box);
            pack ();
        }
    } // class PageSelector

    PageSelector selector;

    ITableModel model;
    PrivateListSelectionModel selectionModel;

    J6502 cpu;

    public void cpuChanged (CPUEvent ce) {

        if (model.getPage () != model.pageOfAddress (cpu.getPC ())) {
            model.setPage (model.pageOfAddress (cpu.getPC ()));
        }

        int row = model.rowOfAddress (cpu.getPC ());

        if (row >= 0) {
            selectionModel.privateSetSelectionInterval (row, row);
            Rectangle r = getCellRect (row, 0, true);
            scrollRectToVisible (r);
        }
    } // cpuChanged

    public void cpuReset (CPUEvent ce) {
    }

    private void setPage (int page) {
        //int page = (model.getPage () + 1) % model.NUM_PAGES;

        int row = model.rowOfAddress (cpu.getPC ());

        model.setPage (page);
        mainProgram.setPC (model.addressOfRow (row));

        //selectionModel.privateSetSelectionInterval (row, row);
    }

    private void nextPage () {
        int page = (model.getPage () + 1) % model.NUM_PAGES;
        setPage (page);
    }

    private void prevPage () {
        int page = model.getPage () - 1;
        if (page < 0) page += model.NUM_PAGES;
        setPage (page);
    }

    JPopupMenu popupMenu;

    JTable theTable;

    JPopupMenu makePopupMenu () {
        JPopupMenu menu = new JPopupMenu ("Memory Operations");

        JMenuItem dis = new JMenuItem ("disassemble");
        dis.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    model.updateCode ();
                    cpuChanged (null);
                }
            });

        JMenuItem disAtPC = new JMenuItem ("disassemble at pc");
        disAtPC.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    model.updateCode (cpu.getPC ());
                    cpuChanged (null);
                }
            });

        JMenuItem prevItem = new JMenuItem ("previous page");
        JMenuItem nextItem = new JMenuItem ("next page");
        JMenuItem selItem  = new JMenuItem ("page ...");

        prevItem.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    prevPage ();
                }
            });

        nextItem.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    nextPage ();
                }
            });

        selItem.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    selector.setVisible (true);
                }
            });

        menu.add (dis);
        menu.add (disAtPC);
        menu.addSeparator ();
        menu.add (prevItem);
        menu.add (nextItem);
        menu.addSeparator ();
        menu.add (selItem);

        return menu;
    }
    
    Main mainProgram;

    public MemoryTable (J6502 cpu, ITableModel model, Main prog) {
        super (model, null, new PrivateListSelectionModel ());
        this.cpu = cpu;
        this.model = model;
        this.selectionModel = (PrivateListSelectionModel)getSelectionModel ();
        this.mainProgram = prog;

        theTable = this;

        setSelectionForeground (Color.white);
        setSelectionBackground (Color.red);

        selector = new PageSelector ();

        model.setTable (this);

        setToolTipText ("current memory page, right click for options");

        popupMenu = makePopupMenu ();

        addMouseListener (new MouseAdapter () {

                int likedButtons = InputEvent.BUTTON2_MASK |
                    InputEvent.BUTTON3_MASK;

                public void mousePressed (MouseEvent me) {
                    if ((me.getModifiers () & likedButtons)
                        == 0) return;

                    Point p = me.getPoint ();
                    popupMenu.show (theTable, p.x, p.y);
                }
            });

        cpuChanged (null);
    }

    public char addressOfRow (int row) {
        return model.addressOfRow (row);
    }
}
