package com.dev.apiviewer.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static com.dev.apiviewer.handler.Handler.reducedArgs;
import static com.dev.apiviewer.handler.Handler.simpleToQualifiedNamesMap;


public class PackagesHandler {
		
	private static Collection<String> packageHierarchiesList = new ArrayList<>();
	
	
	static int numberOfSubstrings(String target, String substring) {
		String temp = target.replace(substring, "");
		return (target.length() - temp.length()) / substring.length();
	}
	
	
	// In markInnerClasses(), iterate values, and check if v contains any other values (except if value 
	// is equal) but length is longer than that which it contains - if found, this value is an inner 
	// class, so replace last instance of "." with "@@@"
	private static Collection<String> markInnerClasses(Collection<String> c) {
		Collection<String> markedList = new ArrayList<>();
		
		for (String value : packageHierarchiesList) {
			boolean foundInnerClass = false;
			StringBuffer sb = null;
			
			for (String otherValue : packageHierarchiesList) {
				if (!value.equals(otherValue)) { //object equality
					
					if (
							value.contains(otherValue) && 
							value.length() > otherValue.length() &&
							numberOfSubstrings(value, ".") > numberOfSubstrings(otherValue, ".")
						) {
						System.out.println("\n### INNER CLASS " + value);
						
						sb = new StringBuffer(value);
				        sb.replace(value.lastIndexOf("."), value.lastIndexOf(".") + 1, "@@@");
				        foundInnerClass = true;
				        break;
					}
				}	
			}
			markedList.add(foundInnerClass ? sb.toString() : value);
		}
		
		System.out.println("\n\n###markedList  " + markedList);
		System.out.println("###markedList SIZE  " + markedList.size());
		return markedList;
	}
	
	
	
	
	/*
	 * Find either any parent package or any subpackage hierarchies for the package whose
	 * name is passed in the String parameter (find prior for boolean parameter true and 
	 * latter for false).  Return null if none exist 
	 */
	private static String[] findPackageHierarchies(String p, boolean parentHeirarchies) {
		String concat = "";
	    List<String> parentPackagesList = new ArrayList<>();
	    String[] packageCandidates;
	    
	    if (packageHierarchiesList.isEmpty()) {
	    	Collection<String> mapValues = simpleToQualifiedNamesMap.values();
	    	
	    	System.out.println();
	    	for (String v : mapValues) {
	    		if (v.contains("." + p + ".")) {
	    			packageHierarchiesList.add(v);
	    			System.out.println("### ADD TO packageHierarchiesMap " + v);
	    		}
	    	}
	    	packageHierarchiesList = markInnerClasses(packageHierarchiesList);
	    }
	    
	    //TEST
	    System.out.println("### packageHierarchiesList SIZE :" + packageHierarchiesList.size());
	    
	    
	    for (String s : packageHierarchiesList) {
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
	    
	    if (parentPackagesList.size() == 0) 
	    	return null;
	    String[] parentPackages = new String[parentPackagesList.size()];
	    return parentPackagesList.toArray(parentPackages);
	  }




	  /*
	   *  Find qualified names of reference types (classes, interfaces, annotations) found within
	   *  the passed package name. Return null if none exist i.e. only subpackages found
	   */
	 private static String[] findTypesInPackage(String p) {
		  String concat = "";
		  List<String> typeList = new ArrayList<String>();
		  String[] packageCandidates;    
		  
		  for (String s : packageHierarchiesList) {
			  packageCandidates = s.split("\\.");
			  
			  //Check that p matches package name and not just a substring of package name
			  if (Arrays.asList(packageCandidates).contains(p)) {
				  String[] packages = new String[packageCandidates.length -1];
				  for (int i = 0; i < packageCandidates.length -1; i++) {
					 packages[i] = packageCandidates[i];
				  }
				  
				  int indexNumber = Arrays.asList(packages).indexOf(p);
				  	
				  // if package contains reference type(s) it will be second to last in qualified name of latter
				  if (indexNumber == packages.length -1) {
					  //noSubPackage = true;
					  concat = "";
					  for (String str : packageCandidates) {
						  concat = concat + str + ".";
					  }
					  // Remove final dot
					  if (concat.length() > 1) 
						  concat = concat.substring(0, concat.length()-1);
					  
					  typeList.add(concat.replace("@@@", "."));
				  } 
			  }      
		  }
		  if (typeList.size() == 0) 
			  return null;
		  String[] types = new String[typeList.size()];
		  return typeList.toArray(types);
	  }

	  
	  
	  static void printPackagesData() {
		  StringBuffer sb = new StringBuffer();
		  sb.append("\n");
		  String[] parentPackages = findPackageHierarchies(reducedArgs[0], true);
		  if (!(parentPackages == null)) {
			  sb.append("Package named \"");
			  sb.append(reducedArgs[0]);
			  sb.append("\" has the following parent package(s):");
			  sb.append("\n");
			  for (String s : parentPackages) {
				  sb.append(s);
				  sb.append("\n");
			  }
		  }
		  else {
			  sb.append("No parent package found for package named \"");
			  sb.append(reducedArgs[0]);
			  sb.append("\n");
		  }
			  sb.append("\n");
		  
		  String[] subPackages = findPackageHierarchies(reducedArgs[0], false);
	    
		  if (!(subPackages == null)) {
			  sb.append("Package named \"");
			  sb.append(reducedArgs[0]);
			  sb.append("\" has the following subpackage(s):");
			  sb.append("\n");
			  for (String s : subPackages) {
				  sb.append(s);
				  sb.append("\n");
			  }
		  } 
		  else {
			  sb.append("No subpackage found for package named \"");
			  sb.append(reducedArgs[0]);
			  sb.append("\"");
		  }
		  sb.append("\n");
		  String[] types = findTypesInPackage(reducedArgs[0]);
		  
		  if (!(types == null)) {
			  sb.append("Package named \"" );
			  sb.append(reducedArgs[0]);
			  sb.append("\" directly contain(s) the following reference types:");
			  sb.append("\n");
			  for (String s : types) {
				  sb.append(s);
				  sb.append("\n");
			  }
		  }
		  else {
			  sb.append("No reference types found for package named \"");
			  sb.append(reducedArgs[0]);
			  sb.append("\n");
		  }
		  sb.append("\n");
		  System.out.println(sb);
	  }
}
