import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        SqlCommand sql = new SqlCommand();
        long start = System.currentTimeMillis();

       sql.connectToDB();

       sql.paste(5000);

       sql.addValueToList(list);

       sql.createXML(list);

       sql.xsl("1.xml","2.xml","red.xsl");

       sql.parse();

       sql.closeAll();

        System.out.println("Время работы программы - "+(((System.currentTimeMillis() - start)/1000)/60) + " минут");


    }
}
