package j6502;

/**
 * DefaultMemoryModel.java<BR>
 * Richard Russo <A HREF="mailto:rrusso@cs.ucf.edu">rrusso@cs.ucf.edu</A>
 *
 * <P>
 *
 * The <CODE>DefaultMemoryModel</CODE> provides a trivial implementation
 * of the <CODE>MemoryModel</CODE> interface.  It provides a 64K address
 * space, with no special actions taken on memory reads or writes.
 *
 * </p>
 *
 *
 * <P>
 *
 * Following is license information; for more information please see
 * the file named COPYING distributed with this software.
 *
 * </p>
 *
 * <HR>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * <P>
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * </p>
 *
 * <P>
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.  
 *
 * </p>
 *
 * @version 1.0
 * @since   1.0
 *
 * @author Richard Russo
 *
 * @see j6502.MemoryModel
 */

public class DefaultMemoryModel implements MemoryModel {
    byte[] memory;

    /**
     * The size, in bytes, of the address space of every instance of
     * <code>DefaultMemoryModel</code>
     *
     * @since 1.0
     */

    public static final int SIZE = 65536;

    /**
     * Construct a new DefaultMemoryModel.
     *
     * @since 1.0
     */

    public DefaultMemoryModel () {
	memory = new byte[SIZE];
    }

    /**
     * Returns the size of the address space of this <code>DefaultMemoryModel</code>.
     *
     * @since 1.0
     */

    public int getSize ()
    {
	return SIZE;
    }

    /**
     * See the general contract of method <CODE>writeMemory</CODE> of
     * <CODE>MemoryModel</CODE>.
     *
     * @see j6502.MemoryModel#writeMemory(char,byte)
     *
     * @since 1.0
     */

    public void writeMemory (char addr, byte value)
    {
	memory[addr] = value;
    }

    /**
     * See The general contract of method <CODE>readMemory</CODE> of
     * <CODE>MemoryModel</CODE>.
     *
     * @see j6502.MemoryModel#readMemory(char)
     *
     * @since 1.0
     */

    public byte readMemory (char addr)
    {
	return memory[addr];
    }

    /**
     * Sets all memory locations to have the value 0.
     *
     * @see j6502.MemoryModel#reset
     * @since 1.0
     */

    public void reset () {
        for (int count = 0; count < SIZE; count++) {
            memory[count] = 0;
        }
    }

} // class DefaultMemoryModel
