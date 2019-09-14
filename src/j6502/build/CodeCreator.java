package j6502.build;

import java.io.*;
import java.util.*;


abstract class CodeCreator {

    protected void printCode (PrintWriter pw, String[] code) {
            
        for (int count = 0; count < code.length; count++) {
            pw.println (code[count]);
        }
    }

    abstract void create ();
}


