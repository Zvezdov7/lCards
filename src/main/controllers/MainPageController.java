package main.controllers;

import com.sun.webkit.dom.KeyboardEventImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.dao.CardsDao;
import main.model.Card;

import java.io.IOException;

public class MainPageController {
    @FXML
    private VBox vBox;
    @FXML
    private TextField wordField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button addWordButton;
    @FXML
    private Button deleteRowButton;
    @FXML
    private Button showStatisticsButton;
    @FXML
    private Button startGameButton;
    @FXML
    private Button resetAllGrades;
    @FXML
    private TableView<Card> tableView;
    @FXML
    private
    TableColumn<Card, Integer> idColumn;
    @FXML
    private
    TableColumn<Card, String> wordColumn;
    @FXML
    private
    TableColumn<Card, String> descriptionColumn;
    @FXML
    private
    TableColumn<Card, Integer> gradeColumn;


    private CardsDao cardsDao = new CardsDao();
    private ObservableList<Card> cards = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Card, Integer>("id"));
        wordColumn.setCellValueFactory(new PropertyValueFactory<Card, String>("word"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Card, String>("description"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<Card, Integer>("grade"));

        refreshCards();
        tableView.setItems(cards);

        addWordButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            addNewWordFromTextFields();
        });
        descriptionField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addNewWordFromTextFields();
            }
        });
        tableView.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                delelteCardFromTable();
            }
        });
        deleteRowButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEven ->{
            delelteCardFromTable();
        });
        showStatisticsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mauseEvent -> {
            showStats();
        });
        startGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
            startGame();
        });
        resetAllGrades.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cardsDao.resetAllGrades();
            refreshCards();
        });

    }

    public void showStats() {
        Stage statStage = new Stage();
        try {
            Parent stat = FXMLLoader.load(getClass().getResource("../gui/statistics.fxml"));
            statStage.setTitle("Hello World");
            statStage.setScene(new Scene(stat, 640, 400));
            statStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        Stage statStage = new Stage();
        try {
            Parent stat = FXMLLoader.load(getClass().getResource("../gui/game.fxml"));
            statStage.setScene(new Scene(stat, 640, 400));
            statStage.showAndWait();
            cards.clear();
            cards.addAll(FXCollections.observableArrayList(cardsDao.getCards()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewWordFromTextFields() {
        Card card = cardsDao.addCard(wordField.getText().trim(), descriptionField.getText().trim());
        cards.add(card);
        wordField.clear();
        descriptionField.clear();
        wordField.requestFocus();
    }

    private void delelteCardFromTable() {
        Card selectedItem = tableView.getSelectionModel().getSelectedItem();
        cardsDao.deleteCard(selectedItem);
        cards.remove(selectedItem);
    }

    private void refreshCards() {
        cards.clear();
        cards.addAll(FXCollections.observableArrayList(cardsDao.getCards()));
    }
}
