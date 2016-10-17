package application.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.VoxspellMain;
import application.VoxspellModel;
import application.model.WordMastery;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

public class ControllerStatistics implements Initializable {
	@FXML private ChoiceBox _categoryChoice;
	@FXML private Button _clearStats;
	@FXML private Button _back;
	//@FXML private TreeTableView<WordMastery> _categoryTable;
	//private TreeTableColumn<WordMastery,String> _word;
	//private TreeTableColumn<WordMastery,Integer> _mastery;
	
	private ArrayList<String> _arrayCategoryList;
	private ObservableList<String> _categoryList;
	private String _category;
	private HashMap<String, Integer> _stats;
	//private ObservableList<String> _data;
	
	private VoxspellMain _main;
	private VoxspellModel _model;
	
	@FXML
	private void backToMenu(){
		Stage stage = (Stage) _back.getScene().getWindow();
		Scene scene = _main.getScene();
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	private void setTable(){
		_stats = _model.getStats(_category);
		for(String s : _stats.keySet()){
			System.out.println(s +" " +  _stats.get(s));
			//_word.

		}
		System.out.println("-------");
	}
	@FXML
	private void changeCategory(){
		_category = (String) _categoryChoice.getValue();
		_categoryChoice.setValue(_category);
		setTable();
	}
	@FXML
	private void clearStatistics(){
		_model.clearData();
	}

	public ControllerStatistics(VoxspellModel model, VoxspellMain main){
		_model = model;
		_main = main;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		_arrayCategoryList = new ArrayList<String>(_model.getCategoryList());
		_categoryList = FXCollections.observableArrayList(_arrayCategoryList);
		_category = _model.getCategory();
		
		_categoryChoice.setItems(_categoryList);
		_categoryChoice.setValue(_category);
		_categoryChoice.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
			changeCategory();
		});
		
		setTable();
	}
}
