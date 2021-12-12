package net.sf.persism;

import junit.framework.TestCase;

public abstract class BaseTest extends TestCase {

    long now;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    void out(Object text) {
        long newNan = (System.nanoTime() - now);
        long newMil = newNan / 1000000;

        System.out.println("TIME:  " + newNan + " (" + newMil + ") " + text);
        now = System.nanoTime();
    }

    abstract void testUserAndVotes() throws Exception;

    abstract void testAllFullUsers() throws Exception;

    abstract void testAllFullAutoUsers() throws Exception;

    abstract void testFetchComments() throws Exception;

    abstract void testFetchPost() throws Exception;

    abstract void testUsersSingleWithFetch() throws Exception;
}
