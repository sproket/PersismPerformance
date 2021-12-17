package net.sf.persism;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestJDBC.class,
        TestPersism.class,
        TestJPA.class,
        TestJDBC.class,
        TestPersism.class,
        TestJPA.class,
        TestJDBC.class,
        TestPersism.class,
        TestJPA.class,
})
public class AllTests {
}
