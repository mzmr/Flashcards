package fiszki.views;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class IconTextField extends HintTextField {
	private static final long serialVersionUID = 9L;
	private BufferedImage image = null;
	private int x0;
	
	public IconTextField(final String hint) {
		super(hint);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int y = (getHeight() - image.getHeight())/2;
		g.drawImage(image, x0, y, this);
	}
	
	public void setIconURL(URL iconUrl) {
		try {
			image = ImageIO.read(iconUrl);
			Border border = UIManager.getBorder("TextField.border");
			x0 = border.getBorderInsets(new JTextField()).left;
			setMargin(new Insets(2, 2, 2, 2));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void setMargin(Insets m) {
		int imgWidth = image == null ? 0 : image.getWidth();
		super.setMargin(new Insets(m.top, m.left + x0 + imgWidth, m.bottom, m.right));
	}
}
