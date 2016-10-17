package application.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VideoReward {
	private Path _path;
	public VideoReward(){
		_path = Paths.get(System.getProperty("user.dir") + "/src/application/assets/big_buck_bunny_1_minute.mp4");
	}

	public void start(Stage primaryStage) {
		MediaPlayer mediaplayer;
		Button btn_play, btn_pause, btn_stop;

		btn_play = new Button("Start");
		btn_pause = new Button("Pause");
		btn_stop = new Button("Stop");

		Media videoFile = new Media(new File("//home/alex/Desktop/big_buck_bunny_1_minute.mp4").toURI().toString());

		mediaplayer = new MediaPlayer(videoFile);
		mediaplayer.setAutoPlay(true);
		mediaplayer.setVolume(0.1);

		MediaView mediaView = new MediaView(mediaplayer);

		VBox root = new VBox();
		root.getChildren().addAll(btn_play,btn_pause,btn_stop,mediaView);

		Scene scene = new Scene(root, 500, 500);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

}
