package j6502;

// J6502Skeleton.java
// Richard Russo <rrusso@cs.ucf.edu>
// 08062000

/**
 * The <code>J6502Skeleton</code> class is the base class for
 * <code>J6502</code>, which is the actual emulator.
 *
 * J6502Skeleton provides the registers, code for addressing modes,
 * set/get property functions, etc.
 *
 * @author  Richard Russo
 * @version 1.0
 * @since   1.0
 */

abstract class J6502Skeleton {

    /**
     * The 6502 accumulator.  */
    protected byte accumulator;

    /**
     * The 6502 x index register.
     *
     * @since 1.0
     */

    protected byte regX;

    /**
     * The 6502 y index register.
     *
     * @since 1.0
     */

    protected byte regY;

    /**
     * The 6502 stack pointer.  Note that the 6502's stack is 256
     * bytes in size, and is located in the second memory page
     * (<code>$0100 - $01ff </code>).
     *
     * @since 1.0
     */

    protected byte regSP;
    //protected byte regP;


    /**
     * The 6502's program counter.
     *
     * @since 1.0
     */

    protected char pc;

    /**
     * Get the value held in the 6502's accumulator register.
     *
     * @returns the value in the accumulator
     *
     * @since 1.0
     */

    public byte getAccumulator () {
        return accumulator;
    }

    /**
     * Get all 6502 registers at once.  They are stored in a
     * <code>J6502Registers</code> object.
     *
     * @returns A <code>J6502Registers</code> object containing the
     * current value of the registers.
     *
     * @see j6502.J6502Registers
     *
     * @since 1.0
     */

    public J6502Registers getRegisters () {
        J6502Registers r = new J6502Registers ();

        r.accumulator = accumulator;
        r.regX  = regX;
        r.regY  = regY;
        r.regSP = regSP;
        r.regP  = getStatus ();
        r.pc    = pc;
        return r;
    }

    /**
     * Set all 6502 registers at once.  They are specified in a
     * <code>J6502Registers</code> object.
     *
     * @param r A <code>J6502Registers</code> object specifying the
     * state of each register.
     * @see j6502.J6502Registers
     *
     * @since 1.0
     */

    public void setRegisters (J6502Registers r) {
        accumulator = r.accumulator;
        regX = r.regX;
        regY = r.regY;
        regSP = r.regSP;
        setStatus (r.regP);
        pc = r.pc;
    }

    /**
     * Get the value of the X index register.
     *
     * @since 1.0
     */

    public byte getX () {
        return regX;
    }

    /**
     * Get the value of the Y index register.
     *
     * @since 1.0
     */

    public byte getY () {
        return regY;
    }

    /**
     * Get the value of the stack pointer.
     *
     * @since 1.0
     */

    public byte getSP () {
        return regSP;
    }

    /**
     * Get the value of the status register.
     *
     * @since 1.0
     */

    public byte getP () {
        return getStatus ();
    }

    /**
     * Get the value of the program counter.
     *
     * @since 1.0
     */

    public char getPC () {
        return pc;
    }

    /**
     * Set the value of the program counter.
     *
     * @since 1.0
     */

    public void setPC (char address) {
        pc = address;
    }

    /**
     * These integer values give the index of the flag that they name
     * in the <code>flags</code> array.
     *
     * @since 1.0
     */

     private static final int 
         CARRY     = 0,
         ZERO      = 1,
         INTERRUPT = 2,
         DECIMAL   = 3,
         BREAK     = 4,
         UNUSED    = 5,
         OVERFLOW  = 6,
         SIGN      = 7;

    //public static final int MEM_SIZE_BYTES = 65536;

    MemoryModel memory;

    /**
     * Read a byte from memory from the currently installed
     * <code>MemoryModel</code>.
     *
     * @param addr The address to read from.
     * @returns a <code>byte</code> value giving the contents of the memory location.
     *
     * @since 1.0
     */

    public byte readMemory (char addr) {
        return memory.readMemory (addr);
    }

    /**
     * Write a byte value to the <code>MemoryModel</code> currently installed.
     *
     * @param addr - the address to modify
     * @param value - the byte value to write to memory.
     *
     * @since 1.0
     */

    public void writeMemory (char addr, byte value) {
        memory.writeMemory (addr, value);
    }

    /**
     * Resets the cpu registers to their initial state and calls
     * <code>reset</code> on the currently installed
     * <code>MemoryModel</code>.
     *
     * @see j6502.MemoryModel
     * @see j6502.MemoryModel#reset()
     *
     * @since 1.0
     */

    public void reset () {
        pc = 0;
        accumulator = 0;
        regX = 0;
        regY = 0;
        regSP = (byte)(0xff);
        System.arraycopy (initialFlags, 0, flags, 0, 8);
        memory.reset ();
        setLastClocks (0);
        setElapsedClocks (0);
    }

    /**
     * A subclas can use this to construct a
     * <code>J6502Skeleton</code> with a specified
     * <code>MemoryModel</code>.
     *
     * @see j6502.MemoryModel
     *
     * @since 1.0
     */

    protected J6502Skeleton (MemoryModel mem) {
        memory = mem;
        reset ();
        lastClocks = 0;
        elapsedClocks = 0;
    }

    /**
     * A subclass can use this constructor to construct a
     * <code>J6502Skeleton</code> with a
     * <code>DefaultMemoryModel</code>.
     *
     * @see j6502.DefaultMemoryModel
     *
     * @since 1.0
     */

    protected J6502Skeleton () {
        this (new DefaultMemoryModel ());
    }

    /**
     * This gives the base address of the 6502's stack.  Note that the
     * stack starts at <code>STACK_BASE + $ff</code> and grows toward
     * memory address 0.
     *
     * @since 1.0
     */

    public final static int STACK_BASE = 0x100;

    /**
     * Push a byte onto the 6502's stack.  Note that no errors or
     * trapping occurs on stack overflow.  This may change in a
     * subsequent release.
     *
     * @see j6502.J6502Skeleton#pop()
     *
     * @since 1.0
     */

    public final void push (byte b) {
        char addr = (char)(STACK_BASE + (regSP & 0xff));

        writeMemory (addr, b);
        regSP--;
    }

    /**
     * Pop the byte on the top of the 6502's stack.  Note that there
     * is no notification of stack undeflow, but that may change in a
     * subsequent release.
     *
     * @see j6502.J6502Skeleton#push(byte)
     *
     * @since 1.0
     */

    public final byte pop () {
        regSP++;
        char addr = (char)(STACK_BASE + (regSP & 0xff));
        return readMemory (addr);
    }

    final boolean[] flags = new boolean[8];

    final boolean[] initialFlags = { false, // carry
                                     false, // zero
                                     true,  // interrupt
                                     false, // decimal
                                     
                                     false, // break
                                     false, // [unused]
                                     false, // overflow
                                     false }; // sign

    /**
     * Get the status of the carry flag.
     *
     * @returns true if it is set, false otherwise.
     *
     * @since 1.0
     */

    public final boolean isCarry () {
        return flags[CARRY];
    }

    /**
     * Get the status of the zero flag.
     *
     * @returns true if it is set, false otherwise.
     *
     * @since 1.0
     */

    public final boolean isZero () {
        return flags[ZERO];
    }

    /**
     * Get the status of the interrupt flag.
     *
     * @returns true if it is set, false otherwise.
     *
     * @since 1.0
     */

    public final boolean isInterrupt () {
        return flags[INTERRUPT];
    }

    /**
     * Get the status of the decimal mode flag.
     *
     * @returns true if it is set, false otherwise
     *
     * @since 1.0
     */

    public final boolean isDecimal () {
        return flags[DECIMAL];
    }

    /**
     * Returns true if the CPU is in binary coded decimal mode, false
     * otherwise.
     *
     * @see j6502.J6502Skeleton#isDecimal()
     *
     * @since 1.0
     */

    public final boolean isBCDMode () {
        return isDecimal ();
    }

    /**
     * Get the status of the break flag.
     *
     * @returns true if it is set, false otherwise.
     *
     * @since 1.0
     */

    public final boolean isBreak () {
        return flags[BREAK];
    }


    /**
     * Get the status of the overflow flag.
     *
     * @returns true if it is set, false otherwise.
     *
     * @since 1.0
     */

    public final boolean isOverflow () {
        return flags[OVERFLOW];
    }

    /**
     * Get the status of the sign flag.
     *
     * @returns true if it is set, false otherwise.
     *
     * @since 1.0
     */

    public final boolean isSign () {
        return flags[SIGN];
    }

    /**
     * Set the status of the carry flag.
     *
     * @since 1.0
     */

    public final void setCarry (boolean b) {
        flags[CARRY] = b;
    }

    /**
     * Set the status of the zero flag.
     *
     * @since 1.0
     */

    public final void setZero (boolean b) {
        flags[ZERO] = b;
    }

    /**
     * Set the status of the interrupt flag.
     *
     * @since 1.0
     */

    public final void setInterrupt (boolean b) {
        flags[INTERRUPT] = b;
    }

    /**
     * Set the status of the decimal flag.
     *
     * @since 1.0
     */

    public final void setDecimal (boolean b) {
        flags[DECIMAL] = b;
    }

    /**
     * set the status of the break flag.
     *
     * @since 1.0
     */

    public final void setBreak (boolean b) {
        flags[BREAK] = b;
    }

    /**
     * Set the status of the overflow flag.
     *
     * @since 1.0
     */

    public final void setOverflow (boolean b) {
        flags[OVERFLOW] = b;
    }

    /**
     * Set the status of the sign flag.
     *
     * @since 1.0
     */

    public final void setSign (boolean b) {
        flags[SIGN] = b;
    }

    /**
     * set the status of the arithmetic flags (sign and zero), based
     * on the value of b.
     *
     * @since 1.0
     */

    protected final void setArithmeticFlags (byte b) {
        setZero (b == 0);
        setSign (b < 0);
    }

    /**
     * Get the value of the status register.
     *
     * @since 1.0
     */

    public byte getStatus () {
        byte bflags = 0;

        for (int count = 7; count >= 0; count--) {
            bflags <<= 1;
            if (flags[count]) {
                bflags++;
            }
        }
        return bflags;
    } // getStatus

    /**
     * Set the value of the status register.
     *
     * @since 1.0
     */

    public void setStatus (byte b) {

        int count;
        byte mask = (byte)0x01;

        for (count = 0; count < 8; count++) {
            flags[count] = ((b & mask) != 0);
            mask <<= 1;
        }
    } // setStatus

    /**
     * Add with carry the value in b to the accumulator. This sets all relevant flags.
     *
     * @since 1.0
     */

    protected final void adc (byte b) {
	int result;
	int carry6;
	
	result = (b & 0xFF) + (accumulator & 0xFF);
	carry6 = (b & 0x7F) + (accumulator & 0x7F);
	
	if (isCarry ()) {
	    result++;
	    carry6++;
	}

        setCarry ((result & 0x100) != 0);
        setOverflow (isCarry () ^ ((carry6 & 0x80) != 0));

	accumulator = (byte)(result & 0xFF);

	setArithmeticFlags (accumulator);
    } // adc

    /**
     * Add with carry the value of b to the accumulator, in binary
     * coded decimal mode.  This sets all relevant flags.
     *
     * @since 1.0
     */

    protected final void adcBCD (byte b) {
	int low, high;
	int result;

	low = (accumulator & 0x0F) + (b & 0x0F);
	if (isCarry ())
	    {
		low++;
	    }

	high = (accumulator & 0xF0) + (b & 0xF0);

        setZero (((low + high) & 0xff) == 0);

	if (low > 9) {
	    low += 6;
	}

        setSign (((low + high) & 0x80) != 0);
        setOverflow ( ((((low + ((accumulator & 0x70) + (b & 0x70))) & 0x80) << 1) // bit 6 carry
                       ^ ((low + high) & 0x100)) != 0); // bit 7 carry

	if (high > 0x90) {
	    high += 0x60;
	}

	result = high + low;
	accumulator = (byte)(result);
        setCarry ((result & 0x100) != 0);
    } // adcBCD

    /**
     * Subtract with carry (borrow) the value of b from the
     * accumulator.  All relevant flags are set.
     *
     * @since 1.0
     */

    protected final void sbc (byte b) {
        b--;
        b ^= 0xff;
        adc (b);
    }

    /**
     * Subtract with carry (borrow) the value of b from the
     * accumulator, in binary coded decimal.  All relevant flags are
     * set.
     *
     * @since 1.0
     */

    protected final void sbcBCD (byte b) {
	int low, high;
	int result;

	low = (accumulator & 0x0F) - (b & 0x0F);

	if (!isCarry ())
	    {
		low --;
	    }

	low &= 0xFF;
	if ((low & 0x10) != 0)
	    {
		low -= 6;
	    }

	high = (accumulator & 0xF0) - (b & 0xF0);
	high &= 0xFF;

	if ((low & 0x10) != 0)
	    {
		high -= 0x10;
	    }

	if ((high & 0x100) != 0)
	    {
		high -= 0x60;
	    }

	result = (accumulator & 0xFF) - (b & 0xFF);
	if (isCarry ())
	    {
		result --;
	    }

        setCarry ((result & 0x100) != 0);
        setSign ((result & 0x80)  != 0);
        setZero ((result & 0xff) == 0);
        setOverflow ( (((result ^ accumulator) != 0)
                       && (((accumulator ^ b) & 0x80) != 0)));

	accumulator = (byte)((high & 0xF0) + (low & 0x0F));
    } // sbcBCD

    /**
     * Compare the value of a to the value of b, and set the relevant
     * flags accordingly.
     *
     * @since 1.0
     */

    public void cmp (byte a, byte b) {
	int i;
	
	b--;
	b ^= 0xFF;
	
	i = (a & 0xFF) + (b & 0xFF);
	
        setCarry ((i & 0x100) != 0);
        setArithmeticFlags ((byte)(i & 0xff));
    }

    /**
     * This represents a correction to the clocks table.  The value of
     * the <code>extraClocks</code> variable should be added to the
     * value in the table of clock cycles for opcode, to correct for
     * any exceptions to the table, depending on addressing mode.
     *
     * @since 1.0
     */

    protected int extraClocks = 0;

    /**
     * calculate target address, using zero page addressing.
     *
     * @since 1.0
     */

    protected char zpDirect (byte b) {
        extraClocks = 0;
        return (char)(b & 0xff);
    }

    /**
     * Calculate target address, using zero page indexed with x
     * addressing.
     *
     * @since 1.0
     */

    protected char zpIndexedX (byte b) {
        extraClocks = 0;

        int addr = (b & 0xff) + (regX & 0xff);
        if (addr > 255) {
            addr -= 256;
        }
        return (char)addr;
    }

    /**
     * Calculate target address, using zero page indexed with y
     * addressing.
     *
     * @since 1.0
     */

    protected char zpIndexedY (byte b) {
        extraClocks = 0;

        int addr = (b & 0xff) + (regY & 0xff);
        if (addr > 255) {
            addr -= 256;
        }
        return (char)addr;
    }

    /**
     * Calculate target address, using pre-indexed indirect
     * addressing.
     *
     * @since 1.0
     */

    protected char preIndexedIndirect (byte b) {
        extraClocks = 0;
        char addr = zpIndexedY (b);
        byte lsb = readMemory (addr);
        addr++;
        if (addr > 255) {
            addr -= 256;
        }

        byte msb = readMemory (addr);

        return direct (lsb, msb);
    }

    /**
     * Calculate target address, using post-indexed indirect
     * addressing.
     *
     * @since 1.0
     */

    protected char postIndexedIndirect (byte b) {
        extraClocks = 0;
        char addr = (char)(b  & 0xff);

        byte lsb = readMemory (addr);
        addr++;
        if (addr > 255) {
            addr -= 256;
        }
        byte msb = readMemory (addr);

        return indexedY (lsb, msb);
    }

    /**
     * Calculate target address, using direct addressing.
     *
     * @since 1.0
     */

    protected char direct (byte lsb, byte msb) {
        extraClocks = 0;
        return (char)(((msb & 0xff) << 8) + (lsb & 0xff));
    }

    /**
     * Calculate target address, using indexed (by x) addressing.
     *
     * @since 1.0
     */

    protected char indexedX (byte lsb, byte msb) {
        extraClocks = 0;

        char addr = direct (lsb, msb);

        int oldPage = addr / 256;
        addr += (char)(regX & 0xff);
        int newPage = addr / 256;

        if (oldPage != newPage) extraClocks++;

        return addr;
    }

    /**
     * Calculate target address, using indexed (by y) addressing
     *
     * @since 1.0
     */

    protected char indexedY (byte lsb, byte msb) {
        extraClocks = 0;

        char addr = direct (lsb, msb);
        int oldPage = addr / 256;
        addr += (char)(regY & 0xff);
        int newPage = addr / 256;

        if (oldPage != newPage) extraClocks++;

        return addr;
    }

    /**
     * calculate the value of extraClocks, using pc-relative
     * addressing.
     *
     * @since 1.0
     */

    protected void jmpRelative (byte b) {
        extraClocks = 0;
        char addr;

        addr = pc;
        int oldPage = addr / 256;
        addr += b;
        int newPage = addr / 256;

        if (oldPage == newPage) {
            extraClocks++;
        } else {
            extraClocks += 2;
        }
    } // jmpRelative

    /**
     * Calculate the target of a jmp instruction.
     *
     * @since 1.0
     */

    protected char jmpDirect (byte lsb, byte msb) {
        return direct (lsb, msb);
    }

    /**
     * Calculate the target of an indirect jmp instruction.
     *
     * @since 1.0
     */

    protected char jmpIndirect (byte incomingLSB, byte incomingMSB) {
        char addr = direct (incomingLSB, incomingMSB);

        byte lsb = readMemory (addr);
        addr++;
        byte msb = readMemory (addr);

        return direct (lsb, msb);
    } // jmpIndirect

    private final byte[] insnBuf = new byte[3];

    /**
     * Execute the instruction in the array <code>insn</code>.  It is
     * executed as if pc is pointing to the next instruction in
     * memory.  The opcode should be in the 0th position, and operands
     * should follow in the proper order.  If the array is too small
     * to contain the whole instruction, an
     * <code>ArrayIndexOutOfBoundsException</code> will be thrown. If
     * it is too large, the remaining bytes will be ignored.
     *
     * @returns an integer value, giving the number of clock cycles
     * that elapsed during the execution of the instruction.  A value
     * of 0 indicates that the operand was invalid.
     *
     * @since 1.0
     */

    public abstract int execute (byte[] insn);

    int lastClocks;
    int elapsedClocks;

    /**
     * Execute the instruction pointed to by the program counter, and
     * advance the program counter to point to the next instruction.
     *
     * @returns an int value giving the number clock cycles elapsed
     * during execution of the instruction, or 0 indicating that the
     * opcode was invalid.  In the case of an invalid opcode, the
     * program counter will not be advanced.
     *
     * @since 1.0
     */

    public int execute () {
        insnBuf[0] = readMemory (pc);
        
        int size = J6502Constants.sizes[insnBuf[0] & 0xff];

        //System.out.println ("0: " + (insnBuf[0] & 0xff));
        for (int count = 1; count < size; count++) {
            insnBuf[count] = readMemory ((char)(pc + count));
            //System.out.println (count + ": " + (insnBuf[count] & 0xff));
        }

        pc += size;

        lastClocks = execute (insnBuf);

        elapsedClocks += lastClocks;
        return lastClocks;
    }

    /**
     * Get the number of clocks elapsed during the last instruction
     * that was executed.
     *
     * @since 1.0
     */

    public int getLastClocks () {
        return lastClocks;
    }

    /**
     * Set the value returned by getLastClocks to i (until the
     * next call to execute).  
     *
     * @since 1.0
     */

    public void setLastClocks (int i) {
        lastClocks = i;
    }

    /**
     * Get the number of clocks elapsed since construction or the last
     * reset.
     *
     * @since 1.0
     */

    public int getElapsedClocks () {
        return elapsedClocks;
    }

    /**
     * Set the value returned by getElapsedClocks.  This will be
     * used in subsequent calls to getLastClocks, getElapsedClocks,
     * and execute.  
     *
     * @since 1.0
     */

    public void setElapsedClocks (int i) {
        if (i < 0) {
            throw new IllegalArgumentException ();
        }

        elapsedClocks = i;
    }

} // class J6502Skeleton
