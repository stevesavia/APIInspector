package com.dev.apiviewer.util;

import java.io.IOException;

public class ShellCommand implements ExecCommand {
	
	String execCommand;
	
	public ShellCommand(String execCommand) {
		this.execCommand = execCommand; 
	}

	public boolean execute() {
		String[] cmd = {"/bin/sh", "-c", execCommand};
		try {
			Runtime.getRuntime().exec(cmd);
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("\nError in execCommand: " + execCommand);
			return false;
		}
		return true;
	}
}
