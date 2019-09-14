package j6502;

// MemoryModel.java
// Richard Russo <rrusso@cs.ucf.edu>
// 06102000

/**
 *
 * <P>
 *
 * The <CODE>MemoryModel</CODE> interface encodes the organization of
 * memory for a particular 6502-based machine.  It presents this
 * information to the <CODE>J6502</CODE> class through a simple
 * interface consisting of just a few methods.
 *
 * </P>
 * <P>
 *
 * If the 6502-based machine to be emulated has memory-mapped I/O or
 * if certain areas of RAM are mapped into several address ranges,
 * these quirks can be encoded in a class implementing this interface.
 * Using this interface, a class can be written to trap reads and
 * writes to the memory-mapped I/O ranges, etc.  An object of a class
 * implementing this interface is then passed to a specialized
 * constructor of <CODE>J6502</CODE>.
 *
 * </p>
 * <P>
 *
 * For a default implementation of this interface, see the
 * <code>DefaultMemoryModel</code> class.
 *
 * </p>
 * <p>
 *
 * All <code>MemoryModel</code> implementations should support reading
 * and writing of 65356 memory addresses.
 *
 * </p>
 * <P>
 *
 * Following is license information; for more information please see
 * the file named COPYING distributed with this software.
 *
 * </p>
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
 * <P>
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.  
 *
 * @version 1.0
 * @since   1.0
 *
 * @author  Richard Russo
 *
 * @see     j6502.DefaultMemoryModel
 * @see     j6502.J6502
 */

public interface MemoryModel {

    /*
     * Get the number of bytes that can be addressed with this
     * <CODE>MemoryModel</CODE>.  Currently, this should be 65536
     * bytes.
     *
     * @returns a char value giving the number of bytes.
     *
     * @since 1.0 
     */

    /* public int getSize ();  */

    /**
     * Read the byte in memory at a specified address.
     *
     * @param addr a non-negative integer specifying the memory
     * location. This number should be less than 65536.  Note that
     * this is the entire range of a <code>char</code> variable.
     *
     * @returns a <CODE>byte</CODE> value giving the value at the specified
     *          location.
     *
     * @since 1.0
     */

    public byte readMemory (char addr);

    /**
     * Write a specified byte value to a specified memory address.
     *
     * @param addr a non-negative integer specifying the memory
     * location, less than 65536.  Note that this is the entire range
     * of <code>char</code>.
     *
     * @param data the value to write to the specified memory location.
     *
     * @since 1.0
     */
    
    public void writeMemory (char addr, byte data);

    /**
     * Reset this memory to its initial state.
     *
     * This should provide an efficient implementation of resetting
     * this memory to its initial state.  For example, if all memory
     * locations get reset to 0, a good implementaion would be:
     *
     * <p>
     * <code>
     * Arrays.fill (memory, (byte)0);
     * </code>
     * </p>
     *
     * @since 1.0
     */

    public void reset ();

} // interface MemoryModel
