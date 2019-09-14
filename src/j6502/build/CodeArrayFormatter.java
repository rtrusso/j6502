package j6502.build;

import java.util.*;
import java.io.*;

class CodeArrayFormatter {

    String[] array;
    int fieldWidth;

    String name;
    String type;

    public CodeArrayFormatter (String name, String type, String[] data) {
        array = data;

        this.name = name;
        this.type = type;

        fieldWidth = getMaxLen ();

    }

    public static CodeArrayFormatter create (String name, String type, int[] data, boolean hex) {

        String[] sdata = new String[data.length];

        for (int count = 0; count < data.length; count++) {
            if (hex) {
                sdata[count] = "0x" + Integer.toHexString (data[count]);
            } else {
                sdata[count] = Integer.toString (data[count]);
            }
        }

        return new CodeArrayFormatter (name, type, sdata);
    }


    private int getMaxLen () {
        int count, maxLen = 0;

        for (count = 0; count < array.length; count++) {
            if (array[count].length () > maxLen) {
                maxLen = array[count].length ();
            }
        }

        return maxLen;
    }

    private String justify (String s) {
        if (s.length () > fieldWidth) {
            throw new Error ();
        }

        StringBuffer buf = new StringBuffer (s);

        while (buf.length () < fieldWidth) {
            buf.insert (0, ' ');
        }

        return buf.toString ();
    }

    public void output (IndentingPrintWriter pw, int numCols) {
        int col = 0;

        boolean first = true;

        pw.println (type + "[] " + name + " = {");

        pw.indent ();

        for (int count = 0; count < array.length; count++) {
            if (first) {
                first = false;
            } else {
                pw.print (", ");
            }
            if (col >= numCols) {
                pw.println ();
                col = 0;
            }

            pw.print (justify (array[count]));
            col++;
        } // count

        pw.unindent ();
        pw.println ();

        pw.println ("};");
    } // output

    public static void main (String[] args) {

        int[] data = new int[256];

        for (int count = 0; count < data.length; count++) {
            data[count] = count;
        }

        IndentingPrintWriter ipw = new IndentingPrintWriter (new
            OutputStreamWriter (System.out));

        ipw.indent ();
        ipw.indent ();

        CodeArrayFormatter caf = CodeArrayFormatter.create ("numbers", "int",
                                                            data, true);
        caf.output (ipw, 8);
        ipw.unindent ();
        ipw.println ("hello");
        ipw.unindent ();
        ipw.println ("goodbye");
    } // main

} // CodeArrayFormatter
