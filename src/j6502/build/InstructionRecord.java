package j6502.build;

import j6502.*;

import java.util.*;

/**
 * An instruction specification
 */

class InstructionRecord {

    int opcode;
    AddressingMode addrMode;
    int size, clockTicks;
    int exception;

    public static InstructionRecord parse (String s) {
        StringTokenizer st = new StringTokenizer (s, " ");

        InstructionRecord is = new InstructionRecord ();

        if (st.countTokens () != 5) {
            return null;
        }

        String opcodeString = st.nextToken ();

        if (opcodeString.length () != 2) {
            return null;
        }

        try {
            is.opcode = Integer.parseInt (opcodeString, 16);
        } catch (NumberFormatException nfe) {
            return null;
        }

        is.addrMode = AddressingMode.getByName (st.nextToken ());

        if (is.addrMode == null) {
            return null;
        }

        try {
            is.size = Integer.parseInt (st.nextToken ());
            is.clockTicks = Integer.parseInt (st.nextToken ());
            is.exception = Integer.parseInt (st.nextToken ());
        } catch (NumberFormatException nfe) {
            return null;
        }

        return is;
    } // parse

    public String toString () {
        return getClass ().getName () + "["
            + Integer.toHexString (opcode) + ","
            + addrMode.toString () + ","
            + size + ","
            + clockTicks + ","
            + exception  + "]";
    }

} // class InstructionRecord
