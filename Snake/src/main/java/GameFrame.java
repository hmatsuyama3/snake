import javax.swing.*;

public class GameFrame extends JFrame {

    //Constructor
    public GameFrame(){
        GamePanel panel = new GamePanel();
        this.add(panel);
        //The above can be shortened to: this.add(new GamePanel()); if instance of GamePanel is never called on
        this.setTitle("Snake");
        this.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        this.setResizable(false);
        this.pack(); //This method sizes the JFrame nicely so everything fits inside(I think)
        this.setVisible(true);
        this.setLocationRelativeTo(null); //This opens up the frame at the middle of the screen
    }
}
