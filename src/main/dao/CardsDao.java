package main.dao;

import main.model.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Dmitry on 03.07.2017.
 */
public class CardsDao {
    private static final String URL = "jdbc:h2:~/test";
    private static final String GET_CARDS = "SELECT * FROM TEST";
    private static final String GET_CARDS_BY_GRADE = "SELECT * FROM TEST WHERE GRADE=?";
    private static final String ADD_CARD = "INSERT INTO TEST VALUES(?, ?, ?, ?)";
    private static final String UPDATE_GRADE_FOR_CARD = "UPDATE TEST SET GRADE = ? WHERE ID=?;";
    private static final String RESETL_ALL_GRADES = "UPDATE TEST SET GRADE = 1 WHERE ID>0";
    private static final String DELETE_CARD = "DELETE FROM TEST WHERE ID=?";
    private static Connection dbconnection = null;

    private synchronized Connection getConnection() throws SQLException {
        if (dbconnection != null && !dbconnection.isClosed()) {
            return dbconnection;
        } else {
            try {
                dbconnection = DriverManager.getConnection(URL, "sa", "");
                DatabaseMetaData dbmd;
                dbmd = dbconnection.getMetaData();
                System.out.println("\n----------------------------------------------------");
                System.out.println("Database Name    = " + dbmd.getDatabaseProductName());
                System.out.println("Database Version = " + dbmd.getDatabaseProductVersion());
                System.out.println("Driver Name      = " + dbmd.getDriverName());
                System.out.println("Driver Version   = " + dbmd.getDriverVersion());
                System.out.println("Database URL     = " + dbmd.getURL());
                System.out.println("----------------------------------------------------");
                // Use the database connection somehow.
                return dbconnection;
            } catch (SQLException se) {
                se.printStackTrace();
            }
            throw new RuntimeException("Can't establish a connection");
        }
    }

    private PreparedStatement getPreparedStatement(Connection connection, String query, PreparedStatementConsnumer... consumers) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (PreparedStatementConsnumer consumer : consumers) {
            consumer.accept(preparedStatement);
        }
        return preparedStatement;

    }

    private interface PreparedStatementConsnumer {
        void accept(PreparedStatement preparedStatement) throws SQLException;
    }


    public List<Card> getCards() {
        List<Card> list = new ArrayList<>();
        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(GET_CARDS);
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String word = resultSet.getString(2);
                String description = resultSet.getString(3);
                int grade = resultSet.getInt(4);
                if (word != null && description != null) {
                    list.add(new Card(id, word, description, grade));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Card addCard(String word, String description) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = getPreparedStatement(
                        connection,
                        ADD_CARD,
                        p -> p.setString(1, null),
                        p -> p.setString(2, word),
                        p -> p.setString(3, description),
                        p -> p.setString(4, "1"));
        ) {
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
                System.out.println("Добавлено слово: " + id + word + description + " 1");
            }
            return new Card(id, word, description, 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Can't add the card");
    }

    public List<Card> getCardsByGrade(int grade) {
        List<Card> list = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = getPreparedStatement(connection, GET_CARDS_BY_GRADE, p -> p.setString(1, String.valueOf(grade)));
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String word = resultSet.getString(2);
                String description = resultSet.getString(3);
                list.add(new Card(id, word, description, grade));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateGrade(Card card, int grade) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = getPreparedStatement(
                        connection,
                        UPDATE_GRADE_FOR_CARD,
                        p -> p.setString(1, String.valueOf(grade)),
                        p -> p.setString(2, String.valueOf(card.getId())))
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCard(Card card) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = getPreparedStatement(connection, DELETE_CARD, p -> p.setString(1, String.valueOf(card.getId())))
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resetAllGrades() {
        try (
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(RESETL_ALL_GRADES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

