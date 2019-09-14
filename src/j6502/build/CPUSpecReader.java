package j6502.build;

import java.io.*;
import java.text.*;
import java.util.*;

class CPUSpecReader {

    LineNumberReader stdin;

    public CPUSpecReader (InputStream in)
        throws Exception
    {
        stdin = new LineNumberReader (new
            InputStreamReader (in));
    }

    private String nextGoodLine () 
        throws IOException
    {

        String line;

        do {
            line = stdin.readLine ();
            if (null == line) return null;
            //line = line.trim ();
        } while ((line.length () == 0)
                 || (line.startsWith ("#")));

        return line;
    }

    public OperationSpec readOperationSpec ()
        throws IOException, CPUSpecFormatException
    {
        String line;

        line = nextGoodLine ();

        if (null == line) {
            return null;
            /**
            throw new CPUFormatException
                ("can't read operation mnemonic and description");
            */
        }

        StringTokenizer st = new StringTokenizer (line, "-", false);

        if (st.countTokens () != 2) {
            throw new CPUSpecFormatException
                ("bad operation mnemonic and description line: " 
                 + stdin.getLineNumber ());
        }

        String name = st.nextToken ().trim ();
        String description = st.nextToken ().trim ();

        if ((name.length () != 3) || (description.length () == 0)) {
            throw new CPUSpecFormatException
                ("bad name or description, line: "
                 + stdin.getLineNumber ());
        }

        int flagsLineNumber = stdin.getLineNumber ();
        line = nextGoodLine ();

        if (null == line) {
            throw new CPUSpecFormatException
                ("expecting flags, line: "
                 + flagsLineNumber);
        }

        if ((line.length () < 1) || (line.length () > 2)) {
            throw new CPUSpecFormatException
                ("bad flags, line: "
                 + stdin.getLineNumber ());
        }

        boolean read = false;
        boolean write = false;

        for (int count = 0; count < line.length (); count++) {
            char c = line.charAt (count);

            if (c == 'r') {
                if (read) {
                    throw new CPUSpecFormatException
                        ("bad flags, line: "
                         + stdin.getLineNumber ());
                }
                read = true;
            } else if (c == 'w') {
                if (write) {
                    throw new CPUSpecFormatException
                        ("bad flags, line: "
                         + stdin.getLineNumber ());
                }
                write = true;
            } else {
                throw new CPUSpecFormatException
                    ("bad flags, line: " 
                     + stdin.getLineNumber ());
            }
        }

        int separatorLineNumber = stdin.getLineNumber ();
        line = nextGoodLine ();

        if (null == line) {
            throw new CPUSpecFormatException
                ("expecting separator [---], line: "
                 + separatorLineNumber);
        }

        if (!line.equals ("---")) {
            throw new CPUSpecFormatException
                ("expecting separator [---], line: "
                 + stdin.getLineNumber ());
        }

        CodeSnippet snippet = new CodeSnippet ();

        do {
            line = nextGoodLine ();
            if (null == line) {
                throw new CPUSpecFormatException
                    ("unexpected end of file while "
                     + "reading code snippet, line: "
                     + stdin.getLineNumber ());
            }

            if (!line.equals ("---")) {
                snippet.addCodeLine (line);
            }
        } while (!line.equals ("---"));

        OperationSpec spec = new OperationSpec (name,
                                                description,
                                                snippet);

        spec.read = read;
        spec.write = write;

        do {
            int tmpLineNo = stdin.getLineNumber ();
            line = nextGoodLine ();
            if (null == line) {
                throw new CPUSpecFormatException
                    ("unexpected end of file while "
                     + "reading instruction specifications, "
                     + "line: " + tmpLineNo);
            }

            if (!line.equals ("---")) {
                InstructionRecord is = InstructionRecord.parse (line);

                if (null == is) {
                    throw new CPUSpecFormatException
                        ("bad instruction specification, line: "
                         + stdin.getLineNumber ());
                }
                spec.addInstructionRecord (is);
            }
        } while (!line.equals ("---"));

        return spec;

    } // readOperationSpec

    /**
     * Get all OperationSpecs defined.
     *
     * @returns an array containing all of the OperationSpec objects.
     *
     */

    public OperationSpec[] readAllOperationSpecs () 
        throws IOException, CPUSpecFormatException
    {
        Vector v = new Vector ();
        

        OperationSpec spec = readOperationSpec ();
        while (null != spec) {
            v.add (spec);
            spec = readOperationSpec ();
        }

        OperationSpec[] specs = new OperationSpec[v.size ()];

        int count = 0;
        for (Enumeration e = v.elements (); e.hasMoreElements (); count++) {
            specs[count] = (OperationSpec)e.nextElement ();
        }
        return specs;
    }

    public static void main (String[] args) 
        throws Exception
    {

        FileInputStream f = new FileInputStream ("6502.txt");

        CPUSpecReader csr = new CPUSpecReader (f);

        OperationSpec[] specs = csr.readAllOperationSpecs ();
        
        InstructionSpec[] set = InstructionSpec.makeInstructionSpecSet (specs);

        f.close ();
    }
} // class CPUSpecReader
