package j6502;

/**
 * Objects of this class contain a copy of the 6502's registers.
 *
 * @see j6502.J6502#getRegisters()
 * @see j6502.J6502#setRegisters(J6502Registers)
 *
 * @version 1.0
 * @since   1.0
 * @author  Richard Russo
 */

public class J6502Registers {

    /**
     * The 6502's accumulator register.  The initial value is 0.
     *
     * @since 1.0
     */

    public byte accumulator = 0;

    /**
     * The 6502's x index register.  The initial value is 0.
     *
     * @since 1.0
     */
    
    public byte regX = 0;

    /**
     * The 6502's y index register.  The initial value is 0.
     *
     *
     * @since 1.0
     */

    public byte regY = 0;

    /**
     * The 6502's stack pointer.  The initial value is <code>0xff</code>
     *
     * @since 1.0
     */

    public byte regSP = (byte)0xff;

    /**
     * The 6502's status register.  The initial value is <code>0x04</code>.
     *
     *
     * @since 1.0
     */

    public byte regP = (byte)0x04;

    /**
     * The 6502's program counter.  The initial value is 0.
     *
     * @since 1.0
     */

    public char pc = 0;

} // class J6502Registers
