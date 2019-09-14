package j6502.build;

import java.io.*;
import java.util.*;

public class J6502ConstantsCreator 
    extends CodeCreator
{

    final static String fileName = "J6502Constants.java";

    final static String[] preCode = {
        "package j6502;",
        "",
        "public interface J6502Constants {",
        ""
    };

    final static String[] postCode = {
        "} // interface J6502Constants"
    };

    IndentingPrintWriter ipw;

    InstructionSpec[] set;

    public J6502ConstantsCreator (PrintStream ps,
                                  InstructionSpec[] set) {
        ipw = new IndentingPrintWriter (new
            OutputStreamWriter (ps));

        if (set.length != 256) {
            throw new Error ();
        }

        this.set = set;
    }

    private int[] getSizes () {
        int[] sizes = new int[set.length];

        for (int count = 0; count < set.length; count++) {
            if (set[count] == null) {
                sizes[count] = 0;
            } else {
                sizes[count] = set[count].rec.size;
            }
        }

        return sizes;
    }

    private int[] getTicks () {
        int[] ticks = new int[set.length];

        for (int count = 0; count < set.length; count++) {
            if (set[count] == null) {
                ticks[count] = 0;
            } else {
                ticks[count] = set[count].rec.clockTicks;
            }
        }
        return ticks;
    }

    private int[] getExceptions () {
        int[] exceptions = new int[set.length];

        for (int count = 0; count < set.length; count++) {
            if (set[count] == null) {
                exceptions[count] = -1;
            } else {
                exceptions[count] = set[count].rec.exception;
            }
        }

        return exceptions;
    }

    private String[] getMnemonics () {
        String[] mnemonics = new String[set.length];

        String dq = "\"";

        for (int count = 0; count < set.length; count++) {
            if (set[count] == null) {
                mnemonics[count] = dq + dq;
            } else {
                mnemonics[count] = dq + set[count].mnemonic + dq;
            }
        }

        return mnemonics;
    }

    private String[] getModes () {
        String[] modes = new String[set.length];

        for (int count = 0; count < set.length; count++) {
            if (set[count] == null) {
                modes[count] = "null";
            } else {
                modes[count] = "AddressingMode." + set[count].rec.addrMode.toString ();
            }
        }

        return modes;
    }

    private void printCode (String[] code) {
        printCode (ipw, code);
    }

    public void create () {

        printCode (preCode);

        ipw.indent ();
        
        String[] sizesComment = {
            "/**",
            " * This array, at index i, gives the size of the instruction in bytes",
            " * for opcode i.  If i is not a valid opcode, the value is 0.",
            " */",
            ""
        };

        printCode (sizesComment);

        CodeArrayFormatter caf = CodeArrayFormatter.create ("sizes", "int",
                                                            getSizes (), false);

        caf.output (ipw, 8);
        ipw.println ();

        String[] clocksComment = {
            "/**",
            " * This array, at index i, gives the number of clock cycles elapsed in",
            " * executing opcode i.  There are exceptions to this rule.  See the ",
            " * exceptions table for a description.",
            " *",
            " */",
            ""
        };

        caf = CodeArrayFormatter.create ("clocks", "int",
                                         getTicks (), false);

        caf.output (ipw, 8);
        ipw.println ();

        String[] exceptionsComment = {
            "/**",
            " * This array, at index i, gives a value which encodes any exceptions",
            " * to the clocks table for opcode i.  A 0 value indicates a bad opcode",
            " *",
            " * If the value is 0, there is no exception.  ",
            " * <p>",
            " *",
            " * If the value is 1, then the addressing mode is indexed, and when",
            " * the index crosses a page boundary, 1 should be added to the value",
            " * in the clocks table to get the correct number of clock cycles.",
            " *",
            " * </p>",
            " * <p>",
            " *",
            " * If the value is 2, then this is a branch instruction.  Add 1 to the",
            " * number of clock cycles if the branch is to the same page.  Add 2 if",
            " * the branch is to a different page.",
            " */",
            ""
        };

        printCode (exceptionsComment);
            
        caf = CodeArrayFormatter.create ("exceptions", "int",
                                         getExceptions (), false);

        caf.output (ipw, 8);
        ipw.println ();

        String[] namesComment = {
            "/**",
            " * This array, at index i, gives the mnemonic for the opcode i.",
            " * An empty string indicates a bad opcode.",
            " */",
            ""
        };

        printCode (namesComment);

        caf = new CodeArrayFormatter ("names", "String",
                                      getMnemonics ());
        caf.output (ipw, 8);
        ipw.println ();

        String[] modesComment = {
            "/**",
            " * This array, at index i, gives the <code>AddressingMode</code>",
            " * object which represents the addressing mode of opcode i.",
            " * <code>null</code> indicates a bad opcode.",
            " *",
            " */",
            ""
        };

        printCode (modesComment);

        caf = new CodeArrayFormatter ("modes", "AddressingMode",
                                      getModes ());
        caf.output (ipw, 2);
        ipw.println ();

        ipw.unindent ();
        printCode (postCode);
    }

    public static void main (String[] args) 
        throws Exception
    {

        FileInputStream f = new FileInputStream ("6502.txt");
        CPUSpecReader reader = new CPUSpecReader (f);

        OperationSpec[] ospecs = reader.readAllOperationSpecs ();

        InstructionSpec[] specs = 
            InstructionSpec.makeInstructionSpecSet (ospecs);

        FileOutputStream fout = new FileOutputStream (fileName);

        J6502ConstantsCreator jcc = new J6502ConstantsCreator (new PrintStream (fout),
                                                               specs);

        jcc.create ();

        f.close ();
        fout.close ();
    }

} // class J6502ConstantsCreator
