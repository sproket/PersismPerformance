package net.sf.persism;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestPersism.class,
        TestJDBC.class,
        TestJPA.class,
        TestPersism.class,
        TestJDBC.class,
        TestJPA.class,
})
public class AllTests {
}
