package com.dev.apiviewer.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileToFileTransformer implements TextTransformer {
	
	
	public void execute(String... args) {
	
	assert args.length > 1 : "FileToFileTransformer.execute() requires two String arguments";
	
	try (
			Stream<String> input = Files.lines(Paths.get(args[0]));
		    PrintWriter output = new PrintWriter(args[1], "UTF-8")
		)
		{
		    input
		    .map(s -> transform(s))
		    .filter(s -> !s.isEmpty())
		    .forEachOrdered(output::println);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String transform(String s) {
		if (!s.contains(".class"))
			return "";
		
		String qualifiedName;
		String simpleName;
		try {
			qualifiedName = s.substring(
					s.lastIndexOf(" "),
					s.lastIndexOf(".class")
					);
			simpleName = qualifiedName.substring(
					qualifiedName.lastIndexOf("/") + 1
					);

		} catch (Throwable e) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(simpleName);
		sb.append("\t");
		sb.append(qualifiedName.replaceAll("/", "."));
		return sb.toString();
	}

}


