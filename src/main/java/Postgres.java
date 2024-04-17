import com.mongodb.client.MongoClients;
import models.Contact;
import models.User;
import org.bson.Document;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Postgres {
    public Connection connect_and_insert() {
        String url = "jdbc:postgresql://localhost:5432/somedb";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "someuser", "somepass");
            System.out.println("Connection to Postgres has been established");
            conn.createStatement().execute("create table if not exists \"Users\" (\n" +
                    "id INT generated always as identity,\n" +
                    "name varchar(20) not null,\n" +
                    "primary key(id));\n" +
                    "\n" +
                    "create table if not exists \"Contacts\" (\n" +
                    "id INT generated always as identity,\n" +
                    "\"customerId\" INT,\n" +
                    "\"contactName\" VARCHAR(255) not null,\n" +
                    "phone VARCHAR(15),\n" +
                    "email VARCHAR(100),\n" +
                    "primary key(id),\n" +
                    "constraint \"fk_Users_Contacts\"\n" +
                    "foreign key(\"customerId\")\n" +
                    "references \"Users\"(id)\n" +
                    ");");
            System.out.println("Tables created");

            String sqlCreate = "insert into \"Users\" (name) values\n" +
                    "('Konstantin'),\n" +
                    "('Nikolai'),\n" +
                    "('Roman');";
            Statement stmtCreate = conn.createStatement();
            stmtCreate.execute(sqlCreate);

            String sqlCreate2 = "insert into \"Contacts\" (\"customerId\",\"contactName\",phone,email) values\n" +
                    "(2,'Pushkin','1234567','email@lll.ru'),\n" +
                    "(6,'Zaitzev','4563456436','sefsefsefs@lll.ru'),\n" +
                    "(4,'Kurochkin','1234567','hehhehheh@lll.ru');";
            Statement stmtCreate2 = conn.createStatement();
            stmtCreate.execute(sqlCreate2);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Connection connectOnly() {
        String url = "jdbc:postgresql://localhost:5432/somedb";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "someuser", "somepass");
            System.out.println("Connection to Postgres has been established");
            conn.createStatement().execute("create table if not exists \"Users\" (\n" +
                    "id INT generated always as identity,\n" +
                    "name varchar(20) not null,\n" +
                    "primary key(id));\n" +
                    "\n" +
                    "create table if not exists \"Contacts\" (\n" +
                    "id INT generated always as identity,\n" +
                    "\"customerId\" INT,\n" +
                    "\"contactName\" VARCHAR(255) not null,\n" +
                    "phone VARCHAR(15),\n" +
                    "email VARCHAR(100),\n" +
                    "primary key(id),\n" +
                    "constraint \"fk_Users_Contacts\"\n" +
                    "foreign key(\"customerId\")\n" +
                    "references \"Users\"(id)\n" +
                    ");");
            System.out.println("Tables created");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void selectAll() {
        String sql = "select u.id as \"Users -> id\", u.name as \"Users -> name\"," +
                " c.\"customerId\", c.id as \"contactId\", c.\"contactName\",c.phone ,c.email  from \"Users\" " +
                "u left join \"Contacts\" c ON c.\"customerId\" = u.id order by \"Users -> id\";";
        List<User> users = new ArrayList<>();
        User tmpUser = null;
        List<Contact> tmpContacts = new ArrayList<>();
        try (Connection conn = this.connectOnly()) {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                if (tmpUser != null && tmpUser.id != rs.getInt("Users -> id")) {
                    tmpUser.contacts = tmpContacts;
                    users.add(tmpUser);
                    tmpUser = null;
                    tmpContacts = new ArrayList<>();
                }
                if (tmpUser == null) tmpUser = new User(rs.getInt("Users -> id"),
                        rs.getString("Users -> name"));

                if (tmpUser.id == rs.getInt("customerId")) {
                    tmpContacts.add(new Contact(rs.getInt("contactId"), rs.getInt("Users -> id"),
                            rs.getString("contactName"), rs.getString("phone"),
                            rs.getString("email")));

                    MongoDB.putLogsinMongoDB(rs);
                }
            }
            if (tmpUser != null) {
                tmpUser.contacts = tmpContacts;
                users.add(tmpUser);
            }
        } catch (SQLException e) {
            System.out.println("");
        }
        System.out.println("\nВсе пользователи и их контакты:");
        System.out.println(users);
    }
}