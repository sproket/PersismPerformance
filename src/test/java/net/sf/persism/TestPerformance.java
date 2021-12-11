package net.sf.persism;

import net.sf.persism.models.InventoryAdjustment;
import net.sf.persism.models.SalesOrder;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import static net.sf.persism.NadaPrintStream.out;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 2)
@Measurement(iterations = 4)
@Threads(4)
public class TestPerformance {

// https://mkyong.com/java/java-jmh-benchmark-tutorial/

    private Session session;
    private Connection con;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TestPerformance.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    public void setup() throws IOException, ClassNotFoundException, SQLException {

        long now = System.currentTimeMillis();

        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        out.println(url);

        con = DriverManager.getConnection(url, "tl", "tl");

        session = new Session(con);

        out.println("Time to init " + (System.currentTimeMillis() - now));

    }

    @Benchmark
    @RepeatedTest(2)
    public void testPersism() throws Exception {
        setup();

        long now = System.nanoTime();
        out.println("PERSISM");
        List<SalesOrder> salesOrders = session.query(SalesOrder.class);
        out.println("SalesOrder COUNT: " + salesOrders.size());

        List<InventoryAdjustment> items = session.query(InventoryAdjustment.class);
        out.println("InventoryAdjustment COUNT: " + items.size());

        out.println("Time to read " + ( (System.nanoTime() - now) / 1000000) );
    }

    @Benchmark
    @RepeatedTest(2)
    public void testPersism2() throws Exception {
        setup();

        long now = System.nanoTime();
        out.println("PERSISM2");
        List<SalesOrder> salesOrders = session.query(SalesOrder.class);
        out.println("SalesOrder COUNT: " + salesOrders.size());

        List<InventoryAdjustment> items = session.query(InventoryAdjustment.class);
        out.println("InventoryAdjustment COUNT: " + items.size());

        out.println("Time to read " + ( (System.nanoTime() - now) / 1000000) );
    }

    @Benchmark
    @RepeatedTest(2)
    public void testJDBC() throws Exception {
        setup();

        out.println("JDBC");

        long now = System.nanoTime();

        List<SalesOrder> salesOrders = new ArrayList<>();

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from SalesOrders");
        while (rs.next()) {
            SalesOrder ord = new SalesOrder();
            ord.setIdentity(rs.getObject("identity"));
            ord.setDate(rs.getDate("Date"));
            ord.setComments(rs.getString("Comments"));
            ord.setSalespersonName(rs.getString("SalesPersonName"));
            ord.setSalespersonId(rs.getObject("SalesPersonID"));
            ord.setClientName(rs.getString("ClientName"));
            ord.setClientId(rs.getObject("ClientID"));
            ord.setAdjustment(rs.getDouble("Adjustment"));
            ord.setCommission(rs.getDouble("Commission"));
            ord.setCurrencyName(rs.getString("CurrencyName"));
            ord.setCurrencyRate(rs.getDouble("CurrencyRate"));
            ord.setPoNumber(rs.getString("PONumber"));
            ord.setTotalCharge(rs.getDouble("TotalCharge"));
            ord.setCustomerNumber(rs.getString("CustomerNUmber"));
            ord.setShippedVia(rs.getString("ShippedVIA"));
            ord.setShipToAddress(rs.getString("ShipTOAddress"));
            ord.setClientAddress(rs.getString("ClientAddress"));
            ord.setControlNumber(rs.getString("ControlNumber"));
            ord.setStatus(rs.getShort("Status"));
            ord.setInvoiceDate(rs.getDate("InvoiceDate"));
            ord.setSubTotal(rs.getDouble("SubTotal"));
            ord.setTerms(rs.getString("Terms"));
            ord.setUsid(rs.getString("USID"));
            ord.setUserName(rs.getString("UserName"));
            ord.setTaxCode(rs.getInt("TaxCode"));
            ord.setRequestedShipDate(rs.getDate("RequestedShipDate"));
            ord.setWorkOrderPrintCount(rs.getInt("WorkOrderPrintCount"));

            salesOrders.add(ord);
        }
        out.println("SalesOrder COUNT: " + salesOrders.size());


        List<InventoryAdjustment> items = new ArrayList<>();
        rs = st.executeQuery("SELECT * FROM [InventoryAdjustments]");
        while (rs.next()) {
            InventoryAdjustment item = new InventoryAdjustment();
            item.setIdentity(rs.getInt("identity"));
            item.setItemId(rs.getString("ItemID"));
            item.setAdjustmentType(rs.getShort("AdjustmentType"));
            item.setAdjustmentDate(rs.getDate("AdjustmentDate"));
            item.setUserName(rs.getString("UserName"));
            item.setStockBefore(rs.getDouble("StockBefore"));
            item.setStockAfter(rs.getDouble("StockAfter"));
            item.setStockUnits(rs.getString("StockUnits"));
            item.setComments(rs.getString("Comments"));

            items.add(item);
        }
        out.println("InventoryAdjustment COUNT: " + items.size());

        out.println("Time to read " + ( (System.nanoTime() - now) / 1000000) );

    }
}
