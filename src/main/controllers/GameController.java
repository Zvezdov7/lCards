package main.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.dao.CardsDao;
import main.model.Card;

import java.util.List;
import java.util.Random;

/**
 * Created by Dmitry on 03.07.2017.
 */
public class GameController {
    @FXML
    private Label gradeLabel;
    @FXML
    private Label wordLabel;
    @FXML
    private Label descriptionLabel;

    @FXML
    private Button knowButton;
    @FXML
    private Button doNotKnowButton;
    @FXML
    RadioButton firstGradeRadioButton;
    @FXML
    RadioButton secondGradeRadioButton;
    @FXML
    RadioButton thirdGradeRadioButton;
    @FXML
    ToggleGroup gradeToggleGroup = new ToggleGroup();


    private CardsDao cardsDao = new CardsDao();
    private List<Card> cardsByGrade;
    private Card currentCard;

    @FXML
    public void initialize() {
        wordLabel.setWrapText(true);
//        wordLabel.setPrefWidth(400);
        wordLabel.setMaxWidth(200);
        descriptionLabel.setWrapText(true);
//        descriptionLabel.setPrefWidth(400);
        descriptionLabel.setMaxWidth(200);
        prepareRadioButtons();
        loadCards();
        descriptionLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            descriptionLabel.setText(currentCard.getDescription());
        });

        knowButton.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
            cardsDao.updateGrade(currentCard, currentCard.getGrade() + 1);
            cardsByGrade.remove(currentCard);
            if (cardsByGrade.isEmpty()) {
                Stage stage = (Stage) knowButton.getScene().getWindow();
                stage.close();
                return;
            }
            nextCard();
            showCard(currentCard);
        });
        doNotKnowButton.addEventHandler(MouseEvent.MOUSE_CLICKED, m -> {
            nextCard();
            showCard(currentCard);
        });
        gradeToggleGroup.selectedToggleProperty().addListener(observable -> {
            loadCards();
        });

    }

    private void nextCard() {
        Random random = new Random();
        int i = random.nextInt(cardsByGrade.size());
        currentCard = cardsByGrade.get(i);
    }
    private void showCard(Card card) {
        wordLabel.setText(currentCard.getWord());
        descriptionLabel.setText("     ");
    }
    private void prepareRadioButtons() {
        firstGradeRadioButton.setToggleGroup(gradeToggleGroup);
        secondGradeRadioButton.setToggleGroup(gradeToggleGroup);
        thirdGradeRadioButton.setToggleGroup(gradeToggleGroup);
        firstGradeRadioButton.setUserData(1);
        secondGradeRadioButton.setUserData(2);
        thirdGradeRadioButton.setUserData(3);
        firstGradeRadioButton.fire();
    }
    private void loadCards() {
        descriptionLabel.setText("");
        int grade = (int) gradeToggleGroup.getSelectedToggle().getUserData();
        cardsByGrade = cardsDao.getCardsByGrade(grade);
        gradeLabel.setText("Группа: " + grade + " | Карточек в группе: " + cardsByGrade.size());
        if (cardsByGrade.isEmpty()) {
            wordLabel.setText("No cards");
            knowButton.setDisable(true);
            doNotKnowButton.setDisable(true);
            descriptionLabel.setDisable(true);
        } else {
            descriptionLabel.setDisable(false);
            knowButton.setDisable(false);
            doNotKnowButton.setDisable(false);
            nextCard();
            showCard(currentCard);
        }
    }
}
