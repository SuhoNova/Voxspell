package application.model;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.SwingWorker;

/**
 * This class deals with processing of video using ffmpeg
 * Currently only have the option to flip the video using the an option called vflip
 * 
 */

import application.view.ControllerVideoReward;

public class VideoRewardFFMPEG extends SwingWorker<Void,Void> {
	private ControllerVideoReward _vr;
	private String _pathVideo;
	private String _newFile;
	//private String _newSpaceFile;
	public VideoRewardFFMPEG(ControllerVideoReward vr, String path, String vidPath){
		_vr = vr;
		_pathVideo = vidPath;
		//String temp = vidPath;
		//_pathVideo = temp.replace(" ", "\\ ");
		_newFile = path+"/assets/output.mp4";
		//temp = path.replace(" ", "\\ ");
		//_newSpaceFile = temp+"/assets/output.mp4";
	}
	@Override
	protected Void doInBackground() throws Exception {
		String command = "";
		// if file exist delete it first, before converting video
		if(new File(_newFile).exists()){
			command = "rm "+_newFile+";ffmpeg -i "+_pathVideo+" -vf vflip "+_newFile;
		}else{
			command = "ffmpeg -i "+_pathVideo+" -vf vflip "+_newFile;
		}
		// execute the command on linux
		Runtime rt = Runtime.getRuntime();
		Process process = rt.exec(command);
		OutputStream output = process.getOutputStream();
		output.write(("").getBytes());
		output.flush();
		output.close();
		process.waitFor();
		return null;
	}
	
	@Override
	protected void done(){
		//String temp = _newFile.replace("\\ ", " ");
		_vr.playVideo(Paths.get(_newFile));
	}

}
