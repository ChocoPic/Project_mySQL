import java.io.*;
import java.sql.*;

/*
프로젝트 내의 images 폴더 안에 있는 메뉴 이미지를
db에 스타벅스 테이블- image 에 blob 으로 넣어주는 코드

ex)./images/카페 아메리카노.jpg
* 차이티는 용량이 커서 안들어감.
이유는 모르겠는데 수동으로 넣으면 됨.
*/

public class SaveImages {

    public static void main(String[] args) {
        Connection con = null;
        Statement st = null;
        String url = "jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true&useSSL=false";
        String id = "menu_master";
        String password = "menu804";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,id, password);
            System.out.println("DB 연결 완료");
            st = con.createStatement();
            saveImage(con, st);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 에러");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL 실행 에러");
            e.printStackTrace();
        }
    }
    private static void saveImage(Connection con, Statement st) throws SQLException {

        String sql1 = "select * from 스타벅스;";
        String sql2 = "update 스타벅스 set image=? where name=?;";
        String img_path=null;
        String img_name=null;

        ResultSet rs = st.executeQuery(sql1);
        while (rs.next()) {
            img_name = rs.getString("name").trim();
            img_path = "./images/" + img_name+".jpg";
            try{
                PreparedStatement pstmt = con.prepareStatement(sql2);
                pstmt.setString(2," "+img_name);
                InputStream inputStream = new FileInputStream(new File(img_path));
                pstmt.setBlob(1, inputStream);
                pstmt.executeUpdate();
            } catch (FileNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        con.close();
    }
}

