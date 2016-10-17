package application;

import java.io.IOException;

import application.view.ControllerMenu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class VoxspellMain extends Application {
	private Stage _primaryStage;
	private VoxspellModel _model;
	private Scene _scene;

    @Override
    public void start(Stage primaryStage) {
        _primaryStage = primaryStage;
        _primaryStage.setTitle("Voxspell");
        _model = new VoxspellModel();
        
        initRootLayout();

    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
		try {
			// Load layout from fxml file
	    	FXMLLoader loader = new FXMLLoader();
	    	loader.setLocation(VoxspellMain.class.getResource("view/ViewMenu.fxml"));
	    	VoxspellMain temp = this;
	    	loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> aClass){
					return new ControllerMenu(_model, temp);
				}
			});
	        AnchorPane menu = (AnchorPane) loader.load();
			// show the scene contaning the layout
	        _scene = new Scene(menu);
	        
	        _primaryStage.setScene(_scene);
	        _primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return _primaryStage;
    }
    public Scene getScene(){
    	return _scene;
    }
	public static void main(String[] args) {
		launch(args);
	}
}
