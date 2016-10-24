package application.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.SwingWorker;

import application.view.ControllerVideoReward;

public class VideoRewardFFMPEG extends SwingWorker<Void,Void> {
	private ControllerVideoReward _vr;
	private Path _path;
	private Path _pathVideo;
	private String _filename;
	public VideoRewardFFMPEG(ControllerVideoReward vr, Path path){
		_vr = vr;
		_path = path;
		_filename = "output.mp4";
	}
	@Override
	protected Void doInBackground() throws Exception {
		String command = "";
		_pathVideo = Paths.get(System.getProperty("user.dir") + "/" + _filename);
		System.out.println(_pathVideo.toFile().exists());
		System.out.println(_pathVideo);
		System.out.println(_path);
		String filePath = _pathVideo.toString();
		if(_pathVideo.toFile().exists()){
			command = "rm "+filePath+";ffmpeg -i "+_path.toString()+" -vf vflip -strict - 2 "+filePath;
		}else{
			command = "ffmpeg -i "+_path.toString()+" -vf vflip -strict - 2 "+filePath;
		}
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
		Process process = pb.start();
		process.waitFor();
		return null;
	}
	
	@Override
	protected void done(){
		System.out.println("Is done");
		_vr.playVideo(_pathVideo);
	}

}
