package Man;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import javax.media.opengl.*;
import javax.swing.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Run extends JFrame {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        new Run();
    }

    private boolean isAnimating = false; // لتعقب حالة المشغل
    private Animator animator;

    public Run() {
        GLCanvas glcanvas;
        JButton startButton = new JButton("Start");

        JButton Button2 = new JButton("HighScore");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAnimating) {
                    animator.start();
                    startButton.setText("Stop");
                    isAnimating = true;
                } else {
                    animator.stop();
                    startButton.setText("Start");
                    isAnimating = false;
                }
            }
        });




        Button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("============================================");
                System.out.println("============================================");
                 readFromFile();
                System.out.println("============================================");
                System.out.println("============================================");

            }
        });



















        // إعداد GLCanvas والمستمعات
        AnimListener listener = new AirHockey();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
//        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
//        glcanvas.addMouseListener((MouseListener) listener);
        glcanvas.addMouseMotionListener((MouseMotionListener) listener);
        glcanvas.addMouseListener((MouseListener) listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(glcanvas, 60);

//        JPanel panel = new JPanel();
//        panel.add(startButton, BorderLayout.CENTER);
//        add(panel, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // المحاذاة لليسار
        panel.add(startButton);  // إضافة الزر إلى اللوحة
        panel.add(Button2);
        add(panel, BorderLayout.SOUTH);  // إضافة اللوحة في الجزء السفلي

        setTitle("Air Hockey");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
    public static void readFromFile() {
        String filename="playersNamesAndTheirScore.txt";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filename));  // Open file for reading
            String line;

            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Print each line
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();  // Close the file
                }
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }
}
