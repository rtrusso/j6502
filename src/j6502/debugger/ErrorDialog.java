package j6502.debugger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ErrorDialog extends JDialog {

    JTextArea text;

    public ErrorDialog (JFrame parent, String message) {
        super (parent, "Error", true);

        text = new JTextArea (message);
        text.setEditable (false);
        text.setEnabled (false);
        text.setDisabledTextColor (Color.black);

        JButton button = new JButton ("ok");
        button.addActionListener (new ActionListener () {
                public void actionPerformed (ActionEvent ae) {
                    setVisible (false);
                }
            });

        getContentPane ().add (text, "Center");
        getContentPane ().add (button, "South");
        pack ();
        setVisible (true);
    }

} // class ErrorDialog
