package application.view;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import application.VoxspellMain;
import application.VoxspellModel;
import application.model.VideoRewardFFMPEG;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

/**
 * This class makes a scene to put mediaplayer in.
 * The mediaplayer plays the video
 * The user has the option to watch it slowmotion, or fast motion
 * or normal motion. The user can also pause or play the video.
 * The user can also quit mid way back to menu
 * 
 * @author syu680, Alex (Suho) Yu
 *
 */
public class ControllerVideoReward implements Initializable{
	@FXML private  MediaView _mediaView;
	private MediaPlayer _mediaPlayer;
	private Media _media;
	private String _source;
	
	private VoxspellMain _main;
	private VoxspellModel _model;
	
	@FXML private Button _back;
	@FXML private Button _play;
	@FXML private Button _ffmpegVideo;
	@FXML private Button _fast;
	@FXML private Button _slow;
	
	private Path _path;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//_ffmpegVideo.setDisable(true);
		_path = Paths.get(System.getProperty("user.dir") + "/assets/big_buck_bunny_1_minute.mp4");
		_source = "file:///" + _path.toString();
		_media = new Media(_source);
		_mediaPlayer = new MediaPlayer(_media);
		_mediaView.setMediaPlayer(_mediaPlayer);
		_mediaPlayer.setAutoPlay(true);
		DoubleProperty width = _mediaView.fitWidthProperty();
		DoubleProperty height = _mediaView.fitHeightProperty();
		width.bind(Bindings.selectDouble(_mediaView.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(_mediaView.sceneProperty(), "height"));
		_play.setText("Pause");
	}
	
	public void backToPreviousScene(){
		_mediaPlayer.dispose();
		Stage stage = (Stage) _back.getScene().getWindow();
		Scene scene = _main.getScene();
		stage.setScene(scene);
	    stage.show();
	}
	/**
	 * Plays/pause the video and changes the speed of video back to normal 
	 */
	public void play(){
		if(_mediaPlayer.getStatus().equals(Status.PLAYING)){
			_mediaPlayer.pause();
			_play.setText("Play");
		}else{
			_mediaPlayer.play();
			_play.setText("Pause");
		}
		_mediaPlayer.setRate(1);
	}
	/**
	 * ffmpeg video reward 
	 */
	public void ffmpegVideoReward(){
		_play.setDisable(true);
		_fast.setDisable(true);
		_slow.setDisable(true);
		_ffmpegVideo.setDisable(true);
		_ffmpegVideo.setText("Processing Please wait...");
		Path p = _path;
		VideoRewardFFMPEG fVideo = new VideoRewardFFMPEG(this,p);
		fVideo.execute();
	}
	public void playVideo(Path path){
		_ffmpegVideo.setDisable(true);
		_source = "file:///" + path.toString();
		_media = new Media(_source);
		_mediaPlayer = new MediaPlayer(_media);
		_mediaView.setMediaPlayer(_mediaPlayer);
		_mediaPlayer.setAutoPlay(true);
		DoubleProperty width = _mediaView.fitWidthProperty();
		DoubleProperty height = _mediaView.fitHeightProperty();
		width.bind(Bindings.selectDouble(_mediaView.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(_mediaView.sceneProperty(), "height"));
		_play.setDisable(false);
		_fast.setDisable(false);
		_slow.setDisable(false);
		_play.setText("Pause");
		
	}
	/**
	 * Changes video into fast motion
	 */
	public void fast(){
		_mediaPlayer.setRate(2);
	}
	/**
	 * Changes video into slow motion
	 */
	public void slow(){
		_mediaPlayer.setRate(0.5);
	}
	
	public ControllerVideoReward(VoxspellModel voxspellModel, VoxspellMain voxspellMain){
		_model = voxspellModel;
		_main = voxspellMain;
	}
}
