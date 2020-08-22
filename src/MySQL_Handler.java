import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/*
--------------------
MySQL_Handler 클래스
--------------------
void connectDB() : db랑 연결합니다.
ArrayList<Integer> coffee_O() : 커피 id 리스트를 만듭니다.
ArrayList<Integer> coffee_X() : 논커피 id 리스트를 만듭니다.
String getMenuString(int i, String COLUMN) : String 값을 가져옵니다.(i: id값 / COLUMN 값: BRAND,NAME,PRICE,COFFEE)
ImageIcon getMenuImage(int i) : ImageIcon(메뉴이미지)을 가져옵니다. (i: id값)
 */

public class MySQL_Handler {
    private ResultSet rs = null;
    private Statement st = null;
    private ImageIcon img = null;

    private int id = 1; //출력할 메뉴의 id값
    private static ArrayList<Integer> idList_coffee = new ArrayList<>();    //커피인 메뉴의 id가 저장될 arraylist
    private static ArrayList<Integer> idList_noncoffee = new ArrayList<>(); //논커피인 메뉴의 id가 저장될 arraylist

    private String sql_O = "select * from cafemenu where coffee='O';";
    private String sql_X = "select * from cafemenu where coffee='X';";

    private void make_coffeeList(String sql_coffee){    //커피 또는 논커피의 id가 저장된 Arraylist 를 생성
        try{
            rs = st.executeQuery(sql_coffee);
            while(rs.next()){
                idList_coffee.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void conectDB() {   //db에 연결하기
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true&useSSL=false";
        String id = "menu_master";
        String password = "menu804";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, id, password);
            System.out.println("DB 연결 완료");
            st = con.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 에러");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL 실행 에러");
            e.printStackTrace();
        }
    }

   //커피 id가 담긴 arraylist를 반환
    public ArrayList<Integer> coffee_O(){
        make_coffeeList(sql_O);
        return idList_coffee;
    }

    //논커피 id가 담긴 arraylist를 반환
    public ArrayList<Integer> coffee_X(){
        make_coffeeList(sql_X);
        return idList_noncoffee;
    }


    //id에 해당하는 필요한 값을 반환
    public String getMenuString(int i, String COLUMN) {
        String sql =  "select * from cafemenu where id='"+i+"';";
        String value = null;
        try {
            rs = st.executeQuery(sql);
            if (rs.next()) {
                switch (COLUMN) {
                    case "BRAND":
                        value = rs.getString("brand");
                        break;
                    case "NAME":
                        value = rs.getString("name");
                        break;
                    case "PRICE":
                        value = rs.getString("price");
                        break;
                    case "COFFEE":
                        value = rs.getString("coffee");
                        break;
                    default:
                        value = null;
                }
            }
        }catch (SQLException e){

        }
        return value;
    }

    public ImageIcon getMenuImage(int i) {
        String sql =  "select * from cafemenu where id='"+i+"';";
        try {
            rs = st.executeQuery(sql);
            if (rs.next()) {
                Blob b = rs.getBlob("image");   //DB에서 바이너리 데이터 얻어옴
                img = new ImageIcon(b.getBytes(1, (int) b.length()));//바이너리 데이터를 이미지 포맷으로 변환
                }
        }catch (SQLException e){

        }
        return img;
    }
}
