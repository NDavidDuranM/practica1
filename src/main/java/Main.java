import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) {

        Scanner lectorString = new Scanner(System.in);
        System.out.print("Digite una URL valida --> ");
        String url = lectorString.nextLine();

        System.out.println("\nLista de operaciones realizadas:\n");

        try {
            Connection conexion = Jsoup.connect(url);
            Connection.Response respuesta_conexion = conexion.execute();

            int cant_lineas = respuesta_conexion.body().split("\n").length;
            System.out.println("* Numero de lineas del recurso retornado --> " + cant_lineas);

            Document doc = respuesta_conexion.parse();
            System.out.println("* Numero de parrafos (p) del HTML --> " + doc.select("p").size());
            System.out.println("* Numero de imagenes (img) dentro de cada parrafo del HTML --> " + doc.select("p > img").size());

            Elements formularios = doc.select("form");
            Elements postForms = formularios.select("form[method='post']");
            System.out.println("* Numero de formularios (form) con POST --> " + postForms.size());

            Elements getForms = formularios.select("form[method='get']");
            System.out.println("* Numero de formularios (form) con GET --> " + getForms.size());

            System.out.println("* Campos del tipo input con sus respectivos tipos:");
            for (int x = 0; x < formularios.size(); x++) {
                System.out.println("- Formulario [" + (x + 1) + "]");

                Elements inputs = formularios.select("input");

                for (int d = 0; d < inputs.size(); d++) {
                    System.out.println("\tTipo de input (" + (d + 1) + ") --> " + inputs.get(d).attr("type"));
                }
            }

            System.out.println("\n* Respuesta de la peticion al servidor:\n");
            for (Element form :
                    postForms) {
                Document DocumentoFormulario = Jsoup.connect(form.attr("abs:action"))
                        .data("asignatura", "practica1")
                        .header("matricula", "20171056")
                        .post();
                System.out.println(DocumentoFormulario.body());
                System.out.println("\n");
            }

        } catch (IOException ioe) {
            System.out.println("La URL no es valida...");
        } catch (IllegalArgumentException iae) {
            System.out.println("La URL no es valida... ");
        }
        lectorString.close();
    }
}