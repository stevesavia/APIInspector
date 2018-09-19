package com.dev.apiviewer.util;

public interface TextTransformer {
	
	public void execute(String... args);
	String transform(String s);
}
