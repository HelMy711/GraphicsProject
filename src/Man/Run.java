package Man;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.media.opengl.*;
import javax.swing.*;

public class Run extends JFrame {
    public static void main(String[] args) {
        new Run();
    }

    private boolean isAnimating = false; // لتعقب حالة المشغل
    private Animator animator;

    public Run() {
        GLCanvas glcanvas;
        JButton startButton = new JButton("Start");

        // إضافة المستمع للزر
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

        // إعداد GLCanvas والمستمعات
        AnimListener listener = new AirHockey();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
//        glcanvas.addMouseListener((MouseListener) listener);
        glcanvas.addMouseMotionListener((MouseMotionListener) listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);

        // إعداد المشغل
        animator = new FPSAnimator(glcanvas, 60); // تشغيل 60 إطارًا في الثانية

        // إعداد لوحة الأزرار
        JPanel panel = new JPanel();
        panel.add(startButton, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // إعداد الإطار
        setTitle("Air Hockey");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);

        // طلب التركيز على لوحة GLCanvas
        glcanvas.requestFocus();
    }
}
