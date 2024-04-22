import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    private BufferedImage back;
    private int count, lives;
    private ImageIcon background;
    private PlayerShip player;
    private ArrayList<AlienShip> aliens;
    private ArrayList<ShipMissile> sMissiles;
    private ArrayList<AlienMissile> aMissiles;
    private boolean start, win, moveRight;
    private char screen;
    private boolean playSad;
    private boolean playWin;
    private Sound music;

    public Game() {
        new Thread(this).start();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        start = true;
        sMissiles = new ArrayList<>();
        aMissiles = new ArrayList<>();
        win = false;
        moveRight = true;
        count = 0;
        screen = 'S';
        aliens = setAliens();
        background = new ImageIcon("a4a10f79883bb2ea50222e75adcef03d.jpg");
        player = new PlayerShip(700, 600, 75, 100);
        lives = 3;
        music = new Sound(); // Assuming Sound class constructor initializes properly
        playWin = false;
        playSad = false;
    }

    public void gameReset() {
        aliens = setAliens();
        count = 0;
        lives = 3;
        screen = 'S';
    }

    public ArrayList<AlienShip> setAliens() {
        ArrayList<AlienShip> temp = new ArrayList<>();
        int x = 75;
        int y = 100;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                temp.add(new AlienShip(x, y, 50, 50));
                x += 100;
            }
            y += 50;
            x = 75;
        }
        return temp;
    }

    public void run() {
        try {
            while (true) {
                Thread.currentThread().sleep(5);
                repaint();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        Graphics2D twoDgraph = (Graphics2D) g;
        if (back == null) {
            back = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        Graphics g2d = back.createGraphics();

        g2d.clearRect(0, 0, getSize().width, getSize().height);

        screen(g2d);

        twoDgraph.drawImage(back, 0, 0, null);
    }

    public void screen(Graphics g2d) {
        switch (screen) {
            case 'S':
                //start screen
                g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
                drawStartScreen(g2d);
                break;

            case 'G':
                //game screen
                g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
                drawAliens(g2d);
                moveAliens();
                drawPlayerShip(g2d);
                drawSM(g2d);
                drawAlienMissile(g2d);
                alienHit();
                playerHit();
                checkLose1();
                checkWin();
                count++;
                if (count % 100 == 0) {
                    getAlienMissile(g2d);
                }
                g2d.setColor(Color.black);
                g2d.drawString("Lives: " + lives, 100, 100);
                break;

            case 'W':
                g2d.setColor(Color.green);
                g2d.setFont(new Font("chiller", Font.BOLD, 55));
                g2d.drawString("You Win!!", 300, 300);

                if (playWin) {
                    music.playmusic("Ta Da-SoundBible.com-1884170640.wav");
                    playWin = false;
                }
                break;

            case 'L':
            	
            	
                g2d.setColor(Color.red);
                g2d.setFont(new Font("chiller", Font.BOLD, 55));
                g2d.drawString("Game Over!!", 300, 300);
               
                if (playSad) {
                    music.playmusic("Sad_Trombone-Joe_Lamb-665429450.wav");
                    playSad = false;
                }
                break;
        }
    }
    public void checkLose1() {
        if (checkAliens() || lives == 0) {
            screen = 'L';
            playSad = true; // Set the flag to play lose sound
        }
    }

    public void drawStartScreen(Graphics g2d) {
        g2d.setFont(new Font("Broadway", Font.BOLD, 50));
        g2d.setColor(Color.white);
        g2d.drawString("Welcome to Space Invaders!", 200, 400);
        g2d.drawString("Press B to Begin", 200, 600);
        g2d.drawString("Use mouse to move and left click to shoot", 200, 800);
    }

    public void drawAliens(Graphics g2d) {
        for (AlienShip a : aliens) {
            g2d.drawImage(a.getPic().getImage(), a.getX(), a.getY(), a.getW(), a.getH(), this);
        }
    }

    public void drawPlayerShip(Graphics g2d) {
        g2d.drawImage(player.getPic().getImage(), player.getX(), player.getY(), player.getW(), player.getH(), this);
    }

    public void checkWin() {
        if (aliens.isEmpty()) {
            screen = 'W';
            playWin = true; // Set the flag to play win sound
        }
    }

    public void checkLose() {
        if (checkAliens() || lives == 0) {
            screen = 'L';
        }
    }

    public void moveAliens() {
        if (checkAlienWall()) {
            changeDyAlien(40);
        }

        for (AlienShip a : aliens) {
            if (moveRight) {
                a.setdx(2);
                changeDyAlien(0);
            } else {
                a.setdx(-2);
                changeDyAlien(0);
            }
        }
    }

    public void changeDyAlien(int dy) {
        for (AlienShip a : aliens) {
            a.setdy(dy);
        }
    }

    public boolean checkAlienWall() {
        for (AlienShip a : aliens) {
            if (a.getX() + a.getW() >= getWidth() || a.getX() < 0) {
                moveRight = !moveRight;
                return true;
            }
        }
        return false;
    }

    
    public void playerMissile(int x) {
        sMissiles.add(new ShipMissile(x, player.getY(), 50, 50));
        // Play shoot sound
        music.playmusic("blaster-2-81267.wav");
    }


    public void drawSM(Graphics g2d) {
        for (ShipMissile sm : sMissiles) {
            g2d.drawImage(sm.getPic().getImage(), sm.getX(), sm.getY(), sm.getH(), sm.getW(), this);
            sm.setdy(-3);
        }
    }

    public void getAlienMissile(Graphics g2d) {
        int randAlien = (int) (Math.random() * aliens.size());
        aMissiles.add(new AlienMissile(aliens.get(randAlien).getX() + (aliens.get(randAlien).getW()) / 2, aliens.get(randAlien).getY() + aliens.get(randAlien).getH(), 30, 30));
    }

    public void drawAlienMissile(Graphics g2d) {
        for (AlienMissile am : aMissiles) {
            g2d.drawImage(am.getPic().getImage(), am.getX(), am.getY(), am.getW(), am.getH(), this);
            am.setdy(3);
        }
    }

    public void playerHit() {
        for (int i = 0; i < aMissiles.size(); i++) {
            if (player.hit(aMissiles.get(i))) {
                aMissiles.remove(i);
                lives--;
                i--;
            }
        }
    }

    public boolean alienHit() {
        if (!aliens.isEmpty()) {
            for (int i = 0; i < aliens.size(); i++) {
                for (int j = 0; j < sMissiles.size(); j++) {
                    if (aliens.get(i).hit(sMissiles.get(j))) {
                        aliens.remove(i);
                        sMissiles.remove(j);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkAliens() {
        for (AlienShip a : aliens) {
            if (a.getY() + a.getH() >= player.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        //TODO Auto-Generated method stub
    }

    @Override
    public void mouseMoved(MouseEvent m) {
        //TODO Auto-Generated method stub
        player.setX(m.getX());
        //System.out.println(m.getX());
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        //TODO Auto-Generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        //TODO Auto-Generated method stub
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        //TODO Auto-Generated method stub
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        //TODO Auto-Generated method stub
        if (arg0.getButton() == 1) {
            playerMissile(arg0.getX() + (player.getW() / 4));
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        //TODO Auto-Generated method stub
    }

    //Do not delete
    @Override
    public void keyTyped(KeyEvent e) {
        //TODO Auto-Generated method stub
    }

    //Do not delete
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println(key);
        if (key == 66) {
            screen = 'G'; // Start the game
        }

        if (key == 82) {
            gameReset(); // Reset the game
            start = false;
        }

        // Cheat code for adding lives (Key: P)
        if (key == 80) {
            lives++; // Increase the player's lives
            System.out.println("Cheat activated: Lives added!");
        }

        // Cheat code for bringing up the win screen (Key: Q)
        if (key == 81) {
            screen = 'W'; // Set the screen to win
            playWin = true; // Set the flag to play win sound
            System.out.println("Cheat activated: Win screen!");
        }
    }
      

    //Do not delete
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
    }
}

