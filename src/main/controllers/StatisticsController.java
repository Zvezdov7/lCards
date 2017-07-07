package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import main.dao.CardsDao;


/**
 * Created by Dmitry on 03.07.2017.
 */
public class StatisticsController {

    @FXML
    private TextArea firstGradeStatistics;
    @FXML
    private TextArea secondGradeStatistics;
    @FXML
    private TextArea  thirdGradeStatistics;

    CardsDao cardsDao = new CardsDao();

    @FXML
    public void initialize() {
        cardsDao.getCardsByGrade(1).stream().forEach((v) -> {
            System.out.println(v);
            firstGradeStatistics.appendText(v.getWord() + " : " + v.getDescription() + "\n");
        });
        cardsDao.getCardsByGrade(2).stream().forEach((v) -> {
            secondGradeStatistics.appendText(v.getWord() + " : " + v.getDescription() + "\n");
        });
        cardsDao.getCardsByGrade(3).stream().forEach((v) -> {
            thirdGradeStatistics.appendText(v.getWord() + " : " + v.getDescription() + "\n");
        });
    }

}
