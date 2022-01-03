package net.sf.persism.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {



    public VBox resultCharts;

    @FXML
    private Parent mooo;

    // private TestResultController testResultController;

    @FXML
    private VBox leaderboard;

    @FXML
    private PieChart testExtendedUsers;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println(url + " " + resourceBundle + " " + leaderboard + " " + testExtendedUsers + " " + welcomeText + " ");
//        leaderboard.getParent().
        //testResultController = mooo.
        //System.out.println(testResultController + " MOOO!");
    }

//    public TestResultController getTestResultController() {
//        return testResultController;
//    }
//
//    public void setTestResultController(TestResultController testResultController) {
//        this.testResultController = testResultController;
//    }
}