import java.io.*;
import java.sql.*;

/*
프로젝트 내의 images 폴더 안에 있는 메뉴 이미지를
db에 cafemenu 테이블- image 에 mediumblob 으로 넣어주는 코드

ex)./images/스타벅스/카페 아메리카노.jpg
파일 못찾는 오류는 대부분 이미지파일 이름을 수정하면 됨.

예외: 빽다방 민트초코라떼 핫이랑 아이스랑 분리?? or 이미지합치기?
빽다방 딸기라떼랑 미숫가루 이미지는 못찾음*/

public class SaveImages {

    public static void main(String[] args) {
        Connection con = null;
        Statement st = null;
        String url = "jdbc:mysql://localhost:3306/dbdb?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true&useSSL=false";
        String id = "root";
        String password = "0709";
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

        String[] img_brand = {"스타벅스", "공차", "할리스커피", "빽다방"};

        for (int i = 0; i < 4; i++) {
            String sql1 = "select * from cafemenu where brand='" + img_brand[i] + "';";
            String sql2 = "update cafemenu set image=? where name=? and brand=?;";
            String img_path = null;
            String img_name = null;
            String img_path2 = null;
            ResultSet rs = st.executeQuery(sql1);
            while (rs.next()) {
                img_name = rs.getString("name").trim();
                img_path = "./images/" + img_brand[i] + "/" + img_name + ".jpg";
                img_path2 = "./images/" + img_brand[i] + "/" + img_name + ".png";
                try {
                    PreparedStatement pstmt = con.prepareStatement(sql2);
                    pstmt.setString(3, img_brand[i]);
                    pstmt.setString(2, img_name);
                    InputStream inputStream = new FileInputStream(new File(img_path));
                    pstmt.setBlob(1, inputStream);
                    pstmt.executeUpdate();
                } catch (FileNotFoundException e) {
                    try {
                        PreparedStatement pstmt = con.prepareStatement(sql2);
                        pstmt.setString(3, img_brand[i]);
                        pstmt.setString(2, img_name);
                        InputStream inputStream = new FileInputStream(new File(img_path2));
                        pstmt.setBlob(1, inputStream);
                        pstmt.executeUpdate();
                    } catch (FileNotFoundException | SQLException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        con.close();
    }
}

