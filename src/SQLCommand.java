
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLCommand {

   private static ArrayList<Integer> list = new ArrayList<>();

    public static void init(int n){
        connectToDB();
        paste(n);
        addValueToList(list);
        createXML(list);
        xsl("1.xml","2.xml","red.xsl");
        parse();
        closeAll();
    }

    public SQLCommand(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public SQLCommand(){

    }

    public static void setUrl(String url) {
        SQLCommand.url = url;
    }

    public static void setUser(String user) {
        SQLCommand.user = user;
    }

    public static void setPassword(String password) {
        SQLCommand.password = password;
    }

    private static  String url;
    private static  String user;
    private static  String password;

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
            ResultSet rs = stmt.executeQuery("SELECT FIELD FROM test_table");
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


    //Создаю файл и вставляю в него значения из колелекции
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
            rs = stmt.executeQuery("SELECT FIELD FROM test_table");
            while(rs.next()){
                list.add(rs.getInt(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //Создаем новый XML по образу XSL файла
    public static void xsl(String inFilename, String outFilename, String xslFilename) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();

            Templates template = factory.newTemplates(new StreamSource(new FileInputStream(xslFilename)));

            Transformer transformer = template.newTransformer();


            Source source = new StreamSource(new FileInputStream(inFilename));
            Result result = new StreamResult(new FileOutputStream(outFilename));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Парсим XML файл
    public static void parse(){
        try{
            long sum = 0;
            DocumentBuilder docBuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuild.parse("2.xml");

            NodeList entry = doc.getElementsByTagName("entry");

            for(int i = 0; i< entry.getLength();i++){
                Node entryAtt = entry.item(i);
                String key = entryAtt.getAttributes().getNamedItem("field").getNodeValue();
                int num = Integer.parseInt(key);
                sum +=num;
            }
            System.out.println("Сумма значений всех аттрибутов = " + sum);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void closeAll(){
        try {
            con.close();
            stmt.close();
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
