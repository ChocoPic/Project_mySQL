import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TestDB extends JFrame implements ActionListener {
    private JLabel imageLabel;
    private JLabel textLabel;
    private ImageIcon img = null;
    private JButton button;
    private ResultSet rs = null;
    private Statement st = null;

    public TestDB() {
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
        setTitle("TestFrame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        button = new JButton("시작");
        button.addActionListener(this);
        imageLabel = new JLabel();
        textLabel = new JLabel();
        c.add(textLabel, BorderLayout.NORTH);
        c.add(imageLabel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setSize(50, 50);
        panel.add(button);
        c.add(panel,BorderLayout.SOUTH);

        setSize(400, 600);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showMenu();
    }

    private void showMenu() {
        try {
            rs = st.executeQuery("select * from 스타벅스");
            if (rs.next()) {
                Blob b = rs.getBlob("image");   //DB에서 바이너리 데이터 얻어옴
                img = new ImageIcon(b.getBytes(1, (int) b.length()));//바이너리 데이터를 이미지 포맷으로 변환
                imageLabel.setIcon(img);
                textLabel.setText(rs.getString("name") + " " + rs.getString("price") + " " + rs.getString("coffee"));
            }
        }catch (SQLException e){

        }
    }

    public static void main(String[] args) {
        TestDB frame = new TestDB();
    }
}