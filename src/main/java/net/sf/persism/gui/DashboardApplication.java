package net.sf.persism.gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import net.sf.persism.Session;
import net.sf.persism.perf.Category;
import net.sf.persism.perf.PerfTest;

import javax.swing.plaf.synth.Region;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(DashboardApplication.class.getResource("/gui/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1050, 600);
        DashboardController controller = fxmlLoader.getController();
        Session session = new Session(DashboardApplication.getConnection());
        List<PerfTest> list = session.query(PerfTest.class).stream().sorted(Comparator.comparing(PerfTest::getTestMethod)).toList();

        Set<String> testNames = list.stream().map(PerfTest::getTestMethod).collect(Collectors.toCollection(LinkedHashSet::new));

        for (String testName : testNames) {
            createChart(testName, controller, list);
        }

        stage.setTitle("Performance data");
        stage.setScene(scene);

        stage.show();
    }

    private void createChart(String testName, DashboardController controller, List<PerfTest> list) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLength(0);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Milliseconds");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setStyle("-fx-padding: 0;");
        lineChart.setLegendVisible(false);
        lineChart.setTitle(testName);

        List<PerfTest> result = list.stream().
                filter(p -> p.getCategory() == Category.Result && testName.equals(p.getTestMethod())).
                sorted(Comparator.comparing(PerfTest::getTestClass)).
                toList();

        for (PerfTest perfTest : result) {
            var series = new XYChart.Series<String, Number>();

            series.setName(perfTest.getId() + "");
            series.getData().add(new XYChart.Data<>(perfTest.getTestClass() + " (" + perfTest.getLineCount() + ")", perfTest.getTimingMS()));

            lineChart.getData().add(series);
        }
        controller.resultCharts.getChildren().add(lineChart);
    }

    public static void main(String[] args) {
        Application.launch();
    }

    private XYChart.Series createSeries(String name) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        final ObservableList data = series.getData();
//        final DataGenerator generator = new DataGenerator();
//        for (String month: MONTHS) {
//            data.add(generator.createDataItem(month));
//        }

        return series;
    }

    static Connection getConnection() throws Exception {
        Properties props = new Properties();
        props.load(DashboardApplication.class.getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        String user = props.getProperty("database.username");
        String pass = props.getProperty("database.password");
        return DriverManager.getConnection(url, user, pass);
    }

}