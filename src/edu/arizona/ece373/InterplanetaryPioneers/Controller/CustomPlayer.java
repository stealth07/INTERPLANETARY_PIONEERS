package edu.arizona.ece373.InterplanetaryPioneers.Controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import javazoom.jl.player.Player;

public class CustomPlayer {
	private Player player;
	private FileInputStream FIS;
	private BufferedInputStream BIS;
	private boolean canResume;
	private String path;
	private int total;
	private int stopped;
	private boolean valid;
	private boolean over;
	
	public CustomPlayer(){
	    player = null;
	    FIS = null;
	    valid = false;
	    BIS = null;
	    path = null;
	    total = 0;
	    stopped = 0;
	    canResume = false;
	}

	public boolean canResume(){
	    return canResume;
	}

	public void setPath(String path){
	    this.path = path;
	}

	public void pause(){
	    try{
		    stopped = FIS.available();
		    player.close();
		    FIS = null;
		    BIS = null;
		    player = null;
		    if(valid) canResume = true;
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}

	public void resume(){
	    if(!canResume) return;
	    if(play(total-stopped)) canResume = false;
	}

	public boolean play(int pos){

	    valid = true;
	    canResume = false;
	    try{
		    FIS = new FileInputStream(path);
		    total = FIS.available();
		    if(pos > -1) FIS.skip(pos);
		    BIS = new BufferedInputStream(FIS);
		    player = new Player(BIS);
	    new Thread(
	            new Runnable(){
	                public void run(){
	                    try{
	                        player.play();
	                    }catch(Exception e){
	                        JOptionPane.showMessageDialog(null, "Error playing mp3 file");
	                        valid = false;
	                    }
	                }
	            }
	    ).start();
	    }catch(Exception e){
	    	e.printStackTrace();
	        valid = false;
	    }
	    return valid;
	}
	
	public boolean isOver(){
		try {
			if(FIS.available() >= 10) over = false;
			else over = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
			System.exit(0);
		}
		return over;
	}

}