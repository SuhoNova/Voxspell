package application.view;

import java.io.IOException;
import java.util.ArrayList;

import application.VoxspellMain;
import application.VoxspellModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ControllerSpellingDone {
	@FXML private Button _menu;
	@FXML private Button _again;
	@FXML private Button _nextCategory;
	@FXML private Button _video;
	@FXML private Label _accuracyLabel;
	
	private VoxspellMain _main;
	private VoxspellModel _model;
	private int _accuracy;
	private String _category;
	private ArrayList<String> _categoryList;
	private boolean _getReward;
	@FXML
	private void backToMenu(){
		Stage stage = (Stage) _menu.getScene().getWindow();
		Scene scene = _main.getScene();
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	private void spellingAgain(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(VoxspellMain.class.getResource("view/ViewMenu.fxml"));
		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> aClass){
				return new ControllerMenu(_model, _main);
			}
		});
		try {
			AnchorPane menu = (AnchorPane) loader.load();
			Stage stage = (Stage) _again.getScene().getWindow();
			Scene scene = new Scene(menu);
			loader.setLocation(VoxspellMain.class.getResource("view/ViewMenu.fxml"));
			ControllerMenu menuController = loader.getController();
			stage.setScene(scene);
			menuController.startApp();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void nextCategory(){
		int index = _categoryList.indexOf(_model.getCategory());
		System.out.println(_model.getCategory() + " " + index);
		index++;
		_category = _categoryList.get(index);
		System.out.println(_model.getCategory() + " " + index);
		System.out.println(_category);
		_model.setCategory(_category);
		spellingAgain();
	}
	@FXML
	private void videoReward(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(VoxspellMain.class.getResource("view/ViewVideoReward.fxml"));
		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> aClass){
				return new ControllerVideoReward(_model, _main);
			}
		});
		try {
			BorderPane spelling = (BorderPane) loader.load();
			Stage stage = (Stage) _video.getScene().getWindow();
			Scene scene = new Scene(spelling);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void initialize(){
		_category = _model.getCategory();
		_categoryList = _model.getCategoryList();
		if(_categoryList.indexOf(_category) == _categoryList.size()-1){
			_nextCategory.setText("Last Category");
			_nextCategory.setDisable(true);
		}
		if(!_getReward){
			_video.setDisable(true);
		}
		_accuracyLabel.setText("Score: " + _accuracy + "%");
	}
	
	public ControllerSpellingDone(VoxspellModel voxspellModel, VoxspellMain voxspellMain, int accuracy){
		_model = voxspellModel;
		_main = voxspellMain;
		_accuracy = accuracy;
		_getReward = _model.getReward();
		_model.setReward(false);
	}
}
