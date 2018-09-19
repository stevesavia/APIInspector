package com.dev.apiviewer.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;


public class Handler {
		static HashMap<String, String> simpleToQualifiedNamesMap = new HashMap<>();
		static String[] reducedArgs;
	
	
		public static void loadSimpleToQualifiedNamesMap(String fileName) {
		    try (Stream<String> input = Files.lines(Paths.get(fileName))) {
		    	input.forEach(s -> addToMap(s));
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		  
		  
		  private static void addToMap(String line) {
			  String[] sa = new String[2];
			  sa = line.split("\\s+");
			  simpleToQualifiedNamesMap.put(sa[0], sa[1]); 
		  }
		  
		  
		  public static boolean getJarsData() {
			  JarsDataHandler jarsDataHandler = new JarsDataHandler();
			  return jarsDataHandler.execute();
		  }

		  
		  public static void process(String[] args) {
			  ArgumentsHandler.process(args);
		  }
}
