package net.sf.persism.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import net.sf.persism.Session;
import net.sf.persism.perf.Category;
import net.sf.persism.perf.PerfTest;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardApplication extends Application {

    private Properties settings;

    @Override
    public void init() throws Exception {
        super.init();
        settings = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/settings.properties")) {
            settings.load(in);
        } catch (Exception e) {
            System.out.println("no settings");
        }
    }

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

    private void createChart(String testName, DashboardController controller, List<PerfTest> list) throws IOException {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLength(0);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Milliseconds");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setStyle("-fx-padding: 0;");
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
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

        if (settings.getProperty("image.path") != null) {
            Platform.runLater(() -> {
                writeNodeToImage(lineChart, 500, 400, settings.getProperty("image.path") + testName + ".png");
            });
        }
    }

    private void writeNodeToImage(Node node, int width, int height, String fileName)  {
        Chart chart = (Chart) node;
        System.out.println(chart.getWidth() + " " + chart.getHeight());
        WritableImage writableImage = new WritableImage(width, height);
        node.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        //Write the snapshot to the chosen file
        try {
            ImageIO.write(renderedImage, "png", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch();
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