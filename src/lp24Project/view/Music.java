package lp24Project.view;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Music {
	//Singleton of the Music Class
	public static final Music INSTANCE = new Music();
	private Player player; 
    private boolean loopMusic = true;

    private Music()
    {
  
    }
    
    //Boolean used to stop the loop thread
    public void setLoopMusic(boolean bool)
    {
    	loopMusic = bool;
    }
    
    public void loopPlaying()
    {
		new Thread() {
			public void run()
			{
				try {
					play();
					while(loopMusic == true)
					{
						if(player.isComplete())
						{
							stopPlaying();
							play();
						}
					}
					stopPlaying();
					loopMusic = true;
				} catch (Exception e) { System.out.println(e); }
			}
		}.start();
    }
    
    // play the MP3 file to the sound card
    private void play() {
    	try {
    		//Instantiate the stream the file is read on (we have to re instantiate a new one every time we want the music to start again)
            FileInputStream fis = new FileInputStream("resources/music.mp3");
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { 
                	player.play();
                }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
        
    }
    
    private void stopPlaying()
    {
    	player.close();
    }
	
}
