package net.sf.persism;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class TestMisc {

    long now;

    @Before
    public void setUp() throws Exception {
        System.out.println("START");
        now = System.nanoTime();
    }

    @Test
    public void testEqualsVsEquals() {

        Object x;
        if (new Random().nextBoolean()) {
            x = "";
        } else {
            x = 10;
        }


        now = System.nanoTime();
        if (x.getClass() == String.class) {
            out("==");
        }


        now = System.nanoTime();
        if (x.getClass().equals(String.class)) {
            out("eq");
        }

        now = System.nanoTime();
        if (x.getClass() == String.class) {
            out("==");
        }


        now = System.nanoTime();
        if (x.getClass().equals(String.class)) {
            out("eq");
        }

        now = System.nanoTime();
        if (x.getClass() == String.class) {
            out("==");
        }


        now = System.nanoTime();
        if (x.getClass().equals(String.class)) {
            out("eq");
        }

        now = System.nanoTime();
        if (x.getClass() == String.class) {
            out("==");
        }


        now = System.nanoTime();
        if (x.getClass().equals(String.class)) {
            out("eq");
        }

    }

    private void out(String text) {
        long newNan = (System.nanoTime() - now);
        long newMil = newNan / 1_000_000;

        /*
        ID  CLASS                       METHOD              MESSAGE                                         TIMING      DateTime                MS
        199	net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2380753700 (2380) badges size: 1102019	2380753700	2021-12-13 14:12:04.903	2380
         */
        String outText = "TIME:  " + newNan + " (" + newMil + ") " + text;
        System.out.println(outText);

    }

}