import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    //Set GamePanel screen size
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    //How big is each "square" in the game?
    static final int UNIT_SIZE = 24;
    //How many "units" can the screen fit?
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    //Delay for "timer" - how fast should the game run? (higher number = slower game)
    static final int DELAY = 75;
    //Two arrays to hold the x & y coordinates of the snake, including head & body.
    //They array size is the same as the number of "units" on the screen, as the snake can't get any bigger than that
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    //How many "units" long is the snake?
    int bodyParts = 6;
    //Number of apples Eaten by snake
    int applesEaten;
    //X & Y coordinates of apples, which will appear randomly after being eaten
    int appleX;
    int appleY;
    //Variable to indicate direction the snake is traveling. It can start off going R for right.
    char direction = 'R';
    //Is the game running?
    boolean running = false;
    //Challenge: Add new game button on game over screen
    JButton newGameButton = new JButton("New Game");
    int button_width = 120;
    int button_height = 50;
    //Other required objects for the game:
    Timer timer;
    Random random; //An instance of this class is used to generate a stream of pseudorandom numbers

    //Constructor
    public GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true); //Allows player to "select" of "focus" on the particular GUI element
        this.addKeyListener(new MyKeyAdapter()); //Allows program to take in keystrokes

        //Call startGame method after everything has been constructed
        startGame();

    }

    public void startGame(){
        newApple(); //First, create a new apple on the screen
        running = true; //Game is not running until you start the game. Now you've started the game so running = true
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running) { //If the game is running, draw all of this on the gamePanel
            /* Comment this back in if you want to see the gridlines
            //Create gridlines on the GamePanel to better visualize everything
            //g.drawLine parameters are - from what (x,y) to (x,y) are you drawing the line?
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
             */

            //Draw the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //Fills an oval bounded by the specified rectangle with the current color

            //Draw head * body of the snake
            //For loop to  iterate through all of the body parts of the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {  //The first unit of the snake (index 0) is the head
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //Fills the using location of the arrays for the snake head & body
                } else { //This is for the rest of the body
                    g.setColor(new Color(45, 180, 0)); //A slightly different shade of green than the head.
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //Draw the score at the top of the Game Panel -- See Game Over method for more comments
            g.setColor(Color.red);
            g.setFont(new Font("Snell Roundhand", Font.PLAIN, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());

        }
        else{ //If the game isn't running, draw the gameOver screen
            gameOver(g);
        }
    }

    //This method generates a new apple whenever it is called: at the beginning of the game, and also when it is eaten
    public void newApple(){
        //Generate random integer for x coordinate of the apple.
        //The range of the x coordinate is passed through as a paramenter to nextInt()
        //Argument is cast as an "int" just in case. Multiply the randomly generated number by UNIT_size again to get the correct "box"
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;


    }

    //THis method moves the snake
    public void move(){
        //for loop to shift each body part of the snake as it moves to the one in front of it
        for(int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0]-UNIT_SIZE; //takes the y coordinate and moves it up by one 'unit'
                break;
            case 'D':
                y[0] = y[0]+UNIT_SIZE; //takes the y coordinate and moves it down by one 'unit'
                break;
            case 'L':
                x[0] = x[0]-UNIT_SIZE; //takes the x coordinate and moves it left by one 'unit'
                break;
            case 'R':
                x[0] = x[0]+UNIT_SIZE; //takes the x coordinate and moves it right by one 'unit'
                break;
        }
    }

    public void checkApple(){
        //Examine coordinates of snake & apple to see if they match.
        if((x[0] == appleX) && (y[0] == appleY)){ //If the head of the snake & apple coordinates match
            bodyParts++; //Increase number of snake body parts (it gets longer by 1)
            applesEaten++; //Increase score
            newApple(); //Generate a new apple
        }

    }

    public void checkCollisions(){
        //Check if snake tried to eat itself
        for(int i = bodyParts; i>0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){ //If this is true, this means the head has collided with the body
                running = false; //triggers "game over"
            }
        }
        //Check if head touches the left border
        if(x[0] < 0){
            running = false;
        }
        //Check if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        //Check if head touches top border
        if(y[0] <0){
            running = false;
        }
        //Check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop(); //stop timer once game over
        }

    }


    public void gameOver(Graphics g){
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Snell Roundhand", Font.PLAIN, 75)); //New instance of font object with attributes
        FontMetrics metrics1 = getFontMetrics(g.getFont()); //Use this to help line up text in the center of the screen
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); //Tells what and where to draw "GAME OVER"

        //Score - repeated what is in the draw method so that score continues to be displayed in the game over screen
        g.setColor(Color.red);
        g.setFont(new Font("Snell Roundhand", Font.PLAIN, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());

        //Start new game button
        newGameButton.setBounds(SCREEN_WIDTH/2-button_width/2, SCREEN_HEIGHT/2 + 100, button_width, button_height);
        this.add(newGameButton);
        newGameButton.addActionListener(this);

    }




    @Override //Required implementation
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        else if(e.getSource() == this.newGameButton){
            //Reset snake
                x[0] = 0;
                y[0] = 0;
            bodyParts = 6;
            direction = 'R';
            applesEaten = 0;
            this.remove(newGameButton);
            startGame();

        }
        repaint(); //I'm still not really sure why I need this but it doesn't work without it

    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT: //Keypad Left was pressed
                    //Need to prevent player from changing the snake into itself. Check to make sure which direction snake is traveling before allowing change
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }
    }
}
