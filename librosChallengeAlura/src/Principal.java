// Importar las clases necesarias
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

// Clase principal
public class Principal {
    private static Connection connection;
    private static String user = "postgres";
    private static String pass = "C1r3ccolombia";
    private static String dbUrl = "jdbc:postgresql://localhost:5432/libros";

    public static void main(String[] args) {
        try {
            // Establecer la conexión a la base de datos
            connection = DriverManager.getConnection(dbUrl, user, pass);
            System.out.println("Conectado con éxito");

            Scanner scanner = new Scanner(System.in);
            int option;

            // Menú principal
            do {
                System.out.println("Menú:");
                System.out.println("1. Listar libros por título");
                System.out.println("2. Listar libros registrados");
                System.out.println("3. Buscar todos los autores");
                System.out.println("4. Listar libros por idioma");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                option = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (option) {
                    case 1:
                        listarLibrosPorTitulo(scanner);
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        buscarAutores();
                        break;
                    case 4:
                        listarLibrosPorIdioma(scanner);
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (option != 0);

            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para listar libros por título
    private static void listarLibrosPorTitulo(Scanner scanner) {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();

        try {
            // Realizar la consulta a la API
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://gutendex.com/books?search=" + titulo))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Imprimir el código de estado de la respuesta y la respuesta para depuración
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println("Código de estado de la respuesta: " + statusCode);
            System.out.println("Respuesta de la API: " + responseBody);

            // Si el código de estado no es 200, no intentar parsear el JSON
            if (statusCode != 200) {
                System.out.println("Error: La API devolvió un código de estado no exitoso.");
                return;
            }

            // Parsear la respuesta JSON
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray books = jsonResponse.getJSONArray("results");

            // Mostrar la información de los libros
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                String title = book.getString("title");
                String author = book.getJSONArray("authors").getJSONObject(0).getString("name");
                int downloadCount = book.getInt("download_count");
                String language = book.getJSONArray("languages").getString(0);

                System.out.println("Título: " + title);
                System.out.println("Autor: " + author);
                System.out.println("Idioma: " + language);
                System.out.println("Descargas: " + downloadCount);

                // Insertar en la base de datos
                String sql = "INSERT INTO libro (titulo, autor, idioma, numero_de_descargas) VALUES ('"
                        + title.replace("'", "''") + "', '"
                        + author.replace("'", "''") + "', '"
                        + language + "', "
                        + downloadCount + ")";
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para listar libros registrados
    private static void listarLibrosRegistrados() {
        try {
            String sql = "SELECT * FROM libro";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Verificar si hay registros
            if (!rs.next()) {
                System.out.println("No se encontraron registros de libros.");
                return;
            }

            // Mostrar la información de los libros registrados
            do {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String idioma = rs.getString("idioma");
                int numeroDeDescargas = rs.getInt("numero_de_descargas");

                System.out.println("ID: " + id);
                System.out.println("Título: " + titulo);
                System.out.println("Autor: " + autor);
                System.out.println("Idioma: " + idioma);
                System.out.println("Descargas: " + numeroDeDescargas);
                System.out.println();
            } while (rs.next());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para buscar todos los autores
    private static void buscarAutores() {
        try {
            String sql = "SELECT DISTINCT autor FROM libro";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Mostrar todos los autores
            while (rs.next()) {
                String autor = rs.getString("autor");
                System.out.println("Autor: " + autor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para listar libros por idioma
    private static void listarLibrosPorIdioma(Scanner scanner) {
        System.out.print("Ingrese el idioma: ");
        String idioma = scanner.nextLine();

        try {
            String sql = "SELECT * FROM libro WHERE idioma = '" + idioma + "'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Mostrar la información de los libros por idioma
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int numeroDeDescargas = rs.getInt("numero_de_descargas");

                System.out.println("ID: " + id);
                System.out.println("Título: " + titulo);
                System.out.println("Autor: " + autor);
                System.out.println("Idioma: " + idioma);
                System.out.println("Descargas: " + numeroDeDescargas);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
