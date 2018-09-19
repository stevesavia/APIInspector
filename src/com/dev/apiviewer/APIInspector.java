package com.dev.apiviewer;

import static com.dev.apiviewer.handler.Handler.*;

public class APIInspector {
	
	  public static void main(String[] args) {
		  loadSimpleToQualifiedNamesMap("simpleToQualifiedNames.dat");
		  
		  if (getJarsData())
			  loadSimpleToQualifiedNamesMap("JarsSimpleToQualifiedNames.dat");
		  
		  process(args);
	  }  
}
