package application.model;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 * This class deals with background music.
 * The open source music is EARLY ROOTS OF JAZZ: “WASHBOARD WIGGLES”
 * from http://humanmusicintelligence.com/early-roots-of-jazz-washboard-wiggles/
 * The music will go on indefinitely as it will start again when finished
 * 
 * @author syu680, Alex (Suho) Yu
 *
 */
public class BackgroundMusic implements Initializable{
	private Path _path;
	private MediaPlayer _mediaPlayer;
	private Media _media;
	
	public BackgroundMusic(){
		_path = Paths.get(System.getProperty("user.dir") + "/assets/openSource.mp3");
		_media = new Media(_path.toFile().toURI().toString());
		_mediaPlayer = new MediaPlayer(_media);
		_mediaPlayer.setAutoPlay(false);
		_mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}
	public BackgroundMusic(Path path){
		_path = path;
	}
	
	public void setPath(String path){
		_path = Paths.get("test.mp3");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	public void pause(){
		_mediaPlayer.pause();
	}
	public void play(){
		_mediaPlayer.play();
	}
	public void setVolume(double volume){
		//between 0.0 (min) to 1.0 (max)
		_mediaPlayer.setVolume(volume);
	}
	
}
