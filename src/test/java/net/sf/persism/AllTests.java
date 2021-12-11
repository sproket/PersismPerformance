package net.sf.persism;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({TestPersism.class, TestJDBC.class, TestJPA.class})
public class AllTests {
}
