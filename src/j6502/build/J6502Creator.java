package j6502.build;

import j6502.*;

import java.io.*;
import java.util.*;

public class J6502Creator 
    extends CodeCreator
{

    public static final String fileName = "J6502.java";

    IndentingPrintWriter ipw;

    InstructionSpec[] set;

    String[] preCode = {
        "package j6502;",
        "",
        "/**",
        " * The <code>J6502</code> is the class for actual 6502 emulation",
        " * objects.  Each one is created with a <code>MemoryMap</code> object,",
        " * either a specified one or a <code>DefaultMemoryMap</code>.",
        " *",
        " *",
        " * @see j6502.MemoryModel",
        " * @see j6502.DefaultMemoryModel",
        " *",
        " * @version 1.0",
        " * @since   1.0",
        " * @author  Richard Russo",
        " */",
        "",
        "public class J6502 extends J6502Skeleton {",
        "",
        "/**",
        " * Create a new <code>J6502</code> with a specified MemoryModel.",
        " *",
        " * @see j6502.MemoryModel",
        " */",
        "",
        "    public J6502 (MemoryModel mem) {",
        "        super (mem);",
        "    }",
        "",
        "/**",
        " * Create a new <code>J6502</code> with a <code>DefaultMemoryModel</code>",
        " *",
        " * @see j6502.DefaultMemoryModel",
        " */",
        "",
        "    public J6502 () {",
        "        super ();",
        "    }",
        "",
        "/**",
        " * Execute the instruction in the array.",
        " *",
        " * @see j6502.J6502Skeleton#execute(byte[])",
        " */",
        "",
        "    public int execute (byte[] insn) {",
        "        int clocks;",
        "        int opcode = insn[0] & 0xff;",
        "        boolean badOpcode = false;",
        "        char    addr;",
        "        byte    tmp;",
        "",
        "        extraClocks = 0;",
        "        clocks = J6502Constants.clocks[opcode];",
        "",
        "        switch (opcode) {"
    };

    String[] postCode = {
        "        }",
        "        return clocks;",
        "    } // execute",
        "} // class J6502"
    };

    public J6502Creator (PrintWriter out, InstructionSpec[] set) {
        ipw = new IndentingPrintWriter (out, 4);
        this.set = set;
    }

    void printCode (String[] code) {
        printCode (ipw, code);
    }

    // note that these are in the ordinal value
    // of the AddressingMode contants
    String[] addressingFunctions =
    {
        "zpDirect",
        "zpIndexedX",
        "zpIndexedY",
        "preIndexedIndirect",
        "postIndexedIndirect",
        "direct",
        "indexedX",
        "indexedY",
        "immediate",
        "inherent",
        "accumulator",
        "jmpRelative",
        "jmpDirect",
        "jmpIndirect"
    };

    String replace (String src, char c, String s) {
        StringTokenizer st = new StringTokenizer (src, "" + c, true);
        StringBuffer buf = new StringBuffer ();

        while (st.hasMoreTokens ()) {
            String tmp = st.nextToken ();

            if ((tmp.length () == 1)
                && (tmp.charAt (0) == c)) {
                buf.append (s);
            } else {
                buf.append (tmp);
            }
        }
        return buf.toString ();
    }

    void replace (String[] src, char c, String s) {
        for (int count = 0; count < src.length; count++) {
            src[count] = replace (src[count], c, s);
        }
    }

    void create (InstructionSpec is) {

        String atSymbol = "addr";
        String dollarSymbol = "tmp";
        String instructionArrayName = "insn";

        AddressingMode mode = is.rec.addrMode;

        String[] code = is.code.getCode ();

        if (mode.equals (AddressingMode.ACCUMULATOR)) {

            replace (code, '$', "accumulator");

            ipw.println ("{");
            ipw.indent ();
            printCode (code);
            ipw.unindent ();
            ipw.println ("}");

        } else if (mode.equals (AddressingMode.INHERENT)) {

            ipw.println ("{");
            ipw.indent ();
            printCode (code);
            ipw.unindent ();
            ipw.println ("}");

        } else if (mode.equals (AddressingMode.IMMEDIATE)) {

            ipw.println (dollarSymbol + " = " 
                         + instructionArrayName + "[1];");
            replace (code, '$', dollarSymbol);

            ipw.println ("{");
            ipw.indent ();
            printCode (code);
            ipw.unindent ();
            ipw.println ("}");

        } else if (mode.equals (AddressingMode.JMP_RELATIVE)) {
            
            ipw.println (dollarSymbol + " = " 
                         + instructionArrayName + "[1];");
            ipw.println ("jmpRelative (" + dollarSymbol + ");");
            replace (code, '$', dollarSymbol);

            ipw.println ("{");
            ipw.indent ();
            printCode (code);
            ipw.unindent ();
            ipw.println ("}");

        } else if (mode.equals (AddressingMode.JMP_DIRECT)) {
            
            ipw.println ("addr = jmpDirect ("
                         + instructionArrayName + "[1], "
                         + instructionArrayName + "[2]);");
            replace (code, '@', atSymbol);

            ipw.println ("{");
            ipw.indent ();
            printCode (code);
            ipw.unindent ();
            ipw.println ("}");

        } else if (mode.equals (AddressingMode.JMP_INDIRECT)) {

            ipw.println ("addr = jmpDirect ("
                         + instructionArrayName + "[1], "
                         + instructionArrayName + "[2]);");
            replace (code, '@', atSymbol);

            ipw.println ("{");
            ipw.indent ();
            printCode (code);
            ipw.unindent ();
            ipw.println ("}");

        } else {
            int opcode = is.rec.opcode;
            int num = J6502Constants.sizes[opcode] - 1;

            StringBuffer setupLine = new StringBuffer ();

            setupLine.append (atSymbol + " = ");

            setupLine.append (addressingFunctions[mode.ord ()]);
            setupLine.append (" (");

            boolean first = true;
            for (int count = 0; count < num; count++) {
                if (first) {
                    first = false;
                } else {
                    setupLine.append (", ");
                }
                setupLine.append (instructionArrayName
                                  + "[" + (1 + count) + "]");
            }
            setupLine.append (");");
            ipw.println (setupLine.toString ());
            ipw.println (dollarSymbol + " = readMemory (" + atSymbol
                         + ");");

            replace (code, '$', dollarSymbol);

            ipw.println ("{");
            ipw.indent ();
            printCode (code);
            ipw.unindent ();
            ipw.println ("}");

            if (is.write) {
                ipw.println ("writeMemory (" + atSymbol
                             + ", " + dollarSymbol
                             + ");");
            }
        }
    } // create(InstructionSpec)

    public void create () {
        int opcode;

        String clocksVar = "clocks";

        printCode (preCode);

        ipw.indent ();
        ipw.indent ();
        for (opcode = 0; opcode < set.length; opcode++) {
            String numString = Integer.toHexString (opcode);
            if (numString.length () < 2) {
                numString = "0" + numString;
            }
            ipw.println ("case 0x" + numString + ":");
            ipw.indent ();

            if (null != set[opcode]) {
                create (set[opcode]);
                if (J6502Constants.exceptions[opcode] > 0) {
                    ipw.println (clocksVar + " += extraClocks;");
                }

            } else {
                ipw.println ("badOpcode = true;");
            }

            ipw.println ("break;");
            ipw.println ();

            ipw.unindent ();
        }
        ipw.unindent ();
        ipw.unindent ();

        printCode (postCode);
    } // create

    public static void main (String[] args) 
        throws Exception
    {
        FileInputStream f = new FileInputStream ("6502.txt");
        CPUSpecReader reader = new CPUSpecReader (f);

        OperationSpec[] ospecs = reader.readAllOperationSpecs ();

        InstructionSpec[] specs = 
            InstructionSpec.makeInstructionSpecSet (ospecs);

        FileOutputStream fout = new FileOutputStream (fileName);

        J6502Creator jc = new J6502Creator (new IndentingPrintWriter (new
            OutputStreamWriter (fout)), specs);

        jc.create ();

        f.close ();
        fout.close ();
    }

} // J6502Creator
