package Man;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* package project; */

import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.opengl.*;
import javax.swing.*;

public class Run extends JFrame  {
    public static void main(String[] args) {
        new Run();
    }
    boolean run=false;
    Animator animator;
    public Run() {
        GLCanvas glcanvas;
        JButton start =new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (run){
                    start.setText("Start");
                    animator.stop();
                }
                else {
                    animator.start();
                    start.setText("Stop");
                }
                run=!run;
            }
        });
        AnimListener listener = new AirHockey();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(10);
        animator.start();
        animator.add(glcanvas);
        JPanel panel = new JPanel();
        panel.add(start, BorderLayout.CENTER);
        add(panel,BorderLayout.SOUTH);
        setTitle(" ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}
