package ru.saikalb.homework_3;

/* Повторить все, что было на семинаре на таблице Student с полями
1. id - bigint
2. first_name - varchar(256)
3. second_name - varchar(256)
4. group - varchar(128)
Написать запросы:
1. Создать таблицу
2. Наполнить таблицу данными (insert)
3. Поиск всех студентов
4. Поиск всех студентов по имени группы
Доп. задания:
1. ** Создать таблицу group(id, name); в таблице student сделать внешний ключ на group
2. ** Все идентификаторы превратить в UUID
Замечание: можно использовать ЛЮБУЮ базу данных: h2, postgres, mysql, ...*/

import java.sql.*;

public class Homework_3 {
    public static void main(String[] args) {
        // try with resources
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb2", "root", "root")) {

            acceptConnection(connection);

            // Выбрать данные о студентах по имени группы
            selectStudentsByGroup(connection, "'computer science'"); //здесь нужно вручную подставлять название группы

        } catch (SQLException e) {
            System.err.println("Не удалось подключиться к БД: " + e.getMessage());
        }
    }

    //Создать таблицу:
    static void acceptConnection(Connection connection) throws SQLException {
        // Statement - интерфейс, описывающий конкретный запрос в БД
        try (Statement statement = connection.createStatement()) {
            // execute, executeUpdate, executeQuery
            statement.execute("""
                    create table Student(
                      id bigint,
                      firstName varchar(256),
                      secondName varchar(256),
                      groupName varchar(128)
                    )
                    """);
        }
//Наполнить таблицу данными (insert):
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("""
                    insert into Student(id, firstName, secondName, groupName) values
                    (1, 'Jane', 'Smith', 'economics'),
                    (2, 'John', 'Kuderka', 'physics'),
                    (3, 'Peter', 'White', 'computer science'),
                    (4, 'Anna', 'Green', 'economics'),
                    (5, 'David', 'Black', 'physics'),
                    (6, 'Michael', 'Black', 'computer science'),
                    (7, 'Emily', 'Green', 'physics'),
                    (8, 'Olivia', 'Smith', 'economics'),
                    (9, 'Sophia', 'Kuderka', 'physics'),
                    (10, 'James', 'White', 'computer science')
                  
                    """);
            System.out.println("Количество вставленных строк: " + count);
        }

        // ResultSet Поиск всех студентов
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, firstName, secondName, groupName
                    from Student
                    """);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String secondName = resultSet.getString("secondName");
                String group = resultSet.getString("groupName");
                System.out.println("Прочитана строка: " + String.format("%s, %s, %s, %s", id, firstName, secondName, group));
            }
        }
    }

    //Поиск всех студентов по имени группы

    static void selectStudentsByGroup(Connection connection, String groupName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, firstName, secondName, groupName from Student where groupName = " + groupName);

            System.out.println("Студенты группы " + groupName + ":");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String secondName = resultSet.getString("secondName");
                String group = resultSet.getString("groupName");

                System.out.println(String.format("ID: %s, 1NAME: %s, 2NAME: %s, GROUP: %s", id, firstName, secondName, group));
            }

        }
    }
}
