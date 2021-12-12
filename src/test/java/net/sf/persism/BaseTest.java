package net.sf.persism;

import net.sf.persism.perf.PerfTest;
import net.sf.persism.perf.PerfTestResult;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

public abstract class BaseTest {

    String testClassName;
    String testMethodName;

    long now;

    private int currentTestId;
    private Connection con;
    private Session session;

    private static boolean dropAndRecreate = true;

    public void setUp() throws Exception {
        con = getConnection();
        createTestResultTables();

        session = new Session(con);
        PerfTest perfTest = new PerfTest();
        perfTest.setDescription("some test..... started");
        perfTest.setStartTime(LocalDateTime.now());

        session.insert(perfTest);
        currentTestId = perfTest.getId();
    }

    public void tearDown() throws Exception {
        if (con != null) {
            con.close();
        }
    }

    private void createTestResultTables() throws Exception {
        if (!dropAndRecreate) {
            return;
        }
        dropAndRecreate = false;

        String sql = "";

        try (Statement st = con.createStatement()) {
            if (isTableInDatabase("PerfTests", con)) {
                st.execute("DROP TABLE PerfTests");
            }

            sql = """
                    CREATE TABLE PerfTests (
                        [ID] [int] IDENTITY(1,1) NOT NULL,
                        [Description] VARCHAR(1024) NOT NULL,
                        [StartTime] DATETIME NOT NULL,
                    )
                    """;
            st.execute(sql);

            if (isTableInDatabase("PerfTestResults", con)) {
                st.execute("DROP TABLE PerfTestResults");
            }

            sql = """
                    CREATE TABLE PerfTestResults (
                        [ID] [int] IDENTITY(1,1) NOT NULL,
                        [TEST_ID] [int] NOT NULL,
                        [Description] VARCHAR(1024) NOT NULL,
                        [TEST_CLASS] VARCHAR(254) NOT NULL,
                        [TEST_METHOD] VARCHAR(254) NOT NULL,
                        [Timing] [bigint] NOT NULL
                    )
                    """;
            st.execute(sql);
        }
    }

    private Connection getConnection() throws Exception {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        System.out.println(url);

        return DriverManager.getConnection(url);
    }

    private boolean isTableInDatabase(String tableName, Connection con) throws Exception {

        boolean result = false;
        DatabaseMetaData dma = con.getMetaData();

        String[] types = {"TABLE"};

        try (ResultSet rs = dma.getTables(null, null, null, types)) {

            while (rs.next()) {
                if (tableName.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }


    void out(Object text) {
        long newNan = (System.nanoTime() - now);
        long newMil = newNan / 1000000;

        String desc = "TIME:  " + newNan + " (" + newMil + ") " + text;
        System.out.println(desc);
        System.out.println("TEST NAME? " + testClassName + " " + testMethodName);

        PerfTestResult perfTestResult = new PerfTestResult();
        perfTestResult.setTestId(currentTestId);
        perfTestResult.setDescription(desc);
        perfTestResult.setTestClass(testClassName);
        perfTestResult.setTestMethod(testMethodName);
        perfTestResult.setTiming(newNan);
        session.insert(perfTestResult);

        now = System.nanoTime();
    }
}
