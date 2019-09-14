package j6502;

/**
 * The <code>J6502</code> is the class for actual 6502 emulation
 * objects.  Each one is created with a <code>MemoryMap</code> object,
 * either a specified one or a <code>DefaultMemoryMap</code>.
 *
 *
 * @see j6502.MemoryModel
 * @see j6502.DefaultMemoryModel
 *
 * @version 1.0
 * @since   1.0
 * @author  Richard Russo
 */

public class J6502 extends J6502Skeleton {

/**
 * Create a new <code>J6502</code> with a specified MemoryModel.
 *
 * @see j6502.MemoryModel
 */

    public J6502 (MemoryModel mem) {
        super (mem);
    }

/**
 * Create a new <code>J6502</code> with a <code>DefaultMemoryModel</code>
 *
 * @see j6502.DefaultMemoryModel
 */

    public J6502 () {
        super ();
    }

/**
 * Execute the instruction in the array.
 *
 * @see j6502.J6502Skeleton#execute(byte[])
 */

    public int execute (byte[] insn) {
        int clocks;
        int opcode = insn[0] & 0xff;
        boolean badOpcode = false;
        char    addr;
        byte    tmp;

        extraClocks = 0;
        clocks = J6502Constants.clocks[opcode];

        switch (opcode) {
        case 0x00:
            {
                setBreak (true);
                push ((byte)((pc >> 8) & 0xff));
                push ((byte)(pc & 0xff));
                push (getStatus ());
                setInterrupt (true);
                byte tmpLSB = readMemory ((char)0xfffe);
                byte tmpMSB = readMemory ((char)0xffff);
                pc = (char)(((tmpMSB & 0xff) << 8) + (tmpLSB & 0xff));
            }
            break;
            
        case 0x01:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x02:
            badOpcode = true;
            break;
            
        case 0x03:
            badOpcode = true;
            break;
            
        case 0x04:
            badOpcode = true;
            break;
            
        case 0x05:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x06:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x07:
            badOpcode = true;
            break;
            
        case 0x08:
            {
                push (getStatus ());
            }
            break;
            
        case 0x09:
            tmp = insn[1];
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x0a:
            {
                setCarry ((accumulator & 0x80) != 0);
                accumulator <<= 1;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x0b:
            badOpcode = true;
            break;
            
        case 0x0c:
            badOpcode = true;
            break;
            
        case 0x0d:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x0e:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x0f:
            badOpcode = true;
            break;
            
        case 0x10:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (!isSign ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0x11:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x12:
            badOpcode = true;
            break;
            
        case 0x13:
            badOpcode = true;
            break;
            
        case 0x14:
            badOpcode = true;
            break;
            
        case 0x15:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x16:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x17:
            badOpcode = true;
            break;
            
        case 0x18:
            {
                setCarry (false);
            }
            break;
            
        case 0x19:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x1a:
            badOpcode = true;
            break;
            
        case 0x1b:
            badOpcode = true;
            break;
            
        case 0x1c:
            badOpcode = true;
            break;
            
        case 0x1d:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator |= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x1e:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x1f:
            badOpcode = true;
            break;
            
        case 0x20:
            addr = jmpDirect (insn[1], insn[2]);
            {
                pc--;
                push ((byte)((pc >> 8) & 0xff));
                push ((byte)((pc & 0xff)));
                pc = addr;
            }
            break;
            
        case 0x21:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x22:
            badOpcode = true;
            break;
            
        case 0x23:
            badOpcode = true;
            break;
            
        case 0x24:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                setSign ((tmp & 0x80) != 0);
                setOverflow ((tmp & 0x40) != 0);
                setZero ((accumulator & tmp) != 0);
            }
            break;
            
        case 0x25:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x26:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                if (tmpCarry) {
                  tmp++;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x27:
            badOpcode = true;
            break;
            
        case 0x28:
            {
                setStatus (pop ());
            }
            break;
            
        case 0x29:
            tmp = insn[1];
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x2a:
            {
                boolean tmpCarry = isCarry ();
                setCarry ((accumulator & 0x80) != 0);
                accumulator <<= 1;
                if (tmpCarry) {
                  accumulator++;
                }
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x2b:
            badOpcode = true;
            break;
            
        case 0x2c:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                setSign ((tmp & 0x80) != 0);
                setOverflow ((tmp & 0x40) != 0);
                setZero ((accumulator & tmp) != 0);
            }
            break;
            
        case 0x2d:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x2e:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                if (tmpCarry) {
                  tmp++;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x2f:
            badOpcode = true;
            break;
            
        case 0x30:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (isSign ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0x31:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x32:
            badOpcode = true;
            break;
            
        case 0x33:
            badOpcode = true;
            break;
            
        case 0x34:
            badOpcode = true;
            break;
            
        case 0x35:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x36:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                if (tmpCarry) {
                  tmp++;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x37:
            badOpcode = true;
            break;
            
        case 0x38:
            {
                setCarry (true);
            }
            break;
            
        case 0x39:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x3a:
            badOpcode = true;
            break;
            
        case 0x3b:
            badOpcode = true;
            break;
            
        case 0x3c:
            badOpcode = true;
            break;
            
        case 0x3d:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator &= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x3e:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x80) != 0);
                tmp <<= 1;
                if (tmpCarry) {
                  tmp++;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x3f:
            badOpcode = true;
            break;
            
        case 0x40:
            {
                setStatus (pop ());
                byte tmpLSB = pop ();
                byte tmpMSB = pop ();
                pc = (char)(((tmpMSB & 0xff) << 8) + (tmpLSB & 0xff));
                pc++;
            }
            break;
            
        case 0x41:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x42:
            badOpcode = true;
            break;
            
        case 0x43:
            badOpcode = true;
            break;
            
        case 0x44:
            badOpcode = true;
            break;
            
        case 0x45:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x46:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x47:
            badOpcode = true;
            break;
            
        case 0x48:
            {
                push (accumulator);
            }
            break;
            
        case 0x49:
            tmp = insn[1];
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x4a:
            {
                setCarry ((accumulator & 0x01) != 0);
                accumulator >>>= 1;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x4b:
            badOpcode = true;
            break;
            
        case 0x4c:
            addr = jmpDirect (insn[1], insn[2]);
            {
                pc = addr;
            }
            break;
            
        case 0x4d:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x4e:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x4f:
            badOpcode = true;
            break;
            
        case 0x50:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (!isOverflow ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0x51:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x52:
            badOpcode = true;
            break;
            
        case 0x53:
            badOpcode = true;
            break;
            
        case 0x54:
            badOpcode = true;
            break;
            
        case 0x55:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x56:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x57:
            badOpcode = true;
            break;
            
        case 0x58:
            {
                setInterrupt (false);
            }
            break;
            
        case 0x59:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x5a:
            badOpcode = true;
            break;
            
        case 0x5b:
            badOpcode = true;
            break;
            
        case 0x5c:
            badOpcode = true;
            break;
            
        case 0x5d:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator ^= tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0x5e:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x5f:
            badOpcode = true;
            break;
            
        case 0x60:
            {
                byte tmpLSB = pop ();
                byte tmpMSB = pop ();
                pc = (char)(((tmpMSB & 0xff) << 8) + (tmpLSB & 0xff));
                pc++;
            }
            break;
            
        case 0x61:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            break;
            
        case 0x62:
            badOpcode = true;
            break;
            
        case 0x63:
            badOpcode = true;
            break;
            
        case 0x64:
            badOpcode = true;
            break;
            
        case 0x65:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            break;
            
        case 0x66:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                if (tmpCarry) {
                  tmp |= 0x80;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x67:
            badOpcode = true;
            break;
            
        case 0x68:
            {
                accumulator = pop ();
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x69:
            tmp = insn[1];
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            break;
            
        case 0x6a:
            {
                boolean tmpCarry = isCarry ();
                setCarry ((accumulator & 0x01) != 0);
                accumulator >>>= 1;
                if (tmpCarry) {
                  accumulator |= 0x80;
                }
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x6b:
            badOpcode = true;
            break;
            
        case 0x6c:
            addr = jmpDirect (insn[1], insn[2]);
            {
                pc = addr;
            }
            break;
            
        case 0x6d:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            break;
            
        case 0x6e:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                if (tmpCarry) {
                  tmp |= 0x80;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x6f:
            badOpcode = true;
            break;
            
        case 0x70:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (isOverflow ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0x71:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            clocks += extraClocks;
            break;
            
        case 0x72:
            badOpcode = true;
            break;
            
        case 0x73:
            badOpcode = true;
            break;
            
        case 0x74:
            badOpcode = true;
            break;
            
        case 0x75:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            break;
            
        case 0x76:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                if (tmpCarry) {
                  tmp |= 0x80;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x77:
            badOpcode = true;
            break;
            
        case 0x78:
            {
                setInterrupt (true);
            }
            break;
            
        case 0x79:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            clocks += extraClocks;
            break;
            
        case 0x7a:
            badOpcode = true;
            break;
            
        case 0x7b:
            badOpcode = true;
            break;
            
        case 0x7c:
            badOpcode = true;
            break;
            
        case 0x7d:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  adcBCD (tmp);
                } else {
                  adc (tmp);
                }
            }
            clocks += extraClocks;
            break;
            
        case 0x7e:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                boolean tmpCarry = isCarry ();
                setCarry ((tmp & 0x01) != 0);
                tmp >>>= 1;
                if (tmpCarry) {
                  tmp |= 0x80;
                }
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x7f:
            badOpcode = true;
            break;
            
        case 0x80:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (isCarry ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0x81:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = accumulator;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x82:
            badOpcode = true;
            break;
            
        case 0x83:
            badOpcode = true;
            break;
            
        case 0x84:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = regY;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x85:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = accumulator;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x86:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = regX;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x87:
            badOpcode = true;
            break;
            
        case 0x88:
            {
                regY--;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0x89:
            badOpcode = true;
            break;
            
        case 0x8a:
            {
                accumulator = regX;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x8b:
            badOpcode = true;
            break;
            
        case 0x8c:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp = regY;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x8d:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp = accumulator;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x8e:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp = regX;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x8f:
            badOpcode = true;
            break;
            
        case 0x90:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (!isCarry ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0x91:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = accumulator;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x92:
            badOpcode = true;
            break;
            
        case 0x93:
            badOpcode = true;
            break;
            
        case 0x94:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = regY;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x95:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = accumulator;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x96:
            addr = zpIndexedY (insn[1]);
            tmp = readMemory (addr);
            {
                tmp = regX;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x97:
            badOpcode = true;
            break;
            
        case 0x98:
            {
                accumulator = regY;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0x99:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp = accumulator;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x9a:
            {
                regSP = regX;
            }
            break;
            
        case 0x9b:
            badOpcode = true;
            break;
            
        case 0x9c:
            badOpcode = true;
            break;
            
        case 0x9d:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp = accumulator;
            }
            writeMemory (addr, tmp);
            break;
            
        case 0x9e:
            badOpcode = true;
            break;
            
        case 0x9f:
            badOpcode = true;
            break;
            
        case 0xa0:
            tmp = insn[1];
            {
                regY = tmp;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0xa1:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0xa2:
            tmp = insn[1];
            {
                regX = tmp;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xa3:
            badOpcode = true;
            break;
            
        case 0xa4:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                regY = tmp;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0xa5:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0xa6:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                regX = tmp;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xa7:
            badOpcode = true;
            break;
            
        case 0xa8:
            {
                regY = accumulator;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0xa9:
            tmp = insn[1];
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0xaa:
            {
                regX = accumulator;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xab:
            badOpcode = true;
            break;
            
        case 0xac:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                regY = tmp;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0xad:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0xae:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                regX = tmp;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xaf:
            badOpcode = true;
            break;
            
        case 0xb0:
            badOpcode = true;
            break;
            
        case 0xb1:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0xb2:
            badOpcode = true;
            break;
            
        case 0xb3:
            badOpcode = true;
            break;
            
        case 0xb4:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                regY = tmp;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0xb5:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            break;
            
        case 0xb6:
            addr = zpIndexedY (insn[1]);
            tmp = readMemory (addr);
            {
                regX = tmp;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xb7:
            badOpcode = true;
            break;
            
        case 0xb8:
            {
                setOverflow (false);
            }
            break;
            
        case 0xb9:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0xba:
            {
                regX = regSP;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xbb:
            badOpcode = true;
            break;
            
        case 0xbc:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                regY = tmp;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0xbd:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                accumulator = tmp;
                setArithmeticFlags (accumulator);
            }
            clocks += extraClocks;
            break;
            
        case 0xbe:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                regX = tmp;
                setArithmeticFlags (regX);
            }
            clocks += extraClocks;
            break;
            
        case 0xbf:
            badOpcode = true;
            break;
            
        case 0xc0:
            tmp = insn[1];
            {
                cmp (regY, tmp);
            }
            break;
            
        case 0xc1:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                cmp (accumulator, tmp);
            }
            break;
            
        case 0xc2:
            badOpcode = true;
            break;
            
        case 0xc3:
            badOpcode = true;
            break;
            
        case 0xc4:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                cmp (regY, tmp);
            }
            break;
            
        case 0xc5:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                cmp (accumulator, tmp);
            }
            break;
            
        case 0xc6:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                tmp--;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xc7:
            badOpcode = true;
            break;
            
        case 0xc8:
            {
                regY++;
                setArithmeticFlags (regY);
            }
            break;
            
        case 0xc9:
            tmp = insn[1];
            {
                cmp (accumulator, tmp);
            }
            break;
            
        case 0xca:
            {
                regX--;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xcb:
            badOpcode = true;
            break;
            
        case 0xcc:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                cmp (regY, tmp);
            }
            break;
            
        case 0xcd:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                cmp (accumulator, tmp);
            }
            break;
            
        case 0xce:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp--;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xcf:
            badOpcode = true;
            break;
            
        case 0xd0:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (!isZero ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0xd1:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                cmp (accumulator, tmp);
            }
            clocks += extraClocks;
            break;
            
        case 0xd2:
            badOpcode = true;
            break;
            
        case 0xd3:
            badOpcode = true;
            break;
            
        case 0xd4:
            badOpcode = true;
            break;
            
        case 0xd5:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                cmp (accumulator, tmp);
            }
            break;
            
        case 0xd6:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                tmp--;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xd7:
            badOpcode = true;
            break;
            
        case 0xd8:
            {
                setDecimal (false);
            }
            break;
            
        case 0xd9:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                cmp (accumulator, tmp);
            }
            clocks += extraClocks;
            break;
            
        case 0xda:
            badOpcode = true;
            break;
            
        case 0xdb:
            badOpcode = true;
            break;
            
        case 0xdc:
            badOpcode = true;
            break;
            
        case 0xdd:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                cmp (accumulator, tmp);
            }
            clocks += extraClocks;
            break;
            
        case 0xde:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp--;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xdf:
            badOpcode = true;
            break;
            
        case 0xe0:
            tmp = insn[1];
            {
                cmp (regX, tmp);
            }
            break;
            
        case 0xe1:
            addr = preIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            break;
            
        case 0xe2:
            badOpcode = true;
            break;
            
        case 0xe3:
            badOpcode = true;
            break;
            
        case 0xe4:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                cmp (regX, tmp);
            }
            break;
            
        case 0xe5:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            break;
            
        case 0xe6:
            addr = zpDirect (insn[1]);
            tmp = readMemory (addr);
            {
                tmp++;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xe7:
            badOpcode = true;
            break;
            
        case 0xe8:
            {
                regX++;
                setArithmeticFlags (regX);
            }
            break;
            
        case 0xe9:
            tmp = insn[1];
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            break;
            
        case 0xea:
            {
            }
            break;
            
        case 0xeb:
            badOpcode = true;
            break;
            
        case 0xec:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                cmp (regX, tmp);
            }
            break;
            
        case 0xed:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            break;
            
        case 0xee:
            addr = direct (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp++;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xef:
            badOpcode = true;
            break;
            
        case 0xf0:
            tmp = insn[1];
            jmpRelative (tmp);
            {
                if (isZero ()) pc += tmp;
            }
            clocks += extraClocks;
            break;
            
        case 0xf1:
            addr = postIndexedIndirect (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            clocks += extraClocks;
            break;
            
        case 0xf2:
            badOpcode = true;
            break;
            
        case 0xf3:
            badOpcode = true;
            break;
            
        case 0xf4:
            badOpcode = true;
            break;
            
        case 0xf5:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            break;
            
        case 0xf6:
            addr = zpIndexedX (insn[1]);
            tmp = readMemory (addr);
            {
                tmp++;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xf7:
            badOpcode = true;
            break;
            
        case 0xf8:
            {
                setDecimal (true);
            }
            break;
            
        case 0xf9:
            addr = indexedY (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            clocks += extraClocks;
            break;
            
        case 0xfa:
            badOpcode = true;
            break;
            
        case 0xfb:
            badOpcode = true;
            break;
            
        case 0xfc:
            badOpcode = true;
            break;
            
        case 0xfd:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                if (isBCDMode ()) {
                  sbcBCD (tmp);
                } else {
                  sbc (tmp);
                }
            }
            clocks += extraClocks;
            break;
            
        case 0xfe:
            addr = indexedX (insn[1], insn[2]);
            tmp = readMemory (addr);
            {
                tmp++;
                setArithmeticFlags (tmp);
            }
            writeMemory (addr, tmp);
            break;
            
        case 0xff:
            badOpcode = true;
            break;
            
        }
        return clocks;
    } // execute
} // class J6502
