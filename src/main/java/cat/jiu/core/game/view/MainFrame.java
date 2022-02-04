package cat.jiu.core.game.view;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public MainFrame() {
		super("打飞机");
		this.setBounds(100, 100, 512, 721);
		Image icon = new ImageIcon("resource/img/plane/plane_0.png").getImage();
		this.setIconImage(icon);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addListener();
	}
	
	private void addListener() {
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent event) {
				new Thread(() -> {
					int x = event.getX();
					int y = event.getY();
					Image plane = new ImageIcon("resource/img/plane/plane_0.png").getImage();
					
					Container con = getContentPane();
					con.add(new PlaneCanvas(x, y, plane));
					con.validate();
					repaint();
				}).start();
			}
			
//			public void mouseMoved(MouseEvent event) {
//				new Thread(() -> {
//					int x = event.getX();
//					int y = event.getY();
//					Image plane = new ImageIcon("resource/img/plane/plane_0.png").getImage();
//					
//					Container con = getContentPane();
//					con.add(new PlaneCanvas(x, y, plane));
//					con.validate();
//					repaint();
//				}).start();
//			}
        });
    }
	
	class PlaneCanvas extends Canvas {
		private static final long serialVersionUID = 2L;
		int x;
		int y;
		Image image;
		public PlaneCanvas(int x, int y, Image image) {
			this.x = x;
			this.y = y;
			this.image = image;
		}
		
		@Override
		public void paint(Graphics graphics) {
			Graphics2D g = (Graphics2D) graphics;
			g.drawImage(this.image, this.x, this.y, this.image.getWidth(this) / 5, this.image.getHeight(this) / 5, this);
			System.out.println(x + " | " + y);
		}
	}
}
