import com.fasterxml.uuid.Generators;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Service {

    public static String randomColor() {
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        return String.format("#%06x", rand_num);
    }
    public static void generatePdf(CarDAO[] daos, String path, String title) throws FileNotFoundException, DocumentException {
        PdfPTable table = new PdfPTable(4);

        Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();

        document.add(new Paragraph("Invoice for company", font));
        document.add(new Paragraph("Seller : Car dealership", font));
        document.add(new Paragraph("Buyer : Client of dealership", font));
        document.add(new Paragraph("--", font));
        document.add(new Paragraph("--", font));



        table.addCell(new PdfPCell(new Phrase("No.", font) ));
        table.addCell(new PdfPCell(new Phrase("Price", font) ));
        table.addCell(new PdfPCell(new Phrase("Vat", font) ));
        table.addCell(new PdfPCell(new Phrase("Value", font) ));

        Integer sum = 0;

        int i = 0;
        for(CarDAO dao : daos) {

            table.addCell(new PdfPCell(new Phrase(Integer.toString(i), font) ));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(dao.price), font) ));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(dao.vat), font) ));

            Integer fullPrice = dao.price + dao.price * dao.vat;

            table.addCell(new PdfPCell(new Phrase(String.valueOf(fullPrice), font) ));

            sum += fullPrice;
            i += 1;
        }



        document.add(table);

        document.add(new Paragraph("To be paid " + sum.toString(), font));

        document.close();
    }
    public static CarDAO generateCar() {
        Random rand = new Random();
        CarDAO dao = new CarDAO();

        dao.year = String.valueOf(1998 + rand.nextInt(22));
        ArrayList<String> airbags = new ArrayList<String>(
                Arrays.asList("kierwocy", "boczne", "pasazera", "back")
        );

        String[] models = {
                "Fiat 500", "Citroen C3 I", "SAAB 9-3", "Toyota Camry" , "Toyota Yaris"
        };


        for (String airbag : airbags) {
            dao.airbags.add(new Airbag(airbag, rand.nextBoolean()));
        }

        dao.model = models[ rand.nextInt(models.length) ];
        dao.uuid  = Generators.randomBasedGenerator().generate().toString();
        dao.color = randomColor();
        dao.vat = 7 + rand.nextInt(12);
        dao.price = 11200 + rand.nextInt(50000);

        return dao;
    }

}
