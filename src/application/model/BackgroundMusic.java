package application.model;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class BackgroundMusic implements Initializable{
	private Path _path;
	private MediaPlayer _mediaPlayer;
	private Media _media;
	
	public BackgroundMusic(){
		_path = Paths.get(System.getProperty("user.dir") + "/src/application/assets/openSource.mp3");
		_media = new Media(_path.toFile().toURI().toString());
		_mediaPlayer = new MediaPlayer(_media);
		_mediaPlayer.setAutoPlay(false);
		_mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}
	public BackgroundMusic(Path path){
		_path = path;
	}
	
	public void setPath(String path){
		//tests if correct file
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
		//between 0.0 to 1.0
		_mediaPlayer.setVolume(volume);
	}
	
}
