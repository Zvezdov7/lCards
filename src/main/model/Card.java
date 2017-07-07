package main.model;

/**
 * Created by Dmitry on 03.07.2017.
 */
public class Card {
    private int id;
    private String word;
    private String description;
    private int grade;

    public Card(int id, String word, String description, int grade) {
        this.id = id;
        this.word = word;
        this.description = description;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", description='" + description + '\'' +
                ", grade=" + grade +
                '}';
    }
}
