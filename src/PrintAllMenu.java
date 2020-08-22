import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class PrintAllMenu extends JFrame implements ActionListener {
    private JLabel imageLabel;
    private JLabel textLabel;
    private ImageIcon img = null;
    private JButton button;
    private ResultSet rs = null;
    private Statement st = null;

    private int id = 1; //출력할 메뉴의 id값
    private static ArrayList<Integer> idList_coffee = new ArrayList<>();    //커피인 메뉴의 id가 저장될 arraylist
    private static ArrayList<Integer> idList_noncoffee = new ArrayList<>(); //논커피인 메뉴의 id가 저장될 arraylist

    private String sql_O = "select * from cafemenu where coffee='O';";
    private String sql_X = "select * from cafemenu where coffee='X';";

    public PrintAllMenu() {
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

        /*커피나 논커피인 경우에 arraylist를 만든다.*/
        make_coffeeList(sql_O); //커피인경우
        //make_coffeeList(sql_X); //논커피인경우
    }

    @Override
    public void actionPerformed(ActionEvent e) {    //버튼을 클릭하면 메뉴를 보여준다
        /*1. 전체 메뉴 중에서 보여주는 코드*/
        showMenu(id);
        /*2. 커피 or 3. 논커피 중 에서 보여주는 코드*/
        /*int menuID = idList_coffee.get(id);
        showMenu(menuID);*/
    }

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

    private void showMenu(int id) { //id에 해당하는 메뉴를 gui에 띄움
        String sql = "select * from cafemenu where id='"+id+"';";
        try {
            rs = st.executeQuery(sql);
            if (rs.next()) {
                Blob b = rs.getBlob("image");   //DB에서 바이너리 데이터 얻어옴
                img = new ImageIcon(b.getBytes(1, (int) b.length()));//바이너리 데이터를 이미지 포맷으로 변환
                imageLabel.setIcon(img);    //이미지출력
                textLabel.setText( rs.getString("brand") + " "
                        + rs.getString("name") + " "
                        + rs.getString("price") + " "
                        + rs.getString("coffee"));   //텍스트출력
            }
        }catch (SQLException e){

        }
    }

    public static void main(String[] args) {
        PrintAllMenu frame = new PrintAllMenu();
    }
}