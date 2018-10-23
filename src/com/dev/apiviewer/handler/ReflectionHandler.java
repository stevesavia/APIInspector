package com.dev.apiviewer.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import static com.dev.apiviewer.handler.Handler.reducedArgs;

public class ReflectionHandler {
	
	private final static Pattern stripQualifiersPattern = Pattern.compile("\\w+\\.");
	
	
	
	/*	
	   * Enter reference type name and use reflection to print signatures of all
	   * constructors and methods, or, if second argument is a selector string (which 
	   * may be only one character), print only those methods containing this string
	   * Signatures comprise only simple unqualified names of the types	
	   */
	  @SuppressWarnings("rawtypes")
	  static void processType(String typeName) {
		  
		  try {
			  //Strip to raw type if generic type found
			  if (typeName.contains("<")) {
				  typeName = typeName.substring(0, typeName.indexOf("<"));
			  }
			  Class<?> refName = Class.forName(typeName);
			  Constructor[] constructors = refName.getConstructors();
			  Method[] methods = refName.getMethods();
			  String simpleName = "", methodString = "";
			  String[] sa;
			  StringBuffer sb = new StringBuffer();
			  
			  if (reducedArgs.length == 1) {
				  sb.append("\n");
				  sb.append("Constructors for ");
				  sb.append(refName.toString());
				  sb.append(":");
				  sb.append("\n");
				  
				  for (Constructor c : constructors) {
					  simpleName = stripQualifiersPattern.matcher(c.toString()).replaceAll("");
					  sb.append("  ");
					  sb.append(simpleName);
					  sb.append("\n");
				  }
				  sb.append("\n");
				  sb.append("Methods for ");
				  sb.append(refName.toString());
				  sb.append(":");
				  sb.append("\n");
				  
				  for (Method m : methods) {
					  simpleName = stripQualifiersPattern.matcher(m.toString()).replaceAll("");
					  sb.append("  ");
					  sb.append(simpleName);
					  sb.append("\n");
				  }
			  }
			  
			  // Second argument is word for method lookup
			  if (reducedArgs.length == 2) {
				  sb.append("\n");
				  sb.append("Methods for ");
				  sb.append(refName.toString());
				  sb.append(" containing letter sequence ");
				  sb.append(reducedArgs[1]);
				  sb.append(":");
				  sb.append("\n");
				  
				  for (Method m : methods) {
					  //Qualified method name and signature contain query string
					  if (m.toString().indexOf(reducedArgs[1]) != -1) {
						  sa = m.toString().split("\\s+"); //Separate signature words            
						  for (String s : sa) {  //Find method name, will contain "(" 
							  if ( s.contains("(") ) { 
								  methodString = s.substring(0, s.indexOf("(")); 
							  }           
						  }
						  sa = methodString.split("\\.");
						  methodString = sa[sa.length - 1]; //Reduced to simple method name and parameters
						  
						  //Only test match for query string against simple method name
						  if (methodString.indexOf(reducedArgs[1]) != -1) { 
							  simpleName = stripQualifiersPattern.matcher(m.toString()).replaceAll("");
							  sb.append("  ");
							  sb.append(simpleName);
							  sb.append("\n");
						  }
					  }
				  }
			  }
			  System.out.println(sb);
		  } 
		  catch (ClassNotFoundException e) {
			  System.out.println("Class undetected: " + e);
			  System.out.println("typeName entered: " + typeName);
		  }
	  }
	
	  
	  
	  
	  /*
	   *  As for method processType, but print signatures using fully qualified names of types
	   *  and with fully qualified method name i.e indicating the type that method is inherited
	   *  from, if it is not declared within the type under inspection.
	   *  Called if user enters -q option
	   */
	  @SuppressWarnings("rawtypes")
	  static void processTypeWithQualifiers(String typeName) {
		  try {
			  //Strip to raw type if generic type found
			  if (typeName.contains("<")) {
				  typeName = typeName.substring(0, typeName.indexOf("<"));
			  }
			  
			  Class<?> refName = Class.forName(typeName);
			  Constructor[] constructors = refName.getConstructors();
			  Method[] methods = refName.getMethods();
			  String methodString = "";
			  String[] sa;
			  StringBuffer sb = new StringBuffer();
			  
			  if (reducedArgs.length == 1) {
				  sb.append("\n");
				  sb.append("Constructors for ");
				  sb.append(refName.toString());
				  sb.append(":");
				  sb.append("\n");
				  for (Constructor c : constructors) {
					  sb.append("  ");
					  sb.append(c.toString());
					  sb.append("\n");
				  }
				  sb.append("\n");
				  sb.append("Methods for ");
				  sb.append(refName.toString());
				  sb.append(":");
				  sb.append("\n");
				  for (Method m : methods) {
					  sb.append("  ");
					  sb.append(m.toString());
					  sb.append("\n");
				  }
			  }
			  
			  // Second arguement is word for method lookup
			  if (reducedArgs.length == 2) {
				  sb.append("\n");
				  sb.append("Methods for ");
				  sb.append(refName.toString());
				  sb.append(" containing letter sequence ");
				  sb.append(reducedArgs[1]);
				  sb.append(":");
				  sb.append("\n");
				  
				  
				  for (Method m : methods) {
					  //Qualified method name and signature contain query string
					  if (m.toString().indexOf(reducedArgs[1]) != -1) {
						  sa = m.toString().split("\\s+"); //Separate signature words            
						  for (String s : sa) {  //Find method name, will contain "(" 
							  if ( s.contains("(") ) { 
								  methodString = s.substring(0, s.indexOf("(")); 
							  }           
						  }
						  sa = methodString.split("\\.");
						  methodString = sa[sa.length - 1]; //Reduced to simple method name and parameters
						  //Only test match for query string against simple method name
						  if (methodString.indexOf(reducedArgs[1]) != -1) { 
							  sb.append("  ");
							  sb.append(m.toString());
							  sb.append("\n");
						  }
					  }
				  }
			  }
			  System.out.println(sb);
		  } 
		  catch (ClassNotFoundException e) {
			  System.out.println("# Class undetected: " + e);
			  System.out.println("# typeName entered: " + typeName);
		  }
	  }
	  
}
