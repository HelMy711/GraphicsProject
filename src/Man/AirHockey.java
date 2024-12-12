package Man;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

public class AirHockey extends AnimListener implements MouseMotionListener {
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

    double xball = 45;
    double yball = 45;
    double xred = 10;
    double yred = 45;
    double xblue = 80;
    double yblue = 45;
    boolean gamerun1p = true; /* For two players make it false */
    int t1 = 4;
    int t2 = 4;
    int t3 = 4;
    int t4 = 4;
    int time = 120;

    String[] textureNames = {
            "bluehockeystick.png", //0
            "field.png", //1
            "puck.png", //2
            "redhockeystick.png" //3
            , "0.png", "1 .png", "2 .png", "3.png", "4.png", "5.png", "6.png", "7.png", "8.png", "9.png", "colon.png", "colon1.png", "colon2.png",
            "game-rules.png"
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

        if (gamerun1p) {
            run1p(gl);
        } else
            run2p(gl);
        /* For two players method mouse and Keyboard */
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
        time--;
        calctime();
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

        Drawnum(gl, 53, 91, t3, 0.7F);
        Drawnum(gl, 60, 91, t4, 0.7F);
        Drawnum(gl, 46, 91, 16, 0.6F);
        Drawnum(gl, 39, 91, t2, 0.7F);
        Drawnum(gl, 32, 91, t1, 0.7F);
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
                    70,82,
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
        time--;
        calctime();
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

        Drawnum(gl, 53, 91, t3, 0.7F);
        Drawnum(gl, 60, 91, t4, 0.7F);
        Drawnum(gl, 46, 91, 16, 0.6F);
        Drawnum(gl, 39, 91, t2, 0.7F);
        Drawnum(gl, 32, 91, t1, 0.7F);
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

    public void calcscore() {
        if (xball <= 2 && yball >= 30 && yball <= 60) {
            xball = maxWidth;
            yball = maxHeight;
            s4++;
            if (s4 == 14) {
                s4 = 4;
                s3++;
            }

            speedx = 0;
            speedy = 0;
        }
        if (xball >= 85 && yball >= 30 && yball <= 60) {
            xball = maxWidth;
            yball = maxHeight;
            s2++;
            if (s2 == 14) {
                s2 = 4;
                s1++;
            }
            speedx = 0;
            speedy = 0;
        }


    }

    public void calctime() {
        if (time <= 0) {
            time = 120;
            t4++;

            if (t4 == 14) {
                t3++;
                t4 = 4;
            }

            if (t3 == 10) {
                t2++;
                t3 = 4;
            }
            if (t2 == 14) {
                t1++;
                t2 = 4;
            }
        }

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

    void checkCollision() {
        // blue player
        if (Math.abs(xball - xblue) < 5 && Math.abs(yball - yblue) < 10) {
            ballStationary = false;
            speedx = (xball - xblue) * 0.1;
            speedy = (yball - yblue) * 0.1;
        }
        // red player
        if (Math.abs(xball - xred) < 5 && Math.abs(yball - yred) < 10) {
            ballStationary = false;
            speedx = (xball - xred) * 0.1;
            speedy = (yball - yred) * 0.1;
        }

        // collision with walls الحيطه يعني
        if (xball < 2 || xball > maxWidth - 13) {
            speedx = -speedx;
        }
        if (yball < 7 || yball > maxHeight - 18) {
            speedy = -speedy;
        }
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

        if (keys[KeyEvent.VK_LEFT] && xblue > (maxWidth / 2) - 5) {
            xblue -= 5;
        }

        if (keys[KeyEvent.VK_RIGHT] && xblue < maxWidth - 15) {
            xblue += 5;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gamerun1p) {
            keys[e.getKeyCode()] = true;
            handleKeyPressed();
        }
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
        if (tempXred > 2 && tempXred < maxWidth / 2.20) {
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


}
