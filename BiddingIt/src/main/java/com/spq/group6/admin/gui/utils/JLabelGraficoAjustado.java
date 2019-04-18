package com.spq.group6.admin.gui.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

/** Clase mejorada de JLabel para gestionar imagenes ajustadas al JLabel
 */
public class JLabelGraficoAjustado extends JLabel {
	// la posicion X,Y se hereda de JLabel
	protected int anchuraObjeto;   // Anchura definida del objeto en pixels
	protected int alturaObjeto;    // Altura definida del objeto en pixels
	protected double radsRotacion; // Rotacion del objeto en radianes
	protected float opacidad;      // Opacidad del objeto (0.0f a 0.1f)
	protected BufferedImage imagenObjeto;  // imagen para el escalado
	private static final long serialVersionUID = 1L;  // para serializar


	public JLabelGraficoAjustado( String nombreImagenObjeto, int anchura, int altura ) {
		setName( nombreImagenObjeto );
		opacidad = 1.0f;
		setImagen( nombreImagenObjeto ); // Cargamos el icono
		setSize( anchura, altura );
	}
	
	@Override
	public void setSize(int anchura, int altura) {
        if (anchura <= 0 && imagenObjeto!=null) anchura = imagenObjeto.getWidth();
        if (altura <= 0 && imagenObjeto!=null) altura = imagenObjeto.getHeight();
		anchuraObjeto = anchura;
		alturaObjeto = altura;
    	super.setSize( anchura, altura );
    	setPreferredSize( new Dimension( anchura, altura ));
	}
	
	/** Cambia la imagen del objeto
	 * @param nomImagenObjeto	Nombre fichero donde esta la imagen del objeto. Puede ser tambien un nombre de recurso desde el paquete de esta clase.
	 */
	public void setImagen( String nomImagenObjeto ) {
		File f = new File(nomImagenObjeto);
        URL imgURL = null;
        try {
        	imgURL = f.toURI().toURL();
    		if (!f.exists()) {
    			imgURL = JLabelGraficoAjustado.class.getResource( nomImagenObjeto ).toURI().toURL();
    		}
        } catch (Exception e) {}  // Cualquier error de carga, la imagen se queda nula
        if (imgURL == null) {
        	imagenObjeto = null;
        } else {
        	try {  // guarda la imagen para dibujarla de forma escalada despues
    			imagenObjeto = ImageIO.read(imgURL);
    		} catch (IOException e) {}  // Error al leer la imagen
        }
        if (imagenObjeto==null) {
			setOpaque( true );
			setBackground( Color.red );
			setForeground( Color.blue );
	    	setBorder( BorderFactory.createLineBorder( Color.blue ));
	    	setText( nomImagenObjeto );
        }
        repaint();
	}
	
	/** Devuelve la anchura del rectangulo grafico del objeto
	 * @return	Anchura
	 */
	public int getAnchuraObjeto() {
		return anchuraObjeto;
	}
	
	/** Devuelve la altura del rectangulo grafico del objeto
	 * @return	Altura
	 */
	public int getAlturaObjeto() {
		return alturaObjeto;
	}
	
	/** Devuelve la rotacion del objeto
	 * @return	Rotacion actual del objeto en radianes
	 */
	public double getRotacion() {
		return radsRotacion;
	}

	/** Modifica la rotacion del objeto
	 * @param rotacion	Nueva rotacion del objeto (en radianes)
	 */
	public void setRotacion( double rotacion ) {
		radsRotacion = rotacion;
		repaint(); // Si no repintamos aqui Swing no sabe que ha cambiado el dibujo
	}
	
	/** Devuelve la opacidad del objeto
	 * @return	Opacidad del objeto (0.0f transparente a 1.0f opaco)
	 */
	public float getOpacidad() {
		return opacidad;
	}

	/** Modifica la opacidad del objeto
	 * @param opacidad	Opacidad del objeto (0.0f transparente a 1.0f opaco)
	 */
	public void setOpacidad(float opacidad) {
		if (opacidad<0.0f || opacidad>1.0f) return; // No se cambia si el valor es invalido
		this.opacidad = opacidad;
		repaint(); // Si no repintamos aqui Swing no sabe que ha cambiado el dibujo
	}

	/** Actualiza la posicion del objeto
	 * @param x	Coordenada x (doble) - se redondea al pixel mas cercano
	 * @param y	Coordenada y (doble) - se redondea al pixel mas cercano
	 */
	public void setLocation( double x, double y ) {
		setLocation( (int)Math.round(x), (int)Math.round(y) );
	}
	
	// Dibuja este componente de una forma no habitual
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (imagenObjeto!=null) {
			Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
			int anc = anchuraObjeto;
			int alt = alturaObjeto;
			int iniX = 0;
			int iniY = 0;
			if (anc<=0) {
				anc = getWidth();
			} else {
				iniX = (getWidth() - anc) / 2;
			}
			if (alt<=0) {
				alt = getHeight();
			} else {
				iniY = (getHeight() - alt) / 2;
			}
			// Rotacion
			g2.rotate( radsRotacion, getWidth()/2, getHeight()/2 );  // Incorporar al grafico la rotacion definida
			// Transparencia
			g2.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacidad ) ); // Incorporar la transparencia definida
	        g2.drawImage(imagenObjeto, iniX, iniY, anc, alt, null);
		}
	}

	/** Metodo de prueba de label grafico
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame( "Prueba JLabelGraficoAjustado" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JLabelGraficoAjustado label = new JLabelGraficoAjustado( "coche.png", 100, 100 );
			// TODO probar este 300, 300 con diferentes tamanyos. Si x<=0 ajusta el ancho y si es y<=0 ajusta el alto
		f.setSize( 600, 400 );
		f.add( label, BorderLayout.CENTER );
		f.setVisible( true );
		try { Thread.sleep( 5000 ); } catch (Exception e) {}  // Espera 5 segundos
		for (int rot=0; rot<=200; rot++ ) {
			label.setRotacion( rot*Math.PI/100 );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos decimas entre rotacion y rotacion
		}
		for (int op=-100; op<=100; op++ ) {
			label.setOpacidad( Math.abs(op*0.01f) );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos decimas entre rotacion y rotacion
		}
	}
	
}
