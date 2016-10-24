package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import application.VoxspellMain;
import application.VoxspellModel;
import application.model.WordMastery;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerScore {
	@FXML private Button _back;
	@FXML private ChoiceBox _categoryChoice;
	@FXML private Label _score;
	@FXML private Button _clearStats;
	
	private String _category;
	private int _myScore;
	private ArrayList<String> _arrayCategoryList;
	private ObservableList<String> _categoryList;
	
	private VoxspellMain _main;
	private VoxspellModel _model;
	
	private HashMap<String, Integer> _stats;
	private ObservableList<WordMastery> _data;
	@FXML private TableView<WordMastery> _table;
	@FXML private TableColumn<WordMastery, String> _words;
	@FXML private TableColumn<WordMastery, String> _mastery;
	
	@FXML
	private void categoryChanged(){
		_category = (String) _categoryChoice.getValue();
		//_model.setCategory(_category);   //This changes the category for spelling list as well, idk if you want
		_myScore = _model.getTotalAccuracy(_category);
		_score.setText(_myScore+"%");
		this.setTable();
	}
	@FXML
	private void initialize(){
		_arrayCategoryList = new ArrayList<String>(_model.getCategoryList());
		_categoryList = FXCollections.observableArrayList(_arrayCategoryList);
		_category = _model.getCategory();
		
		_myScore = _model.getTotalAccuracy(_category);
		_score.setText(_myScore+"%");
		
		_categoryChoice.setItems(_categoryList);
		_categoryChoice.setValue(_model.getCategory());
		_categoryChoice.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            categoryChanged();
		});
		this.setTable();
	}
	@FXML
	private void setTable(){
		_data = FXCollections.observableArrayList();
		_stats = _model.getStats(_category);
		for(String s : _stats.keySet()){
			if(_stats.get(s) == 1){
				_data.add(new WordMastery(s.toString(),"mastered"));
			}else {
				_data.add(new WordMastery(s.toString(), "failed"));
			}
		}
		_words.setCellValueFactory(new PropertyValueFactory<WordMastery, String>("words"));
		_mastery.setCellValueFactory(new PropertyValueFactory<WordMastery, String>("mastery"));
		_table.setItems(_data);
	}
	@FXML
	private void clearStatistics(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Word List");
		alert.setHeaderText("Warning, statistics will be lost.");
		alert.setContentText("Are you sure you want to clear the statistics?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			_model.clearData();
			
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setHeaderText("Successful");
			alert.setContentText("Statistics has been cleared");

			alert.showAndWait();
		}
		this.setTable();
	}
	@FXML
	private void backToMenu(){
			Stage stage = (Stage) _back.getScene().getWindow();
			Scene scene = _main.getScene();
			stage.setScene(scene);
		    stage.show();
	}

	public ControllerScore(VoxspellModel voxspellModel, VoxspellMain voxspellMain){
		_model = voxspellModel;
		_main = voxspellMain;
	}

}
