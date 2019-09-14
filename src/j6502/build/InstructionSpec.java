package j6502.build;

class InstructionSpec {

    private static final boolean DEBUG = false;

    String      mnemonic, description;
    CodeSnippet code;
    boolean read, write;
    InstructionRecord rec;

    public InstructionSpec (String name,
                            String desc,
                            CodeSnippet javaCode,
                            InstructionRecord record)
    {
        mnemonic = name;
        description = desc;
        code = javaCode;
        rec = record;
    }

    static InstructionSpec[] makeInstructionSpecSet (OperationSpec[] ops) {
        InstructionSpec[] set = new InstructionSpec[256];
        int numInsn = 0;

        for (int count = 0; count < ops.length; count++) {
            
            if (ops[count] == null) {
                throw new Error ("null problem " + count + " " + ops.length);
            }
            InstructionRecord[] allRecs = ops[count].getRecords ();
            for (int rec = 0; rec < allRecs.length; rec++) {
                int idx = allRecs[rec].opcode;

                if (set[idx] != null) {
                    throw new IllegalArgumentException ("duplicate entry for opcode 0x"
                                                        + Integer.toHexString (idx));
                }

                set[idx] = new InstructionSpec (ops[count].mnemonic,
                                                ops[count].description,
                                                ops[count].code,
                                                allRecs[rec]);

                set[idx].read = ops[count].read;
                set[idx].write = ops[count].write;
                numInsn++;
            }
        }

        if (DEBUG) {
            System.out.println ("j6502.build.InstructionSpec"
                                + ": makeInstructionSpecSet:");
            System.out.println ("  " + numInsn + " used");
        }

        return set;
    } // makeInstructionSpecSet

} // class InstructionSpec
