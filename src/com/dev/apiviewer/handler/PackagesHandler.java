package com.dev.apiviewer.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static com.dev.apiviewer.handler.Handler.reducedArgs;
import static com.dev.apiviewer.handler.Handler.simpleToQualifiedNamesMap;


public class PackagesHandler {
		
	
	/*
	 * Find either any parent package or any subpackage hierarchies for the package whose
	 * name is passed in the String parameter (find prior for boolean parameter true and 
	 * latter for false).  Return null if none exist 
	 */
	public static String[] findPackageHierarchies(String p, boolean parentHeirarchies) {
		String concat = "";
	    Collection<String> mapValues = simpleToQualifiedNamesMap.values();
	    List<String> parentPackagesList = new ArrayList<String>();
	    String[] packageCandidates;    
	    
	    for (String s : mapValues) {
	    	if (s.contains(p)) {
	    		packageCandidates = s.split("\\.");
	    		//Check that p matches package name and not just a substring of package name
	    		if (Arrays.asList(packageCandidates).contains(p)) {
	    			List<String> superPackages = new ArrayList<String>();
	    			String[] packages = new String[packageCandidates.length -1];
	    			for (int i = 0; i < packageCandidates.length -1; i++) {
	    				packages[i] = packageCandidates[i];
	    			}
	    			int indexNumber = Arrays.asList(packages).indexOf(p); 
	    			// Determine whether to derive parent package hierarchies or subpackage ones
	    			if (parentHeirarchies) {
	    				for (int i = 0; i < indexNumber; i++) {
	    					superPackages.add(packages[i]);
	    				}
	    			} 
	    			else {
	    				for (int i = indexNumber + 1; i < packages.length; i++) {
	    					superPackages.add(packages[i]);
	    				}
	    			}
	    			
	    			concat = "";
	    			if (superPackages.size() > 0) {            
	    				for (String str : superPackages) {
	    					concat = concat + str + ".";
	    				}
	    				concat = concat.substring(0, concat.length()-1);// Remove final dot
	    			}
	    			
	    			if (parentPackagesList.size() > 0) {
	    				// Add parent package to list if not already added
	    				if (!parentPackagesList.contains(concat) && !concat.equals("")) {
	    					parentPackagesList.add(concat);
	    				}
	    			} 
	    			else { //First parent package found for user-input package, so add to list unconditionally
	    				parentPackagesList.add(concat);
	    			}
	    		}
	    	}      
	    }
	    
	    if (parentPackagesList.size() == 0) 
	    	return null;
	    String[] parentPackages = new String[parentPackagesList.size()];
	    return parentPackagesList.toArray(parentPackages);
	  }




	  /*
	   *  Find qualified names of reference types (classes, interfaces, annotations) found within
	   *  the passed package name. Return null if none exist i.e. only subpackages found
	   */
	  public static String[] findTypesInPackage(String p) {
		  String concat = "";
		  Collection<String> mapValues = simpleToQualifiedNamesMap.values();
		  List<String> typeList = new ArrayList<String>();
		  String[] packageCandidates;    
		  for (String s : mapValues) {      
			  if (s.contains(p)) {
				  packageCandidates = s.split("\\.");
	        
				  //Check that p matches package name and not just a substring of package name
				  if (Arrays.asList(packageCandidates).contains(p)) {
					  String[] packages = new String[packageCandidates.length -1];
					  	for (int i = 0; i < packageCandidates.length -1; i++) {
					  		packages[i] = packageCandidates[i];
					  	}
					  	int indexNumber = Arrays.asList(packages).indexOf(p);
					  	
					  	// if package contains reference type(s) it will be second to last in qualified 
					  	// name of latter
					  	if (indexNumber == packages.length -1) {
					  		//noSubPackage = true;
					  		concat = "";
					  		for (String str : packageCandidates) {
					  			concat = concat + str + ".";
					  		}
					  		// Remove final dot
					  		if (concat.length() > 1) 
					  			concat = concat.substring(0, concat.length()-1);
					  		typeList.add(concat);
					  	} 
				  }
			  }      
		  }
		  if (typeList.size() == 0) 
			  return null;
		  String[] types = new String[typeList.size()];
		  return typeList.toArray(types);
	  }



	  static void printPackagesData() {
		  System.out.println();
		  String[] parentPackages = findPackageHierarchies(reducedArgs[0], true);
		  if (!(parentPackages == null)) {
			  System.out.println("Package(s) named \"" + reducedArgs[0] + 
					  	"\" has/have the following parent package(s):" );
			  for (String s : parentPackages) {
				  System.out.println(s);
			  }
		  }
		  else System.out.println("No parent package found for package named \"" 
	                                  + reducedArgs[0] + "\"");
		  System.out.println();
		  
		  String[] subPackages = findPackageHierarchies(reducedArgs[0], false);
	    
		  if (!(subPackages == null)) {
			  System.out.println("Package(s) named \"" + reducedArgs[0] + 
					  		"\" has/have the following subpackage(s):" );
			  for (String s : subPackages) {
				  System.out.println(s);
			  }
		  } else System.out.println("No subpackage found for package named \"" 
	                                 				+ reducedArgs[0] + "\"");
		  System.out.println();
		  
		  String[] types = findTypesInPackage(reducedArgs[0]);
		  
		  if (!(types == null)) {
			  System.out.println("Package(s) named \"" + reducedArgs[0] + 
					  "\" directly contain(s) the following reference types:" );
			  for (String s : types) {
				  System.out.println(s);
			  }
		  }
		  else System.out.println("No reference types found for package named \"" 
	                                  + reducedArgs[0] + "\"");
		  System.out.println();
	  }
	 
}
