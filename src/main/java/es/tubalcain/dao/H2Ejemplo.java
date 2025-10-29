package es.tubalcain.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class H2Ejemplo {
    public static void main(String[] args) {
        String url = "jdbc:h2:./data/testdb"; // o mem:testdb
        String user = "sa";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Crear tabla
            stmt.execute("CREATE TABLE IF NOT EXISTS alumnos (id INT PRIMARY KEY, nombre VARCHAR(100));");

            // Insertar datos
            stmt.executeUpdate("INSERT INTO alumnos VALUES (1, 'N');");

            // Consultar datos
            ResultSet rs = stmt.executeQuery("SELECT * FROM alumnos;");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


