package application.view;

import java.io.IOException;
import java.util.ArrayList;

import application.VoxspellMain;
import application.VoxspellModel;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControllerScore {
	@FXML private Button _back;
	@FXML private ChoiceBox _categoryChoice;
	@FXML private Label _score;
	
	private String _category;
	private int _myScore;
	private ArrayList<String> _arrayCategoryList;
	private ObservableList<String> _categoryList;
	
	private VoxspellMain _main;
	private VoxspellModel _model;
	@FXML
	private void categoryChanged(){
		_category = (String) _categoryChoice.getValue();
		//_model.setCategory(_category);   //This changes the category for spelling list as well, idk if want
		_myScore = _model.getTotalAccuracy(_category);
		_score.setText(_myScore+"%");
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
