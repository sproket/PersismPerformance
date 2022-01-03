package net.sf.persism.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import net.sf.persism.Session;
import net.sf.persism.perf.PerfTest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {
    @FXML
    private TableView<PerfTest> leaders;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            var service = new PollingService(new Session(DashboardApplication.getConnection()));
            service.setPeriod(Duration.seconds(1));
            service.setOnSucceeded(workerStateEvent -> {
                List<PerfTest> results = (List<PerfTest>) workerStateEvent.getSource().getValue();
                leaders.getItems().addAll(results);
            });
            service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
