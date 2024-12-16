package Man;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class AirHockey extends AnimListener implements MouseMotionListener, MouseListener, KeyListener {
    int tem = 0;
    int tem2;
    double pox = 0;
    double poy;
    double poxb = 0;
    double poyb = 0;
    int maxWidth = 100;
    int maxHeight = 100;
    int s1 = 4;
    int s2 = 4;
    int s3 = 4;
    int s4 = 4;
    double speedx = .3;  //speed of the ball
    double speedy = .2;
    boolean ballStationary = true;
    int ailevel = 3;  // 1=> easy ,2=> med ,3=> hard
    boolean gameended = false;
    double xball = 45;
    double yball = 45;
    double xred = 10;
    double yred = 45;
    double xblue = 80;
    double yblue = 45;
    boolean gamerun1p = true; /* For two players make it false */
    boolean start = true;
Timer T=new Timer();
    int scoreRed = 0;
    int scoreBlue = 0;
    int highScore = 0;
    private int lastHighScore = -1;
    static int page;
    String player1Name = "", player2Name = "";
    int player1Score = scoreRed, player2Score = scoreBlue;
    Scanner input = new Scanner(System.in);

    SoundPlayer goalRed = new SoundPlayer("C:\\Users\\alikh\\g\\mainProject_304\\sound/goolRed.wav");
    SoundPlayer goalBlue = new SoundPlayer("C:\\Users\\alikh\\g\\mainProject_304\\sound/goolBlue.wav");
    SoundPlayer CollisionSound = new SoundPlayer("C:\\Users\\alikh\\g\\mainProject_304\\sound/puckHitWall.wav");
    SoundPlayer playerCollision = new SoundPlayer("C:\\Users\\alikh\\g\\mainProject_304\\sound/puckHitPaddle.wav");
    SoundPlayer backgroundSound = new SoundPlayer("C:\\Users\\alikh\\g\\mainProject_304\\sound/puckHitPaddle.wav");
    SoundPlayer win = new SoundPlayer("C:\\Users\\alikh\\g\\mainProject_304\\sound/edrab.wav");



    //  TextRenderer renderer = new TextRenderer(new Font("sanaSerif", Font.BOLD, 10));
    String[] textureNames = {
            "bluehockeystick.png", //0
            "field.png", //1
            "puck.png", //2
            "redhockeystick.png" //3
            , "0.png", "1 .png", "2 .png", "3.png", "4.png", "5.png", "6.png", "7.png", "8.png", "9.png",
            "BlueWins.png", "RedWins.png", "colon.png", "colon1.png", "colon2.png",
            "LEVELS.png", "game-rules.png", "home1.png"
    };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture[i].getPixels());
            } catch (IOException e) {
                System.out.println(e);

                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        backgroundSound.isPlaying();
        switch (page) {
            case 0: // Home

                draw(gl, textureNames.length - 1);

                break;
            case 1: // Rules -> Help

                draw(gl, textureNames.length - 2);

                break;
            case 2: // Levels
                draw(gl, textureNames.length - 3);
                break;
            case 3: // Game
                if (start) {
                    initializeGame();
                    System.out.println("Page: " + page + ", Player1 Score: " + player1Score + ", Player2 Score: " + player2Score + "\n");
                    System.out.println("Saving scores: " + player1Name + " - " + player1Score + ", " + player2Name + " - " + player2Score + "\n");
//                    System.out.println("Game initialized with players: " + player1Name + ", " + player2Name);
                    start = false;
                }


//                if (player1Name.isEmpty()) player1Name = "Player1";
                if (!gamerun1p && player2Name.isEmpty()) player2Name = "Player2: AI";

                if (gamerun1p) {
                    run1p(gl);
                } else {
                    run2p(gl);
                }
//                // Check if the game is over
                if (scoreBlue == 2 || scoreRed == 2) {
                    endGame(gl);
                    goalBlue.stop();
                    goalRed.stop();
                    win.play();


                }

                break;
            case 4:
                if (scoreRed > scoreBlue) {
                    DrawWin(gl, 15);
                    System.out.println("RedWins");
                } else if (scoreBlue > scoreRed) {
                    DrawWin(gl, 14);
                    System.out.println("BlueWins");
                } else {
                    System.out.println(scoreBlue + scoreBlue);
                }


                break;
        }

//        getScoresAndNamesFromFile();
//        if (gamerun1p) {
//            run1p(gl);
//        } else
//            run2p(gl);
//        /* For two players method mouse and Keyboard */
    }

    void run2p(GL gl) {
        DrawBackground(gl);
        DrawBackground(gl);
        DrawSprite(gl, xblue, yblue, 0, 1);
        DrawSprite(gl, xred, yred, 3, 1);
        DrawSprite(gl, xball, yball, 2, 1);
        if (xball == maxWidth && yball == maxHeight) {
            xball = 45;
            yball = 45;
        }
        T.time--;
        T.calctime();
        calcscore();
        if (!ballStationary) {
            xball += speedx;
            yball += speedy;
        }

        //الحته دي يسطا بتاع التصادم

        if (!(s2 == 4 && s4 == 4)) {
            xball += 0.5 * speedx;
            yball += 0.5 * speedy;
        }
        checkCollision();

        Drawnum(gl, 53, 91, T.t3, 0.7F);
        Drawnum(gl, 60, 91, T.t4, 0.7F);
        Drawnum(gl, 46, 91, 16, 0.6F);
        Drawnum(gl, 39, 91, T.t2, 0.7F);
        Drawnum(gl, 32, 91, T.t1, 0.7F);
        Drawnum(gl, 20, -1, s1, 0.7F);
        Drawnum(gl, 27, -1, s2, 0.7F);
        Drawnum(gl, 70, -1, s3, 0.7F);
        Drawnum(gl, 77, -1, s4, 0.7F);
    }

// Ai difficulty of the game

    // A general AI method to handle common logic
    void AILogic(double speedX, double speedY, double minX, double maxX, double minY, double maxY) {
        // Adjust Y position
        if (yball > yblue) {
            yblue += speedY;
        } else if (yball < yblue) {
            yblue -= speedY;
        }

        // Adjust X position
        if (xball > xblue) {
            xblue += speedX;
        } else if (xball < xblue) {
            xblue -= speedX;
        }

        // Clamp X and Y to allowed ranges
        xblue = Math.max(minX, Math.min(xblue, maxX));
        yblue = Math.max(minY, Math.min(yblue, maxY));

        // Add random adjustments to avoid glitches
        if (Math.abs(yblue - yball) < 10 && Math.abs(xblue - xball) < 10) {
            yblue += (Math.random() * 4) - 2;
            xblue += (Math.random() * 4) - 2;
        }
    }

    // Easy AI
    void aiEasy() {
        AILogic(
                1,
                0.05,
                60, 80,
                10, 80
        );
    }

    // Medium AI
    void aiMid() {
        if (xball > 45) {
            AILogic(
                    0.01,
                    1,
                    70, 82,
                    10, 80
            );
        }
    }

    // Hard AI
    void aiHard() {
        AILogic(
                2 + Math.random() * 0.2,
                1.3 + Math.random() * 0.2,
                60, 82,
                10, 90
        );
    }

    // for 1 player and AI methods
    void run1p(GL gl) {
        DrawBackground(gl);
        DrawSprite(gl, xblue, yblue, 0, 1);
        DrawSprite(gl, xred, yred, 3, 1);
        DrawSprite(gl, xball, yball, 2, 1);
        if (xball == maxWidth && yball == maxHeight) {
            xball = 45;
            yball = 45;
        }
        T.time--;
        T.calctime();
        calcscore();
        if (!ballStationary) {
            xball += speedx;
            yball += speedy;
        }

        //الحته دي يسطا بتاع التصادم

        if (!(s2 == 4 && s4 == 4)) {
            xball += 0.5 * speedx;
            yball += 0.5 * speedy;
        }
        checkCollision();

        Drawnum(gl, 53, 91, T.t3, 0.7F);
        Drawnum(gl, 60, 91, T.t4, 0.7F);
        Drawnum(gl, 46, 91, 16, 0.6F);
        Drawnum(gl, 39, 91, T.t2, 0.7F);
        Drawnum(gl, 32, 91, T.t1, 0.7F);
        Drawnum(gl, 20, -1, s1, 0.7F);
        Drawnum(gl, 27, -1, s2, 0.7F);
        Drawnum(gl, 70, -1, s3, 0.7F);
        Drawnum(gl, 77, -1, s4, 0.7F);

        if (ailevel == 2) {
//            System.out.println("Mid");
            aiMid();
        } else if (ailevel == 3) {
            aiHard();
//            System.out.println("hard");
        } else {
            aiEasy();
//            System.out.println("Easy");
        }
    }

    public void draw(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void calcscore() {
        highScore = getHighScore();

        if (xball <= 2 && yball >= 30 && yball <= 60) { //blueOne
            scoreBlue++;
            goalRed.stop();
            goalBlue.play();
            xball = maxWidth;
            yball = maxHeight;
            s4++;
            if (s4 == 14) {
                s4 = 4;
                s3++;
            }
            if (s3 == 14) {
                s3 = 4;
                s4 = 4;
            }

            speedx = 0;
            speedy = 0;
        }
        if (xball >= 85 && yball >= 30 && yball <= 60) { //redOne
            scoreRed++;
            goalBlue.stop();
            goalRed.play();

            xball = maxWidth;
            yball = maxHeight;
            s2++;
            if (s2 == 14) {
                s2 = 4;
                s1++;
            }
            if (s1 == 14) {
                s1 = 4;
                s2 = 4;
            }
            speedx = 0;
            speedy = 0;
        }
//        if (scoreBlue < scoreRed) {
//            if (highScore < scoreRed) {
//                highScore = scoreRed;
//            }
//        } else {
//            if (highScore < scoreBlue) {
//                highScore = scoreBlue;
//            }
//        }
        if (Math.max(scoreBlue, scoreRed) > highScore) {
            highScore = Math.max(scoreBlue, scoreRed);
            saveHighScore(highScore);
        }

        if (highScore != lastHighScore) {
            System.out.println("New High Score: " + highScore);
            lastHighScore = highScore;
        }

        player1Score = scoreRed;
        player2Score = scoreBlue;

//        saveHighScore(highScore);
//        System.out.println("highScore " + highScore);

    }

    public void saveHighScore(int highScore) {
        try (FileWriter writer = new FileWriter("highScore.txt")) {
            // write in file
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.out.println("Error: Unable to save high score.");
            e.printStackTrace();
        }
    }

    //    public int getHighScore() {
//        int highScore = 0;
//        try (Scanner scanner = new Scanner(new File("highScore.txt"))) {
//            if (scanner.hasNextInt()) {
//                highScore = scanner.nextInt();
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("High score file not found. Starting fresh.");
//        }
//        return highScore;
//    }
    public int getHighScore() {
        try (Scanner scanner = new Scanner(new File("highScore.txt"))) {
            return scanner.hasNextInt() ? scanner.nextInt() : 0;
        } catch (IOException e) {
            System.err.println("Error reading high score file: " + e.getMessage());
            return 0;
        }
    }


    public synchronized void saveScoresAndNamesToFile(String player1Name, int player1Score,
                                                      String player2Name, int player2Score) {
        if (player1Name == null || player1Name.isEmpty()) {
            System.out.println("Error: Player1 name is not set!");
            return;
        }

        if (!gamerun1p && (player2Name == null || player2Name.isEmpty())) {
            System.out.println("Error: Player2 name is not set!");
            return;
        }

        try (FileWriter writer = new FileWriter("playersNamesAndTheirScore.txt", true)) {
            writer.write("Player1: " + player1Name + ", Score: " + player1Score + "\n");
            if (!gamerun1p) {
                writer.write("Player2: " + player2Name + ", Score: " + player2Score + "\n");
            }
            writer.write("----------------------\n");
            System.out.println("Players names and scores saved successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the scores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeGame() {
        System.out.println("Enter player name:");
        scoreRed = 0;
        scoreBlue = 0;
        player1Name = (String) JOptionPane.showInputDialog(null, "Enter Name", "Player 10000"); //java methods  Swing library

        if (!gamerun1p) {
            System.out.println("Enter player 2 name:");

            player2Name = (String) JOptionPane.showInputDialog(null, "Enter Name", "Player 10000");
        }
        highScore = getHighScore(); // Load high score from file
    }

    public void endGame(GL gl) {
        T.t2 = 4;
        page = 4;
//        scoreRed = 0;
//        scoreBlue = 0;
        // make all values are default value
        xball = 45;
        yball = 45;
        xred = 10;
        yred = 45;
        xblue = 80;
        yblue = 45;
        speedx = .3;
        speedy = .2;
        ballStationary = true;
        s1 = 4;
        s2 = 4;
        s3 = 4;
        s4 = 4;
        T.t1 = 4;
        T.t2 = 4;
        T.t3 = 4;
        T.t4 = 4;


        player1Score = scoreRed;
        player2Score = scoreBlue;
        saveScoresAndNamesToFile(player1Name, player1Score, player2Name, player2Score);
    }


    public void DrawSprite(GL gl, double x, double y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.08 * scale, 0.1 * scale, 1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void Drawnum(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawWin(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    void checkCollision() {
        // حساب المسافة واتجاه التصادم
        double dxBlue = xball - xblue;
        double dyBlue = yball - yblue;
        double distanceBlue = Math.sqrt(dxBlue * dxBlue + dyBlue * dyBlue);

        double dxRed = xball - xred;
        double dyRed = yball - yred;
        double distanceRed = Math.sqrt(dxRed * dxRed + dyRed * dyRed);

        double collisionRadius = 8;

        // التصادم مع اللاعب الأزرق
        if (distanceBlue < collisionRadius) {
            ballStationary = false;
//            goalBlue.stop();
//            goalRed.stop();
            playerCollision.play();

            speedx = (dxBlue / distanceBlue) * 0.8;
            speedy = (dyBlue / distanceBlue) * 0.8;
        }

        // التصادم مع اللاعب الأحمر
        if (distanceRed < collisionRadius) {
            ballStationary = false;
            speedx = (dxRed / distanceRed) * 1;
            speedy = (dyRed / distanceRed) * 1;
//            goalBlue.stop();
//            goalRed.stop();
            playerCollision.play();
        }

        if (xball < 2 || xball > maxWidth-13) {
            speedx = -speedx;
            CollisionSound.play();        }
        if (yball <7 || yball > maxHeight-18) {
            speedy = -speedy;
            CollisionSound.play();
        }

        xball += speedx;
        yball += speedy;

        speedx *= 0.9999999;
        speedy *= 0.999999999;
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private boolean[] keys = new boolean[256];

    public void handleKeyPressed() {
        if (keys[KeyEvent.VK_UP] && yblue < maxHeight - 20) {
            yblue += 5;
        }

        if (keys[KeyEvent.VK_DOWN] && yblue > 12) {
            yblue -= 5;
        }

        if (keys[KeyEvent.VK_LEFT] && xblue > (maxWidth / 2)) {
            xblue -= 5;
        }

        if (keys[KeyEvent.VK_RIGHT] && xblue < maxWidth - 15) {
            xblue += 5;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        //for debugging
        System.out.println("Key pressed: " + key);

        if (page == 0) {
            if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_E) {
                System.exit(0); // exit
            }
            if (key == KeyEvent.VK_1) {// if press 1 => means 1 player
                gamerun1p = true;
                page = 2;
            }
            if (key == KeyEvent.VK_2) { // if press 2 => means 2 player
                gamerun1p = false;
                page = 2;
            }
            if (key == KeyEvent.VK_H) { // go to help
                page = 1;
            }
        }

        if (page == 2) {
            if (key == KeyEvent.VK_E) {// if press E => means  Easy
                ailevel = 1;
                page = 3;
            }
            if (key == KeyEvent.VK_M) { // if press M => means Medium
                ailevel = 2;
                page = 3;
            }
            if (key == KeyEvent.VK_H) { // if press H => means Hard
                ailevel = 3;
                page = 3;
            }
        }

        if (page == 1 || page == 2) {
            if (key == KeyEvent.VK_ESCAPE) {
                page = 0; // home
            }
        }

        if (!gamerun1p) {
            keys[e.getKeyCode()] = true;
            handleKeyPressed();
        }
        e.getComponent().repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gamerun1p) {
            keys[e.getKeyCode()] = false;
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double tempXred = convertX(e.getX(), e.getComponent().getWidth()) - 5;
        double tempYred = convertY(e.getY(), e.getComponent().getHeight()) - 5;
        //for debugging
        System.out.println(tempXred + " " + tempYred);
        if (tempXred > 2 && tempXred < maxWidth / 2.4) {
            xred = tempXred;
        }
        if (tempYred > 6 && tempYred < maxHeight - 16) {
            yred = tempYred;
        }
    }

    private double convertX(double x, double width) {
        return (x / width) * 100;
    }

    private double convertY(double y, double height) {
        return (1 - y / height) * 100;
    }

    public void mouseClicked(MouseEvent e) {
        double x = convertX(e.getX(), e.getComponent().getWidth()) - 5;
        double y = convertY(e.getY(), e.getComponent().getHeight()) - 5;

        switch (page) {
            case 0:
                if (x >= 19 && x <= 71 && y >= 38 && y <= 44) {
                    page = 3; //game
                    gamerun1p = false;
                } else if (x >= 30 && x <= 59 && y >= 27 && y <= 35) {
                    page = 1; //help->rules
                } else if (x >= 32 && x <= 57 && y >= 17 && y <= 25) {
                    System.exit(0); //exit
                } else if (x >= 22 && x <= 68 && y >= 47 && y <= 52) {
                    page = 2; //levels
                    gamerun1p = true;
                }
                break;
            case 1:
                page = 0;
                break;
            case 2:
                if (x >= 66 && x <= 87 && y >= 75 && y <= 86) {
                    page = 0;
                } else if (x >= 12 && x <= 77 && y >= 39 && y <= 49) {
                    ailevel = 1;
                    page = 3;
                } else if (x >= 12 && x <= 77 && y >= 27 && y <= 37) {
                    ailevel = 2;
                    page = 3;
                } else if (x >= 12 && x <= 77 && y >= 15 && y <= 25) {
                    ailevel = 3;
                    page = 3;
                }
                break;
            case 4:
                page = 0;
                start = true;
                break;
        }
        e.getComponent().repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
