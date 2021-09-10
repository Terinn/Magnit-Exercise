import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

       SqlCommand.connectToDB();
       SqlCommand.paste(10);
       SqlCommand.addValueToList(list);
       SqlCommand.createXML(list);

    }
}
