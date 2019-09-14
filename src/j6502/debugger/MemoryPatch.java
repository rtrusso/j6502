package j6502.debugger;

import j6502.*;

import java.util.*;

public class MemoryPatch {

    public class Pair {
        char address;
        byte value;
        byte newValue;

        public Pair (char ad, byte val) {
            address = ad;
            value = val;
        }

        public Pair (char ad, byte val, byte nval) {
            address = ad;
            value = val;
            newValue = nval;
        }

        public char getAddress () {
            return address;
        }

        public byte getValue () {
            return value;
        }

        public byte getNewValue () {
            return newValue;
        }
    } // class Pair;

    Vector patches;

    public MemoryPatch () {
        patches = new Vector ();

    }

    public void add (char addr, byte val) {
        Pair p = new Pair (addr, val);
        patches.add (p);
    }

    public void add (char addr, byte val, byte nval) {
        Pair p = new Pair (addr, val, nval);
        patches.add (p);
    }

    public void apply (MemoryModel m) {
        for (Enumeration e = patches.elements ();
             e.hasMoreElements ();) {
            Pair p = (Pair)e.nextElement ();
            m.writeMemory (p.address, p.value);
        }
    }
    
    public int size () {
        return patches.size ();
    }

    public MemoryPatch.Pair get (int i) {
        return (MemoryPatch.Pair)patches.get (i);
    }

} // class MemoryPatch
