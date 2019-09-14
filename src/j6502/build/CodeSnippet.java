package j6502.build;

import java.util.*;

/**
 * One of the code snippets that specifies the
 * behavior of an instruction (as seen in the 6502.txt
 * cpu spec file).
 */

class CodeSnippet {

    Vector linesOfCode;

    CodeSnippet () {
        linesOfCode = new Vector ();
    }

    public void addCodeLine (String line) {
        linesOfCode.add (line);
    }

    public String[] getCode () {
        String[] tmp = new String[linesOfCode.size ()];

        Enumeration e = linesOfCode.elements ();
        for (int count = 0; e.hasMoreElements (); count++) {
            tmp[count] = new String ((String)e.nextElement ());
        }

        return tmp;
    }

    public String toString () {
        return getClass ().getName () + "["
            + linesOfCode.toString () + "]";
    }
} // class CodeSnippet
