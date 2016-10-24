package application.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import application.VoxspellMain;
import application.VoxspellModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControllerSettings {
	@FXML private ChoiceBox _voiceChoice; 
	@FXML private ChoiceBox _category;
	@FXML private Button _fileFinder;
	@FXML private Button _back;
	@FXML private Button _default;
	@FXML private Label _wordList;

	private VoxspellMain _main;
	private VoxspellModel _model;
	private ControllerMenu _vMenuController;

	private ArrayList<String> _arrayVoiceList;
	private ObservableList<String> _voiceList;
	private ArrayList<String> _arrayCategoryList;
	private ObservableList<String> _categoryList;
	private Path _pathWordList;

	private FileChooser _fChooser;


	@FXML
	private void initialize(){
		_arrayVoiceList = new ArrayList<String>(_model.getVoiceList());
		_voiceList = FXCollections.observableArrayList(_arrayVoiceList);
		_pathWordList = _model.getWordListPath();
		_arrayCategoryList = new ArrayList<String>(_model.getCategoryList());
		_categoryList = FXCollections.observableArrayList(_arrayCategoryList);

		_voiceChoice.setItems(_voiceList);
		_voiceChoice.setValue(_model.getVoice());
		_category.setItems(_categoryList);
		_category.setValue(_model.getCategory());
		_wordList.setText(_pathWordList.getFileName().toString());
	}
	@FXML
	private void changeVoice(){
		String voice = (String) _voiceChoice.getValue();
		_model.setVoice(voice);
	}
	@FXML
	private void changeCategory(){
		String category = (String) _category.getValue();
		_model.setCategory(category);
	}
	@FXML
	private void changeWordList(){
		File file = _fChooser.showOpenDialog((Stage) _back.getScene().getWindow());
		if(file!=null){
			Path p = Paths.get(file.getPath());
			if(!checkWordListFile(p)){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Error in opening word list");
				alert.setContentText("Make sure it is a txt file with '%' as categories");

				alert.showAndWait();
			} else{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Word List");
				alert.setHeaderText("Warning, statistics will be lost.");
				alert.setContentText("Are you sure you want to change word list?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successful");
					alert.setHeaderText("Successful");
					alert.setContentText("Word list has been changed");

					alert.showAndWait();
						// If valid word list file change model to add wordlist and categorys and reset data
						_model.setWordListPath(p);
						_pathWordList = _model.getWordListPath();
						_arrayCategoryList = new ArrayList<String>(_model.getCategoryList());
						_categoryList = FXCollections.observableArrayList(_arrayCategoryList);
			
						_category.setItems(_categoryList);
						_category.setValue(_model.getCategory());
						_wordList.setText(_pathWordList.getFileName().toString());
						_model.clearData();
				}
			}
		}
	}
	@FXML
	private void defaultWordList(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Word List");
		alert.setHeaderText("Warning, statistics will be lost.");
		alert.setContentText("Are you sure you want to change word list?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			Path p = _model.getDefaultWordListPath();
			_model.setWordListPath(p);
			_pathWordList = _model.getWordListPath();
			_arrayCategoryList = new ArrayList<String>(_model.getCategoryList());
			_categoryList = FXCollections.observableArrayList(_arrayCategoryList);

			_category.setItems(_categoryList);
			_category.setValue(_model.getCategory());
			_wordList.setText(_pathWordList.getFileName().toString());
			_model.clearData();
			
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setHeaderText("Successful");
			alert.setContentText("WordList is back to default");

			alert.showAndWait();
		}
	}
	private boolean checkWordListFile(Path path){
		String fileName = path.getFileName().toString();
		if(!fileName.endsWith(".txt")){
			return false;
		}else{
			if(_model.checkCorrectWordListFile(path)){
				return true;
			}
		}
		return false;
	}

	@FXML
	private void backToMenu(){
		changeVoice();
		changeCategory();
		Stage stage = (Stage) _back.getScene().getWindow();
		Scene scene = _main.getScene();
		stage.setScene(scene);
		stage.show();
	}

	public ControllerSettings(VoxspellModel voxspellModel, VoxspellMain voxspellMain){
		_model = voxspellModel;
		_main = voxspellMain;
		_fChooser = new FileChooser();
		_fChooser.setTitle("Open WordList");
	}
}
