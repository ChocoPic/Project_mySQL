import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame4 extends JFrame implements ActionListener {
    private final static int STARBUCKS = 1;
    private final static int GONGCHA = 201;
    private final static int HOLLYS = 301;
    private final static int PAIKS = 401;

    private JLabel imageLabel;
    private JLabel textLabel;
    private ImageIcon img = null;
    private JButton button;
    private static int id;
    private MySQL_Handler handler = new MySQL_Handler();

    public Frame4() {
        setTitle("Frame4_전체 메뉴 보여주기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        button = new JButton("▶");
        button.addActionListener(this);
        imageLabel = new JLabel();
        textLabel = new JLabel();
        c.add(textLabel, BorderLayout.NORTH);
        c.add(imageLabel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setSize(50, 50);
        panel.add(button);
        c.add(panel,BorderLayout.SOUTH);

        handler.conectDB(); //DB연결
        showMenu(id); //메뉴보여주기

        setSize(400, 600);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {    //버튼을 클릭하면 메뉴를 보여준다
        showMenu(id);
        id++; System.out.println(id);
    }

    public void showMenu(int i){
        imageLabel.setIcon(handler.getMenuImage(i));
        textLabel.setText(handler.getMenuString(i,"NAME")+ " "
                + handler.getMenuString(i,"PRICE")+ "원 "
                + "커피 "+ handler.getMenuString(i,"COFFEE"));
    }

    public static void pickBrand(int brandNum) {
        switch (brandNum){
            case 1:
                id = STARBUCKS; break;
            case 2:
                id = GONGCHA; break;
            case 3:
                id = HOLLYS; break;
            case 4:
                id = PAIKS; break;
        }
        new Frame4();
    }
}
