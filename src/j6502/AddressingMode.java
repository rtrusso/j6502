package j6502;

import java.util.Hashtable;

/**
 * The AddressingMode class provides a typesafe enumeration of all
 * 6502 addressing modes.
 *
 * @author Richard Russo
 * @version 1.0
 * @since   1.0
 */

public class AddressingMode {

    private static Hashtable table = new Hashtable ();

    private static int ordCount = 0;

    private final int ordinalValue = ordCount++;

    /**
     * Get the ordinal value of this <code>AddressingMode</code>.  All
     * AddressingMode objects are assigned an arbitrary ordinal value.
     * 
     * @since 1.0
     */

    public int ord () {
        return ordinalValue;
    }

    private AddressingMode () {
        this (null, null);
    }

    String name;
    String pattern;

    private AddressingMode (String name, String pat) {
        this.name = name;
        pattern = pat;

        table.put (name, this);
    }

    /**
     * Get the AddressingMode whose name matches the specified name.
     * A <code>null</code> return value indicates that none match the
     * specified name.
     *
     * @since 1.0
     */

    public static AddressingMode getByName (String name) {
        if (table.containsKey (name)) {
            return (AddressingMode)table.get (name);
        }
        return null;
    }

    /**
     * Returns true if this <code>AddressingMode</code> matches the
     * specified <code>AddressingMode</code>.
     *
     * @since 1.0
     */

    public boolean equals (AddressingMode am) {
        return ord () == am.ord ();
    }

    /**
     * the zero page direct addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode 
        ZP_DIRECT = new AddressingMode ("ZP_DIRECT",
                                        "${0}");

    /**
     * The zero page indexed with x addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        ZP_INDEXED_X = new AddressingMode ("ZP_INDEXED_X",
                                           "${0},X");

    /**
     * The zero page indexed with y addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        ZP_INDEXED_Y = new AddressingMode ("ZP_INDEXED_Y",
                                           "${0},Y");

    /**
     * The pre-indexed indirect addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        PRE_INDEXED_INDIRECT = new AddressingMode ("PRE_INDEXED_INDIRECT",
                                                   "(${0},X)");

    /**
     * The post-indexed indirect addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        POST_INDEXED_INDIRECT = new AddressingMode ("POST_INDEXED_INDIRECT",
                                                    "(${0}),Y");

    /**
     * The direct addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        DIRECT = new AddressingMode ("DIRECT",
                                     "${1}{0}");

    /**
     * The direct indexed with x addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        INDEXED_X = new AddressingMode ("INDEXED_X",
                                        "${1}{0},X");
    /**
     * The direct indexed with y addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        INDEXED_Y = new AddressingMode ("INDEXED_Y",
                                        "${1}{0},Y");

    /**
     * The immediate addressing mode.
     *
     * @since 1.0
     */

    public final static AddressingMode
        IMMEDIATE = new AddressingMode ("IMMEDIATE",
                                        "#${0}");

    /**
     * The addressing mode for all 1-byte instructions which have no
     * operands.
     *
     * @since 1.0
     */

    public final static AddressingMode
        INHERENT = new AddressingMode ("INHERENT",
                                       "");

    /**
     * The addressing mode representing all instructions with the
     * accumulator as their target.
     *
     * @since 1.0
     */

    public final static AddressingMode
        ACCUMULATOR = new AddressingMode ("ACCUMULATOR",
                                          "A");

    /**
     * Represents the addressing mode of all of the conditional branch
     * instructions.
     *
     * @since 1.0
     */

    public final static AddressingMode
        JMP_RELATIVE = new AddressingMode ("JMP_RELATIVE",
                                           "${1}{0}");

    /**
     * Represents the addressing mode of a JMP instruction to an
     * absolute address.
     *
     * @since 1.0
     */

    public final static AddressingMode
        JMP_DIRECT = new AddressingMode ("JMP_DIRECT",
                                         "${1}{0}");

    /**
     * Represents the addressing mode of the indirect JMP instruction.
     *
     * @since 1.0
     */

    public final static AddressingMode
        JMP_INDIRECT = new AddressingMode ("JMP_INDIRECT",
                                           "(${1}{0})");

    /**
     * Get the name of this <code>AddressingMode</code>.
     *
     * @since 1.0
     */

    public String toString () {
        return name;
    }

    /**
     * Get a <code>String</code> pattern, suitable for use with
     * <code>java.text.MessageFormat</code> which can be used to get
     * the assembly code that represents this addressing mode.
     *
     * @since 1.0
     */

    public String getPattern () {
        return pattern;
    }

} // class AddressingMode
