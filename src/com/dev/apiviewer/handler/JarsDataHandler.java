package com.dev.apiviewer.handler;

import java.util.List;
import java.util.Optional;
import com.dev.apiviewer.parser.CWDJarsParser;
import com.dev.apiviewer.parser.ParserToList;
import com.dev.apiviewer.util.ExecCommand;
import com.dev.apiviewer.util.ExtractJarsData;
import com.dev.apiviewer.util.FileToFileTransformer;
import com.dev.apiviewer.util.ShellCommand;


public class JarsDataHandler implements ExecCommand {
	
	public boolean execute() {
		  List<String> jarList;
	    	
	    	ParserToList parser = new CWDJarsParser();
	    	Optional<List<String>> optJarList = parser.parse();
	    	
	    	if (optJarList.isPresent()) {
	    		jarList = optJarList.get();
	    		//System.out.println("\n@@@ jarList: " + jarList);
	    	}
	    	else {
	    		System.out.println("\nNo Jars found");
	    		return false;
	    	}
	    	
	    	ExtractJarsData extractJarsData = new ExtractJarsData();
	    	extractJarsData.setExecCommands(jarList);
	    	extractJarsData.execute();
	    	
	    	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	    	new ShellCommand("rm JarsSimpleToQualifiedNames.dat");
	    	
	    	FileToFileTransformer fileToFileTransformer = new FileToFileTransformer();
	    	fileToFileTransformer.execute(
	    			new String[] {"jar_contents.dat", "JarsSimpleToQualifiedNames.dat"});
	    	return true;
	  }
}
