import static spark.Spark.*;

import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import spark.Request;
import spark.Response;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {
    public static ArrayList<CarDAO> cars = new ArrayList<CarDAO>();
    public static ArrayList<GroupInvoice> groupInvoices = new ArrayList<GroupInvoice>();
    public static void main(String[] args) {
        port(5000);

        externalStaticFileLocation(new File("").getAbsolutePath() + "/src/main/resources/public");

        post("/add", App::addCarPost);
        post("/json", App::getJsonPost);
        post("/delete", App::deleteCarPost);
        post("/update", App::updateCarPost);
        post("/pdfSingle", App::pdfSingle);
        post("/pdfManyAll", App::pdfManyAll);
        post("/pdfManyPrice", App::pdfManyPrice);
        post("/pdfManyYear", App::pdfManyYear);
        get("/generate", App::generateCars);
        get("/getInvoices", App::getGroupInvoices);

    }

    static String getGroupInvoices(Request req, Response res) {
        Gson gson = new Gson();
        return gson.toJson(groupInvoices);
    }


    static String addCarPost(Request req, Response res){
        res.type("application/json");
        Gson gson = new Gson();
        CarDAO newCar = gson.fromJson(req.body(), CarDAO.class);

        newCar.uuid = Generators.randomBasedGenerator().generate().toString();

        cars.add(newCar);
        return gson.toJson(newCar);
    }
    static String getJsonPost(Request req, Response res){
        res.type("application/json");
        Gson gson = new Gson();
        return gson.toJson(cars);
    }

    static String deleteCarPost(Request req, Response res){
        res.type("text/html");
        Gson gson = new Gson();

        String uuid = gson.fromJson(req.body(), CarDAO.class).uuid;

        boolean deleted = cars.removeIf(car -> Objects.equals(car.uuid, uuid));

        return deleted ? "true" : "false";
    }

    static String generateCars(Request req, Response res) {
        for (int i = 0 ; i < 5; i++) {
            cars.add(Service.generateCar());
        }

        return "{\"ok\" : \"ok\" }";
    }


    static String pdfManyAll(Request req, Response res) throws FileNotFoundException, DocumentException {
        Gson gson =  new Gson();
        CarDAO[] daos = gson.fromJson(req.body(), CarDAO[].class);
        GroupInvoice groupInvoice = new GroupInvoice();

        String realPath = String.format("src\\main\\resources\\public\\katalog\\%s.pdf", groupInvoice.time);
        String serverPath =  String.format("katalog\\%s.pdf", groupInvoice.time);

        groupInvoice.group = 1;
        groupInvoice.time = String.valueOf(System.currentTimeMillis());
        groupInvoice.path = serverPath;

        groupInvoices.add(groupInvoice);


        Service.generatePdf(daos, realPath, groupInvoice.title);

        return "{\"ok\" : \"ok\" }";
    }

    static String pdfManyPrice(Request req, Response res) throws FileNotFoundException, DocumentException {
        Gson gson =  new Gson();
        CarDAO[] daos = gson.fromJson(req.body(), CarDAO[].class);
        GroupInvoice groupInvoice = new GroupInvoice();

        groupInvoice.group = 2;

        groupInvoice.time = String.valueOf(System.currentTimeMillis());
        String serverPath =  String.format("katalog\\%s.pdf", groupInvoice.time);
        String realPath = String.format("src\\main\\resources\\public\\katalog\\%s.pdf", groupInvoice.time);

        groupInvoice.path = serverPath;

        groupInvoices.add(groupInvoice);

        Service.generatePdf(daos, realPath, groupInvoice.title);
        return "{\"ok\" : \"ok\" }";
    }

    static String pdfManyYear(Request req, Response res) throws FileNotFoundException, DocumentException {
        Gson gson =  new Gson();
        CarDAO[] daos = gson.fromJson(req.body(), CarDAO[].class);
        GroupInvoice groupInvoice = new GroupInvoice();

        groupInvoice.group = 3;

        String realPath = String.format("src\\main\\resources\\public\\katalog\\%s.pdf", groupInvoice.time);

        groupInvoice.path = String.format("katalog\\%s.pdf", groupInvoice.time);
        groupInvoice.time = String.valueOf(System.currentTimeMillis());

        groupInvoices.add(groupInvoice);

        Service.generatePdf(daos, realPath, groupInvoice.title);

        return "{\"ok\" : \"ok\" }";
    }



    static String pdfSingle(Request req, Response res) throws IOException, DocumentException {

        Gson gson = new Gson();
        CarDAO dao = gson.fromJson(req.body(), CarDAO.class);

        Document document = new Document();
        String path = String.format("src\\main\\resources\\public\\katalog\\%s.pdf", dao.uuid);
        String serverPath = String.format("katalog\\%s.pdf", dao.uuid);
        PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();

        Color color = Color.decode(dao.color);
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font fontBold = FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD);
        Font fontColor = FontFactory.getFont(FontFactory.COURIER, 16, new BaseColor(color.getRed(), color.getGreen(), color.getBlue() ) );

        Paragraph chunk = new Paragraph(String.format("Invoice for, %s", dao.uuid), fontBold);
        document.add(chunk);
        chunk = new Paragraph(String.format("Model, %s", dao.model), font);
        document.add(chunk);
        chunk = new Paragraph(String.format("Year, %s", dao.year), font);


        document.add(chunk);
        chunk = new Paragraph(String.format("Color : %s", dao.color), fontColor);

        document.add(chunk);
        chunk = new Paragraph(String.format("Drivers airbag, %s", dao.airbags.get(0).value ), font);

        document.add(chunk);
        chunk = new Paragraph(String.format("Passenger airbag, %s", dao.airbags.get(1).value), font);

        document.add(chunk);
        chunk = new Paragraph(String.format("Rear airbags, %s", dao.airbags.get(2).value), font);

        document.add(chunk);
        chunk = new Paragraph(String.format("Side airbags, %s", dao.airbags.get(3).value), font);
        document.add(chunk);

        Image img = Image.getInstance(String.format("src\\main\\resources\\public\\imgs\\%s.png", dao.getImageName()));
        img.scaleAbsolute(200, 200);
        document.add(img);



        document.close();



        for (int i = 0 ; i < cars.size(); i++) {
            if(cars.get(i).uuid.equals(dao.uuid)) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                Invoice invoice = new Invoice();
                invoice.date = dtf.format(now);
                invoice.pdfPath = serverPath;

                cars.get(i).invoice = invoice;
            }
        }

        return "{\"ok\" : \"ok\" }";
    }


    static String updateCarPost(Request req, Response res){
        res.type("application/json");
        Gson gson = new Gson();
        CarDAO updatedCar = gson.fromJson(req.body(), CarDAO.class);

        cars.indexOf(updatedCar);

        cars.removeIf(oldCar -> Objects.equals(oldCar.uuid, updatedCar.uuid));
        cars.add(updatedCar);

        return gson.toJson(updatedCar);
    }

}
