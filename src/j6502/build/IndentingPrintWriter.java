package j6502.build;

import java.io.*;

public class IndentingPrintWriter
    extends PrintWriter {


    boolean indentedYet = false;

    char indentChar = ' ';
    int  charsPerTab = 2;
    int  indent = 0;

    public IndentingPrintWriter (Writer w) {
        super (w, true);
    }

    public IndentingPrintWriter (Writer w, int charsPerTab) {
        this (w);
        this.charsPerTab = charsPerTab;
    }

    public void indent () {
        indent++;
    }

    public void unindent () {
        indent--;
        indent = Math.max (indent, 0);
    }

    private void printIndent () {
        if (indentedYet) return;

        indentedYet = true;

        for (int count = 0; count < (indent * charsPerTab); count++) {
            super.print (indentChar);
        }
    }

    public void print (boolean b) {
        printIndent ();
        super.print (b);
    }

    public void print (char c) {
        printIndent ();
        super.print (c);
    }

    public void print (char[] cs) {
        printIndent ();
        super.print (cs);
    }

    public void print (double d) {
        printIndent ();
        super.print (d);
    }

    public void print (float f) {
        printIndent ();
        super.print (f);
    }

    public void print (int i) {
        printIndent ();
        super.print (i);
    }

    public void print (long l) {
        printIndent ();
        super.print (l);
    }

    public void print (Object obj) {
        printIndent ();
        super.print (obj);
    }

    public void print (String s) {
        printIndent ();
        super.print (s);
    }

    public void println () {
        printIndent ();
        super.println ();
        indentedYet = false;
    }

    public void println (boolean b) {
        printIndent ();
        super.println (b); 
        indentedYet = false;
    }

    public void println (String s) {
        printIndent ();
        super.println (s);
        indentedYet = false;
    }

    public void println (char c) {
        printIndent ();
        super.println (c);
        indentedYet = false;
    }

    public void println (char[] cs) {
        printIndent ();
        super.println (cs);
        indentedYet = false;
    }

    public void println (double d) {
        printIndent ();
        super.println (d);
        indentedYet = false;
    }

    public void println (float f) {
        printIndent ();
        super.println (f);
        indentedYet = false;
    }

    public void println (int i) {
        printIndent ();
        super.println (i);
        indentedYet = false;
    }

    public void println (long l) {
        printIndent ();
        super.println (l);
        indentedYet = false;
    }

    public void println (Object obj) {
        printIndent ();
        super.println (obj);
        indentedYet = false;
    }

    public void write (char[] buf) {
        printIndent ();
        super.write (buf);
    }

    public void write (char[] buf, int off, int len) {
        printIndent ();
        super.write (buf, off, len);
    }

    public void write (int i) {
        printIndent ();
        super.write (i);
    }

    public void write (String s) {
        printIndent ();
        super.write (s);
    }

    public void write (String s, int off, int len) {
        printIndent ();
        super.write (s, off, len);
    }

    public static void main (String[] args) {
        IndentingPrintWriter ipw = new IndentingPrintWriter (new 
            OutputStreamWriter (System.out));

        ipw.indent ();
        ipw.println ("switch (x) {");
        ipw.println ("case 0:");
        ipw.indent ();
        ipw.print ("break;");
        ipw.print (" // this is a useless comment");
        ipw.println ();
        ipw.unindent ();
        ipw.println ("default:");
        ipw.println ("}");
    }

} // class IndentingPrintWriter
