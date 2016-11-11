package org.ski.webcam;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class WebcamFrame extends JFrame {
    Webcam webcam = new Webcam();
    BufferedImage image;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebcamFrame frame = new WebcamFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WebcamFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 642,482);
		contentPane = new JPanel();
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {ImageIO.write(image, "png",new File( "/home/huiying/Pictures/webcam.png"));
				} catch (IOException e1) {				}
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
        new RunnerThread().start();
	}
	
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g = contentPane.getGraphics();

        if (image!=null){
            setSize(image.getWidth()+5, image.getHeight()+5);
            g.drawImage(image, 0, 0, rootPane);
        }
	}

	public class RunnerThread extends Thread {
		public void run() {
			while (contentPane == null || contentPane.getGraphics() == null) {
				try {Thread.sleep(1000);}
				catch (InterruptedException ex) {}
			}
        	Graphics g = contentPane.getGraphics();				
			while (true) {			
				image = webcam.getOneFrame();
				if (image != null)
					g.drawImage(image, 0,0, rootPane);
				try {
					Thread.sleep(30);
				} catch (InterruptedException ex) {
				}
			}
        }
    }
}
