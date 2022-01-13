package com.thomax.letsgo.zoom.handling;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;


public class AppletHandling {
	public static void main(String[] args) {
		
	}
}

//事件处理
class ExampleEventHandling extends Applet implements MouseListener {
	
	private static final long serialVersionUID = -6120608693183938495L;
	
	/*** 在此html内使用ExampleEventHandling.class
	<html>
	<title>Event Handling</title>
	<hr>
	<applet codebase="http://amrood.com/applets" 
	        code="AppletGo.ExampleEventHandling.class"
	        width="300" 
	        height="300">
	</applet>
	<hr>
	</html>
	*/
	
	StringBuffer strBuffer;

	public void init() {
		addMouseListener(this);
		strBuffer = new StringBuffer();
		addItem("initializing the applet ");
	}

	public void start() {
		addItem("starting the applet ");
	}

	public void stop() {
		addItem("stopping the applet ");
	}

	public void destroy() {
		addItem("unloading the applet");
	}

	void addItem(String word) {
		System.out.println(word);
		strBuffer.append(word);
		repaint();
	}

	public void paint(Graphics g) {
		//Draw a Rectangle around the applet's display area.
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		//display the string inside the rectangle.
		g.drawString(strBuffer.toString(), 10, 20);
	}

	public void mouseEntered(MouseEvent event) {
	}
	public void mouseExited(MouseEvent event) {
	}
	public void mousePressed(MouseEvent event) {
	}
	public void mouseReleased(MouseEvent event) {
	}
	public void mouseClicked(MouseEvent event) {
		addItem("mouse clicked! ");
	}
	
}

//显示图片
class ImageDemo extends Applet {
	
	private static final long serialVersionUID = 1L;
	
	/*** 在此html内使用ImageDemo.class
	<html>
	<title>The ImageDemo applet</title>
	<hr>
	<applet code="AppletGo.ImageDemo.class" width="300" height="200">
	<param name="image" value="java.jpg">
	</applet>
	<hr>
	</html>
	*/
	
	private Image image;
	private AppletContext context;
  
	public void init(){
		context = this.getAppletContext();
		String imageURL = this.getParameter("image");
		if(imageURL == null){
			imageURL = "java.jpg";
		}
		try{
			URL url = new URL(this.getDocumentBase(), imageURL);
			image = context.getImage(url);
		}catch(MalformedURLException e){
			e.printStackTrace();
			// Display in browser status bar
			context.showStatus("Could not load image!");
		}
	}
	
	public void paint(Graphics g){
		context.showStatus("Displaying image");
		g.drawImage(image, 0, 0, 200, 84, null);
		g.drawString("www.javalicense.com", 35, 100);
	} 
	
}

//播放音频
class AudioDemo extends Applet {

	private static final long serialVersionUID = 1L;
	
	/*** 在此html内使用AudioDemo.class
	<html>
	<title>The AudioDemo applet</title>
	<hr>
	<applet code="AppletGo.AudioDemo.class" width="0" height="0">
	<param name="audio" value="test.wav">
	</applet>
	<hr>
	*/
	
	private AudioClip clip1;
	private AppletContext context;
	
	public void init(){
		context = this.getAppletContext();
		String audioURL = this.getParameter("audio");
		if(audioURL == null){
         audioURL = "default.au";
		}
		try{
			URL url = new URL(this.getDocumentBase(), audioURL);
			clip1 = context.getAudioClip(url);
		}catch(MalformedURLException e){
			e.printStackTrace();
			context.showStatus("Could not load audio file!");
		}
	}
	
	public void start(){
		if(clip1 != null){
			clip1.loop(); //音频播放
		}
	}
	
	public void stop(){
		if(clip1 != null)
		{
			clip1.stop(); //音频暂停
		}
	}
	
}

