package application.view;

import java.io.IOException;
import application.VoxspellMain;
import application.VoxspellModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * This class is a controller class for Menu
 * It shows what method is executed when pressed on the menu gui
 * Currently it moves the scene to: spelling, score, settings, and exit
 * It also handles background music
 * @author syu680, Alex (Suho) Yu
 *
 */

public class ControllerMenu {
	@FXML private Button _btnStart;
	@FXML private CheckBox _review;
	@FXML private Button _btnStatistics;
	@FXML private Button _btnScore;
	@FXML private Button _btnSetting;
	@FXML private Button _btnExit;
	@FXML private CheckBox _btnBGM;
	@FXML private Slider _volumeSlider;

	private VoxspellMain _main;
	private VoxspellModel _model;

	public ControllerMenu(VoxspellModel model, VoxspellMain main){
		_model = model;
		_main = main;
	}
	/**
	 * Makes scene for spelling
	 */
	@FXML
	public void startApp(){
		if(_model.getCategory().equals("review") && _model.getTestWordList("review").isEmpty()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Review Quiz");
			alert.setHeaderText("There is nothing to review");
			alert.setContentText("Please change the category");

			alert.showAndWait();
		} else {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(VoxspellMain.class.getResource("view/ViewSpelling.fxml"));
			loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> aClass){
					return new ControllerSpelling(_model, _main);
				}
			});
			try {
				AnchorPane spelling = (AnchorPane) loader.load();
				Stage stage = (Stage) _btnStart.getScene().getWindow();
				Scene scene = new Scene(spelling);
				loader.setLocation(VoxspellMain.class.getResource("view/ViewSpelling.fxml"));
				ControllerSpelling vSettingsController = loader.getController();
				stage.setScene(scene);
				stage.show();
				vSettingsController.startQuiz();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Scene for keeping score
	 */
	@FXML
	private void score(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(VoxspellMain.class.getResource("view/ViewScore.fxml"));
		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> aClass){
				return new ControllerScore(_model, _main);
			}
		});
		try {
			AnchorPane score = (AnchorPane) loader.load();
			Stage stage = (Stage) _btnScore.getScene().getWindow();
			Scene scene = new Scene(score);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Scene for settings gui
	 */
	@FXML
	private void settings(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(VoxspellMain.class.getResource("view/ViewSettings.fxml"));
		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> aClass){
				return new ControllerSettings(_model,_main);
			}
		});
		try {
			AnchorPane settings = (AnchorPane) loader.load();
			Stage stage = (Stage) _btnSetting.getScene().getWindow();
			Scene scene = new Scene(settings);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loader.setLocation(VoxspellMain.class.getResource("view/ViewSettings.fxml"));
	}
	/**
	 * Exiting app
	 */
	@FXML
	private void exitApp(){
		try {
			Platform.exit();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Background Music, handles volume, and whether it should play or pause
	 */
	@FXML
	private void backgroundMusic(){
		if(_btnBGM.isSelected()){
			_model.musicPlay();
		}else {
			_model.musicPause();
		}
	}
	@FXML
	private void setBGMVolume(){
		_model.setBGMVolume(_volumeSlider.getValue());
	}
	@FXML
	private void initialize(){
	}
}
