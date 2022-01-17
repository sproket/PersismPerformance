package net.sf.persism.gui;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import net.sf.persism.Session;
import net.sf.persism.perf.Category;
import net.sf.persism.perf.PerfTest;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {

    @FXML
    private TextField txFilter;

    @FXML
    private ComboBox<Category> cbFilter;

    @FXML
    private TableView<PerfTest> tblResults;

    List<PerfTest> results;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            var service = new PollingService(new Session(DashboardApplication.getConnection()));
            service.setPeriod(Duration.seconds(1));
            service.setOnSucceeded(workerStateEvent -> {
                if (results == null) {
                    results = (List<PerfTest>) workerStateEvent.getSource().getValue();
                    tblResults.getItems().addAll(results);
                }
            });
            service.start();

            cbFilter.setItems(FXCollections.observableList(Arrays.asList(Category.values())));
            cbFilter.setValue(Category.Any);

            cbFilter.setOnAction(this::filterChanged);
            txFilter.setOnKeyTyped(this::filterChanged);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterChanged(Event event) {

        Category category = cbFilter.getValue();
        String txt = txFilter.getText().toLowerCase(Locale.ROOT);

        tblResults.getItems().clear();
        if (category == Category.Any && txt.equals("")) {
            tblResults.getItems().addAll(results);
            tblResults.sort();
            return;
        }

        var res = results.stream().filter(perfTest -> {

            var isTxt = perfTest.getTestClass().toLowerCase(Locale.ROOT).contains(txt)
                    || perfTest.getTestMethod().toLowerCase(Locale.ROOT).contains(txt);

            var isCat = category == Category.Any || perfTest.getCategory() == category;

            return isCat && isTxt;

        }).toList();

        tblResults.getItems().addAll(res);
        tblResults.sort();
    }
}
