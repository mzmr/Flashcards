package fiszki.views;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener {
	private static final long serialVersionUID = 8L;
	private final String hint;
	private boolean showingHint;
	private static final Color FNT_HINT = new Color(0xA0A0A0);
	private static final Color FNT_TEXT = new Color(0x000000);
	
	public HintTextField(final String hint) {
		super(hint);
		super.setForeground(FNT_HINT);
		this.hint = hint;
		this.showingHint = true;
		super.addFocusListener(this);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		if(this.getText().isEmpty()) {
			showingHint = false;
			super.setText("");
			super.setForeground(FNT_TEXT);			
		}
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		if(this.getText().isEmpty()) {
			showingHint = true;
			super.setForeground(FNT_HINT);
			super.setText(hint);			
		}
	}
	
	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}
	
	@Override
	public void setText(String t) {
		if (Objects.equals(t, "")) {
			if (hasFocus())
				super.setText("");
			else {
				showingHint = true;
				super.setText(hint);
			}
				
		} else
			super.setText(t);;
	}
}