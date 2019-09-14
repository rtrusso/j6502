package j6502;

// Disassembler.java
// Richard Russo
// 08|10|2000

import java.util.*;
import java.text.*;

/**
 * The Disassembler class provides the minimum functionality to
 * disassemble 6502 instructions into human-readable assembly.
 *
 * @author Richard Russo
 * @version 1.0
 * @since   1.0
 */

public class Disassembler {

    MemoryModel memory;

    /**
     * Create a new Disassembler, which will read instruction from the
     * specified <code>MemoryModel</code>.
     *
     * @since 1.0
     */

    public Disassembler (MemoryModel mem) {
        memory = mem;
    }

    private String formatByte (byte b) {
        StringBuffer buf = new StringBuffer ();
        buf.append (Integer.toHexString (b & 0xff));
        
        while (buf.length () < 2) {
            buf.insert (0, '0');
        }
        return buf.toString ();
    }

    /**
     * Get the disassembled instruction at the specified address in
     * the currently installed <code>MemoryModel</code>.  The branch
     * instructions (bne, beq, bpl, bmi, etc.) are disassembled in a
     * way different from the other instructions.  Rather than
     * disassemble them with the opcode and the signed displacement of
     * the branch, the target address is calculated and that is what
     * is put into the assembly code output.
     *
     * @param address the address to find the opcode of the
     * instruction to be disassembled in the current
     * <code>MemoryModel</code>.
     *
     * @returns a String, which contains the assembly code of the
     * disassembled instruction.  A return value of <code>null</code>
     * indicates that the opcode at the specified address was invalid.
     *
     * @since 1.0
     */

    public String getCode (char address) {
        int opcode = memory.readMemory (address) & 0xff;
        char origAddress = address;

        int size = J6502Constants.sizes[opcode];
        if (size == 0) {
            return null;
        }

        AddressingMode mode = J6502Constants.modes[opcode];
        String[] data;

        if (mode.equals (AddressingMode.JMP_RELATIVE)) {
            address++;
            byte b = memory.readMemory (address);
            
            address = (char)(origAddress + 2 + (int)b);

            byte lsb = (byte)(address & 0xff);
            byte msb = (byte)((address >> 8) & 0xff);
            data = new String[2];
            data[0] = formatByte (lsb);
            data[1] = formatByte (msb);
        } else {
            size--;
            data = new String[size];
            address++;
            for (int count = 0; count < size; count++) {
                data[count] = formatByte (memory.readMemory (address));
                address++;
            }
        }

        String result;

        result = J6502Constants.names[opcode] + " " + MessageFormat
            .format (mode.getPattern (), data);

        return result;
    } // getCode

} // class Disassembler
