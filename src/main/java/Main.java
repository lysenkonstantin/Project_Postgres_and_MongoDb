import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Postgres app = new Postgres();
        app.selectAll();
        MongoDB.printLogsinMongoDB();

    }
}
