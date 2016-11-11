package org.ski.webcam;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.ski.webcam.WebcamFrame.RunnerThread;

public class BSFrame extends JFrame {
    Webcam webcam = new Webcam();
    BufferedImage image,fgMask;
    
	BackgroundSubtractorMOG2 fgbg =Video.createBackgroundSubtractorMOG2();
	Mat mask = new Mat(), bg = new Mat();
	Mat getMask(Mat mat){
		fgbg.apply(mat, mask);
		return mask;
	}

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BSFrame frame = new BSFrame();
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
	public BSFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 640+2*border, 960+2*border);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(border, border, border, border));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		new RunnerThread().start();
	}
	
	int border = 5;
	@Override
    public void paint(Graphics g) {
        super.paint(g);
        g = contentPane.getGraphics();

        if (image!=null){
            setSize(image.getWidth()+border, image.getHeight()+border);
            g.drawImage(image, 0, 0, rootPane);
        }
	}

	ImageConverter cvt1 = new ImageConverter();	
	ImageConverter cvt3 = new ImageConverter();
	public class RunnerThread extends Thread {
		public void run() {
			while (contentPane == null || contentPane.getGraphics() == null) {
				try {Thread.sleep(1000);}
				catch (InterruptedException ex) {}
			}
        	Graphics g = contentPane.getGraphics();				
			while (true) {			
				Mat mat = webcam.getNewImage(true);
				image = cvt3.mat2image(mat);
				fgbg.apply(mat, mask);
				fgMask = cvt1.mat2image(mask);
				if (image != null)
					g.drawImage(image, 0,0, null);
				if (fgMask != null)
					g.drawImage(fgMask, 0,image.getHeight(), null);

				try {
					Thread.sleep(30);
				} catch (InterruptedException ex) {
				}
			}
        }
    }

}
