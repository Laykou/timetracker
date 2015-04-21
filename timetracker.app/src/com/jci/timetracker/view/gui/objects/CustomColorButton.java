package com.jci.timetracker.view.gui.objects;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * Custom button that has gradient from up to bottom - from ligter to darker color and changes on hover. You can customize the color of background and font and specify for example BOLD text.
 * 
 * @author Ladislav Gallay
 * 
 */
public class CustomColorButton extends JButton implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private boolean hovered = false;
	private boolean clicked = false;

	private Color normalColor = null;
	private Color lightColor = null;
	private Color darkColor = null;

	/**
	 * Create custom button with background and font color. Gradient will be automatically generated with {@link Color#brighter()} and {@link Color#darker()} methods. You can make the text bold by setting fontderovation to <b>Font.BOLD</b>
	 * 
	 * @param normalColor
	 * @param fontColor
	 * @param fontDerivation
	 */
	public CustomColorButton(Color normalColor, Color fontColor, int fontDerivation)
	{
		this(normalColor, normalColor.brighter(), normalColor.darker(), fontColor);

		setFont(getFont().deriveFont(fontDerivation));
	}

	/**
	 * Create custom button with background and font color. Gradient will be automatically generated with {@link Color#brighter()} and {@link Color#darker()} methods.
	 * 
	 * @param normalColor
	 * @param fontColor
	 */
	public CustomColorButton(Color normalColor, Color fontColor)
	{
		this(normalColor, normalColor.brighter(), normalColor.darker(), fontColor);
	}

	public CustomColorButton(Color normalColor, Color lightColor, Color darkColor, Color fontColor)
	{
		setForeground(fontColor);

		this.normalColor = normalColor;
		this.lightColor = lightColor;
		this.darkColor = darkColor;

		addMouseListener(this);
		setContentAreaFilled(false);
	}

	/**
	 * Overpainting component, so it can have different colors
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		GradientPaint gp = null;

		if (clicked)
			gp = new GradientPaint(0, 0, darkColor, 0, getHeight(), darkColor.darker());
		else if (hovered)
			gp = new GradientPaint(0, 0, lightColor, 0, getHeight(), lightColor.darker());
		else
			gp = new GradientPaint(0, 0, normalColor, 0, getHeight(), normalColor.darker());

		g2d.setPaint(gp);

		// Draws the rounded opaque panel with borders.
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // For High quality
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 7, 7);

		g2d.setColor(darkColor.darker().darker());
		g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 7, 7);

		// g.fillRect(0, 0, getSize().width, getSize().height);

		super.paintComponent(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{

	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		hovered = true;
		clicked = false;

		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		hovered = false;
		clicked = false;

		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		hovered = true;
		clicked = true;

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		hovered = true;
		clicked = false;

		repaint();
	}
}