package application.view;

import java.io.File;
import java.net.URL;
import java.rmi.server.SocketSecurityException;
import java.util.ResourceBundle;

import application.VoxspellMain;
import application.VoxspellModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class ControllerVideoReward implements Initializable{
	@FXML private  MediaView _mediaView;
	private MediaPlayer _mediaPlayer;
	private Media _media;
	private String source = "big_buck_bunny_1_minute.mp4";
	
	private VoxspellMain _main;
	private VoxspellModel _model;
	
	@FXML private Button _back;
	@FXML private Button _play;
	@FXML private Button _pause;
	@FXML private Button _fast;
	@FXML private Button _slow;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		_media = new Media(this.getClass().getResource(source).toExternalForm());
		_mediaPlayer = new MediaPlayer(_media);
		_mediaView.setMediaPlayer(_mediaPlayer);
		_mediaPlayer.setAutoPlay(true);
		DoubleProperty width = _mediaView.fitWidthProperty();
		DoubleProperty height = _mediaView.fitHeightProperty();
		width.bind(Bindings.selectDouble(_mediaView.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(_mediaView.sceneProperty(), "height"));
	}
	
	public void backToPreviousScene(){
		_mediaPlayer.dispose();
		Stage stage = (Stage) _back.getScene().getWindow();
		Scene scene = _main.getScene();
		stage.setScene(scene);
	    stage.show();
	}
	public void play(){
		_mediaPlayer.play();
		_mediaPlayer.setRate(1);
	}
	public void pause(){
		_mediaPlayer.pause();
	}
	public void fast(){
		_mediaPlayer.setRate(2);
	}
	public void slow(){
		_mediaPlayer.setRate(0.5);
	}
	

	public ControllerVideoReward(VoxspellModel voxspellModel, VoxspellMain voxspellMain){
		_model = voxspellModel;
		_main = voxspellMain;
	}
}
