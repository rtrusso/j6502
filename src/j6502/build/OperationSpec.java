package j6502.build;

import java.util.*;


/**
 * An operation specification - one record in the cpu
 * specification file.
 */

class OperationSpec {

    String      mnemonic;
    String      description;
    CodeSnippet code;
    Vector      specificInstructions;

    boolean read;
    boolean write;

    public OperationSpec (String name,
                          String desc,
                          CodeSnippet javaCode)
    {
        mnemonic = name;
        this.description = desc;
        code = javaCode;
        specificInstructions = new Vector ();
    }

    public void addInstructionRecord (InstructionRecord is) {
        specificInstructions.add (is);
    }

    public String toString () {
        return getClass ().getName () + "["
            + mnemonic + ","
            + description + ","
            + code.toString () + ","
            + "read:" + read + ","
            + "write:" + write + ","
            + specificInstructions.toString ()
            + "]";
    }

    public InstructionRecord[] getRecords () {
        InstructionRecord[] recs = 
            new InstructionRecord[specificInstructions.size ()];

        int count = 0;
        for (Enumeration e = specificInstructions.elements ();
             e.hasMoreElements (); count++) {
            recs[count] = (InstructionRecord)e.nextElement ();
        }

        return recs;
    } // getRecords

} // class OperationSpec
