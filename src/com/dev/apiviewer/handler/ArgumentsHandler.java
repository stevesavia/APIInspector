package com.dev.apiviewer.handler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;
import static com.dev.apiviewer.handler.Handler.reducedArgs;
import static com.dev.apiviewer.handler.Handler.simpleToQualifiedNamesMap;
import static com.dev.apiviewer.handler.ReflectionHandler.processType;
import static com.dev.apiviewer.handler.ReflectionHandler.processTypeWithQualifiers;
import static com.dev.apiviewer.handler.PackagesHandler.printPackagesData;


public class ArgumentsHandler {
		
	  // Option types that can be entered
	  private static enum OptionTypes {q, l, p};
	  
	  // Combinations of option types that may be entered (e.g. qnl means q without l)
	  private static enum OptCombination {none, ql, qnl, lnq, p};
	  
	  private final static ArrayList<String> options = new ArrayList<>();//Holds user-entered options 
	  
	  
	  static void process(String[] args) {
		  if (findOptions(args)) {
			  processOptions(args);
		  } else processArgs(args, false);
	  }
	  
	  

	  // Separate user-entered options from other arguments, placing latter in the 
	  // class variable 'reducedArgs'. Returns true if option(s) found
	  private static boolean findOptions(String[] args) {
		  if (args.length == 0) {
			  System.out.println("Enter at least one command-line argument");
			  System.exit(0);
		  }
		  
		  reducedArgs  = processArgsOptions(args);
		  if (options.size() != 0) {
			  return true;
		  }
		  return false;
	  }



	  // process the user-entered argument(s): called in main. 
	  private static void processArgs(String[] args, boolean qualify) {
		  String name = "";   
		  /*
		   * First command line argument, minus any options, is the name of class under
		   * inspection (or name of other reference type (interface, array, enum, annotation)
		   */
		  if (reducedArgs[0].contains(".")) {
			  if (qualify) {
	    	  //private final static ArrayList<String> options = new ArrayList<>();//Holds user-entered options 
	    	  processTypeWithQualifiers(reducedArgs[0]);
			  } else 		
				  processType(reducedArgs[0]);
		  }
		  
		  /* 
		   * If reducedArgs[0] does not contain ".", then user has provided simple type name,
		   * so look up qualified name from loading mapping list;
		   */
		  if ( !reducedArgs[0].contains(".") ) {      
			  name = simpleToQualifiedNamesMap.get(reducedArgs[0]);
			  if (name != null) {
				  if (qualify) {
					  processTypeWithQualifiers(name);
				  } else 
					  processType(name);
	      }  
	      
	      /*
	       *  Above assignment will handle nearly all cases, but check for simple name being
	       *  duplicated in the API: If so, then map key starts with ";d;", where d gives a
	       *  distinguishing ordinal. The key will not be found, so detect null assignment
	       *  and find all keys that contain the input query string.
	       */
	      if (name == null) {
	    	  ArrayList<String> duplicateList = new ArrayList<String>();
	    	  Set<String> mapKeys = simpleToQualifiedNamesMap.keySet();
	    	  int ordinalCount = 0;
	    	  for (String s : mapKeys) {
	    		  // In practice all ordinals will be single digit
	    		  if ( s.contains(";") && s.substring(2, s.length()).equals(reducedArgs[0]) ) {
	    			  ordinalCount++;
	    			  duplicateList.add(simpleToQualifiedNamesMap.get(s));
	    		  }       
	    	  }
	    	  // type name not found at all
	    	  if (ordinalCount == 0)
	    		  System.exit(0);
	        
	    	  //Duplicate name found, so print output for each of them
	    	  for (String s : duplicateList) {          
	    		  if (qualify) {
	    			  processTypeWithQualifiers(s);
	    		  } else {
	    			  processType(s);
	    		  }
	    	  }
	    	  System.out.println();
	    	  System.out.println(ordinalCount +" reference types named " + reducedArgs[0] +" found:");
	    	  
	    	  for (String s : duplicateList) {
	    		  System.out.println(s);
	    	  }
	    	  System.out.println();
	    	  System.out.println("Their members have been detailed above");
	      	}
		  }
	  }
	  
	  
	  
	  /* The order of option entry to command line is unimportant. For some option, e.g. -p, 
	   * all other options are ignored, otherwise combinations are effected e.g. ql. 
	   * In OptCombination, 'n' negates the following symbol e.g.'lnq' means -l alone
	   */
	  private static void processOptions(String[] args) {
		  
		  if (options.contains("l") && reducedArgs.length == 0) {
			  System.out.println("Must enter a select string before or following -l");
			  System.exit(0);
		  }
		  
		  EnumSet<OptionTypes> optTypesEnumSet = createOptionsEnumSet();
		  OptCombination optionComboSelected = OptCombination.none;
		  
		  System.out.println("optTypesEnumSet " + optTypesEnumSet);
		  
		  if (optTypesEnumSet.contains(OptionTypes.q) && optTypesEnumSet.contains(OptionTypes.l)) 
			  optionComboSelected = OptCombination.ql;
		  if (optTypesEnumSet.contains(OptionTypes.q) && !optTypesEnumSet.contains(OptionTypes.l))
			  optionComboSelected = OptCombination.qnl;
		  if (optTypesEnumSet.contains(OptionTypes.l) && !optTypesEnumSet.contains(OptionTypes.q))
			  optionComboSelected = OptCombination.lnq;
		  if (optTypesEnumSet.contains(OptionTypes.p)) optionComboSelected = OptCombination.p;
		  
		  switch (optionComboSelected) {
	      	case ql:  printTypesStartingWith(reducedArgs[0], true);
	      			break;
	      	case qnl: processArgs(args, true);
	                break;
	      	case lnq: printTypesStartingWith(reducedArgs[0], false);
	                break;
	      	case p:   printPackagesData();
	                break;
	      	default:
		  }
	  }

	  

	  /*
	   *  Return EnumSet representing all valid user-entered options or null if no valid options
	   *  found.  This method will be expanded to process options as they are developed
	   */
	  private static EnumSet<OptionTypes> createOptionsEnumSet() {
		  EnumSet<OptionTypes> optionsEnumSet = EnumSet.noneOf(OptionTypes.class);
		  
		  if (options.contains("q")) 
			  	optionsEnumSet.add(OptionTypes.q);
		  if (options.contains("l")) 
			  	optionsEnumSet.add(OptionTypes.l);
		  if (options.contains("p")) 
			  	optionsEnumSet.add(OptionTypes.p);
		  if (optionsEnumSet.size() == 0) 
			  	return null;
		  
		  return optionsEnumSet;
	  }

	  

	  /* 
	   * If any arguments are options, starting with '-', then insert its string value 
	   * to the ArrayList class variable 'options' and return the args array minus the options.
	   * Return null if no option is present or if args has length of 1 and this is is a option
	   */
	  private static String[] processArgsOptions(String[] args) {
		  boolean foundOptions = false;
		  
		  ArrayList<Integer> optionsIndices = new ArrayList<>();
		  ArrayList<String> argsMinusOptions = new ArrayList<>();
		  int index = 0;
		  
		  for (String s : args) {
			  if ( Pattern.matches("^-\\w+", s) ) {  
				  //strip hyphen
				  options.add( s.substring( 1, s.length() ) );
				  foundOptions = true;
				  optionsIndices.add(index); //Note indices of args that are options
				  index++;
			  }
			  else 
				  argsMinusOptions.add(s);
		  }
		  
		  if (!foundOptions) return args;
		  String[] reducedArgs = new String[argsMinusOptions.size()];
		  reducedArgs = argsMinusOptions.toArray(reducedArgs);
		  return reducedArgs;
	  }

	  

	  /*
	   * Print out every reference type that begins with
	   * the letters contained within the string argument
	   */
	  private static void printTypesStartingWith(String select, boolean qualify) {
		  String compare = "";
		  Set<String> mapKeys = simpleToQualifiedNamesMap.keySet();
		  ArrayList<String> selectList = new ArrayList<String>();
		  int letterCount = select.length();
		  // If another option input is -q then print QUALIFIED type names
		  if (qualify) {
			  for (String s : mapKeys) {
				  // Check if key is a duplicate i.e. begins with '1;', '2;' etc.
				  if (s.contains(";")) {
					  // ensure string is large enough to extract required substring
					  if (s.length() >= letterCount + 2) {
						  compare = s.substring(2, letterCount + 2);
					  } else {
						  compare = "";
					  }
				  } else {
					  if (s.length() >= letterCount) {
						  compare = s.substring(0, letterCount);
					  } else {
						  compare = "";
					  }
				  }
				  if ( compare.equals(select) ) {
					  selectList.add(simpleToQualifiedNamesMap.get(s));
				  }     
			  }
		  } 
		  // else print SIMPLE type names
		  else { 
			  for (String s : mapKeys) {
				  // Check if key is a duplicate i.e. begins with '1;', '2;' etc.
				  if (s.contains(";")) {
					  // ensure string is large enough to extract required substring
					  if (s.length() >= letterCount + 2) {
						  compare = s.substring(2, letterCount + 2);
					  } else {
						  compare = "";
					  }
				  } else {
					  if (s.length() >= letterCount) {
						  compare = s.substring(0, letterCount);
					  } else {
						  compare = "";
					  }
				  }
				  if ( compare.equals(select) ) {
					  selectList.add(s);
				  }     
			  }
		  }
		  for (String s : selectList) {
			  // remove from string any symbols for duplicates i.e. '1;', '2;' etc.
			  if (s.contains(";")) {
				  s = s.substring(2, s.length());
			  }
			  System.out.println(s);
		  }
	  }
	  
}
