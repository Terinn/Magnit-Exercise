
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlCommand {

    private static final String url = "jdbc:mysql://localhost:3306/test";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    //коннектимся к БД
    public static void connectToDB() {
        try {
            // Открываю соединение до MySQL сервера
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();

        } catch (SQLException sql) {
            System.out.println("Ошибка с подключением!");
        }
    }
        //Вставляем N элементов в SQL таблицу
    public static void paste(int n){
        try {
            ResultSet rs = stmt.executeQuery("SELECT*FROM test_table");
            if (rs.next()) {
                stmt.executeUpdate("DELETE FROM test_table WHERE FIELD >0");
            }

            for(int i = 0;i<n;i++){
                PreparedStatement pState = con.prepareStatement("INSERT INTO test_table (FIELD) VALUES (?)");
                pState.setInt(1, (i+1));
                pState.executeUpdate();
            }
            System.out.println("Я заполнил " + n + " строк");

        }catch(SQLException e){
            System.out.println("Ошибка с заполнением таблицы!");
        }
    }


    //Создаю файл и вставляю в него значения из таблицы
    public static void createXML(List<Integer> list){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try{
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("entries");
            document.appendChild(root);

            for(Integer num : list){
                Element subRoot = document.createElement("entry");
                root.appendChild(subRoot);

                Element entry = document.createElement("field");
                entry.setTextContent(String.valueOf(num));
                subRoot.appendChild(entry);
            }

            StreamResult file = new StreamResult(new File("1.xml"));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(document);
            transformer.transform(source, file);
        }catch(ParserConfigurationException | TransformerException e){
            e.printStackTrace();
        }
    }
    //Заполняем коллекцию значениями из таблицы
    public static void addValueToList(ArrayList<Integer> list){

        try {
            rs = stmt.executeQuery("SELECT*FROM test_table");
            while(rs.next()){
                list.add(rs.getInt(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
