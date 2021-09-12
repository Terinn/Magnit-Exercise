public class Main {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        SQLCommand sql = new SQLCommand();
        sql.setUrl("jdbc:mysql://localhost:3306/test");
        sql.setUser("root");
        sql.setPassword("root");
        //Метод init запускает все методы внутри класса SQLCommand
        // и принимает значение N --> колличество элементов которые необходимо вставить;
        sql.init(20);





    }
}
