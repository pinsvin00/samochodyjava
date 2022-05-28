import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class CarDAO {
    public int id = 0;
    public String uuid;
    public String model;
    public String year;
    public ArrayList<Airbag> airbags;
    public String color;
    public Invoice invoice;
    public Integer price;
    public Integer vat;
    public String saleTime;

    public String getImageName() {
        String[] strings = model.split(" ");
        StringBuilder ret = new StringBuilder();

        for (int i = 0 ; i < strings.length; i++) {
            ret.append(strings[i]);
            if(i != strings.length - 1) {
                ret.append("_");
            }
        }

        return ret.toString();
    }

    public CarDAO(int id, String model, String year, ArrayList<Airbag> airbags, String color) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.airbags = airbags;
        this.color = color;
        this.invoice = null;
    }

    public CarDAO() {
        airbags = new ArrayList<>();        Random random = new Random();
        int minDay = (int) LocalDate.of(1998, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2019, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);         saleTime = randomBirthDate.toString();
    }
}
