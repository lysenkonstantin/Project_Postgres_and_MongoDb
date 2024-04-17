import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.function.Consumer;

public class MongoDB {
    static void putLogsinMongoDB(ResultSet rs) throws SQLException {
        var meta = new HashMap<String, Object>();
        meta.put("action", "print contact");
        meta.put("time", LocalDateTime.now());
        meta.put(("newContactId"), rs.getInt("contactId"));

        var mongoClient = MongoClients.create();
        var database = mongoClient.getDatabase("syn");
        var contactCollection = database.getCollection("nContacts");

        contactCollection.insertOne(new Document(meta));
        mongoClient.close();
    }
    static void printLogsinMongoDB() throws SQLException {
        var mongoClient = MongoClients.create();
        var database = mongoClient.getDatabase("syn");
        var contactCollection = database.getCollection("nContacts");
        System.out.println("Логирование печати пользователей и их контактов:");
        contactCollection.find().forEach((Consumer<Document>) System.out::println);
        mongoClient.close();
    }
}