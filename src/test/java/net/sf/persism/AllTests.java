package net.sf.persism;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(TestPersism.class);
        testSuite.addTestSuite(TestJDBC.class);
        testSuite.addTestSuite(TestJPA.class);

        testSuite.addTestSuite(TestPersism.class);
        testSuite.addTestSuite(TestJDBC.class);
        testSuite.addTestSuite(TestJPA.class);
        return testSuite;
    }
}
