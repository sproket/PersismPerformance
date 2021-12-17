package net.sf.persism;

import net.sf.persism.perf.PerfTest;

import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.Properties;

public abstract class BaseTest {

    private static final Log log = Log.getLogger(BaseTest.class);

    String testClassName;
    String testMethodName;

    private long now;

    private Connection con;
    private Session session;

    private static boolean dropAndRecreate = false;

    public void setUp() throws Exception {
//        String h2Home = createHomeFolder("perf");
//        String url = "jdbc:h2:" + h2Home + "/perf";
//
//        log.info(url);
//        con = DriverManager.getConnection(url, "sa", "");

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
                        TestClass VARCHAR(254) NOT NULL,
                        TestMethod VARCHAR(254) NOT NULL,
                        TestText VARCHAR(1024) NOT NULL,
                        Timing BIGINT NOT NULL,
                        TimingMS BIGINT NOT NULL,
                        StartTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
                    )
                    """;
            st.execute(sql);

            if (isTableInDatabase("PerfTestResults", con)) {
                st.execute("DROP TABLE PerfTestResults");
            }
        }
    }

    Connection getConnection() throws Exception {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        String user = props.getProperty("database.username");
        String pass = props.getProperty("database.password");
        return DriverManager.getConnection(url, user, pass);
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

        /*
        ID  CLASS                       METHOD              MESSAGE                                         TIMING      DateTime                MS
        199	net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2380753700 (2380) badges size: 1102019	2380753700	2021-12-13 14:12:04.903	2380
         */
        String outText = "TIME:  " + newNan + " (" + newMil + ") " + text;
        System.out.println(outText);
        PerfTest perfTest;
        perfTest = new PerfTest();
        perfTest.setTestClass(testClassName);
        perfTest.setTestMethod(testMethodName);
        perfTest.setTestText(outText);
        perfTest.setTiming(newNan);
        perfTest.setTimingMS(newMil);
        session.insert(perfTest);

        reset();
    }


    private static String createHomeFolder(String subFolder) {

        String home = System.getProperty("user.home");
        home = home.replace("\\", "/");
        home += "/" + subFolder;

        log.info("createHomeFolder: " + home);
        boolean success = new File(home).mkdirs();
        log.info("createHomeFolder success: " + success);
        return home;
    }

}
