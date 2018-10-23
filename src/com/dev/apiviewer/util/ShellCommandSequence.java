package com.dev.apiviewer.util;

import java.io.IOException;
import java.util.List;

public class ShellCommandSequence implements ExecCommand {
	
	List<String> execCommands;
	private int pauseBetweenCommandsMillis = 0;
	
	
	public void setExecCommands(List<String> execCommands) {
		this.execCommands = execCommands; 
	}
	
	
	void setPauseBetweenCommandsMillis(int pause) {
		pauseBetweenCommandsMillis = pause;
	}
	
	
	public boolean execute() {
		for (String execCommand : execCommands) {
			String[] cmd = {"/bin/sh", "-c", execCommand};
			
			//System.out.println("ShellCommandSequence executing: " + execCommand);
			
			try {
				Runtime.getRuntime().exec(cmd);
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error in execCommand: " + execCommand);
				return false;
			}
			
			if (pauseBetweenCommandsMillis > 0)
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
