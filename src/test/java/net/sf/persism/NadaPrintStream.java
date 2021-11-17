package net.sf.persism;

import java.io.OutputStream;
import java.io.PrintStream;

public class NadaPrintStream {
    public static PrintStream out;

    static {
        out = new PrintStream(OutputStream.nullOutputStream());
    }

}
