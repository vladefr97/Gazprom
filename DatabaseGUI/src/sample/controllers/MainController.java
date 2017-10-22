package sample.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
    public void showDialog(ActionEvent actionEvent) {
        try {

            Stage stage=new Stage();
            stage.setTitle("Добавление новой модели");
            stage.setMinWidth(650);
            stage.setMinHeight(300);
            Parent root= FXMLLoader.load(getClass().getResource("../fxml/addNew.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void showChooseModel(ActionEvent actionEvent) {
        try {

            Stage stage=new Stage();
            stage.setTitle("Выбор существующей модели");
            stage.setMinWidth(650);
            stage.setMinHeight(300);
            Parent root= FXMLLoader.load(getClass().getResource("../fxml/chooseModel.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void showFindModel(ActionEvent actionEvent) {
        try {

            Stage stage=new Stage();
            stage.setTitle("Поиск модели");
            stage.setMinWidth(650);
            stage.setMinHeight(350);
            Parent root= FXMLLoader.load(getClass().getResource("../fxml/findModel.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
