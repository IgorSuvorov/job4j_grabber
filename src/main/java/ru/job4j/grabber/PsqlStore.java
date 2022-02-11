package ru.job4j.grabber;

import ru.job4j.grabber.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection connection;

    public PsqlStore(Properties properties) throws SQLException {
        try {
            Class.forName(properties.getProperty("driver-class-name"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        connection = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "insert into posts(id, title, link, description, created) values (?, ?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, post.getId());
            statement.setString(2, post.getTitle());
            statement.setString(3, post.getLink());
            statement.setString(4, post.getDescription());
            statement.setTimestamp(5, Timestamp.valueOf(post.getCreated()));
            statement.execute();
            try (ResultSet genKeys = statement.getGeneratedKeys()) {
                if (genKeys.next()) {
                    post.setId(genKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("select * from posts")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(savedPost(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement =
                     connection.prepareStatement("select * from posts where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = savedPost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return post;
    }

    public static Post savedPost(ResultSet resultSet) throws SQLException {
        Post post = new Post(resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("link"),
                resultSet.getString("description"),
                resultSet.getTimestamp("created").toLocalDateTime());
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}