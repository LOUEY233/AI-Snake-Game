package Java2232.final_project;

import java.awt.EventQueue;
import javax.swing.JFrame;
// original game for player with keyboard
public class Snake extends JFrame {

    public Snake() throws Exception {
        
        initUI();
    }
    
    private void initUI() throws Exception {
        
        add(new Board());
               
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = null;
            try {
                ex = new Snake();
                ex.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
