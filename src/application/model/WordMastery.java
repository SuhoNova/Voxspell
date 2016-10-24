package application.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class stores data of a word that the user mastered/failed
 * In the format that can be used in treeview 
 * @author syu680, Alex (Suho) Yu
 *
 */
public class WordMastery {
	private SimpleStringProperty words;
	private SimpleStringProperty mastery;
	
	public WordMastery(String w, String m){
		words = new SimpleStringProperty(w);
		mastery = new SimpleStringProperty(m);
	}
	
	public String getWords(){
		return words.get();
	}
	public void setWords(String w){
		words.set(w);
	}
	public String getMastery(){
		return mastery.get();
	}
	public void setMastery(String m){
		mastery.set(m);
	}
}
