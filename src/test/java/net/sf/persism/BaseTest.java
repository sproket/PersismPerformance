package net.sf.persism;

import net.sf.persism.perf.PerfTest;

import java.sql.*;
import java.util.Properties;

public abstract class BaseTest {

    String testClassName;
    String testMethodName;

    private long now;

    private Connection con;
    private Session session;

    private static boolean dropAndRecreate = true;

    public void setUp() throws Exception {
        con = getConnection();
        createTestResultTables();

        session = new Session(con);

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
                        [TestClass] VARCHAR(254) NOT NULL,
                        [TestMethod] VARCHAR(254) NOT NULL,
                        [TestText] VARCHAR(1024) NOT NULL,
                        [Timing] [bigint] NOT NULL,
                        [StartTime] DATETIME default current_timestamp
                    )
                    """;
            st.execute(sql);

            if (isTableInDatabase("PerfTestResults", con)) {
                st.execute("DROP TABLE PerfTestResults");
            }
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


    void reset() {
        now = System.nanoTime();
    }

    void out(Object text) {
        long newNan = (System.nanoTime() - now);
        long newMil = newNan / 1_000_000;

        String outText = "TIME:  " + newNan + " (" + newMil + ") " + text;

        PerfTest perfTest;
        perfTest = new PerfTest();
        perfTest.setTestClass(testClassName);
        perfTest.setTestMethod(testMethodName);
        perfTest.setTestText(outText);
        perfTest.setTiming(newNan);
        session.insert(perfTest);

        reset();
    }
}
