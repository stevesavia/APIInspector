package com.dev.apiviewer.util;

import java.util.ArrayList;
import java.util.List;

public class ExtractJarsData extends ShellCommandSequence {
	
	@Override
	public void setExecCommands(List<String> jars) {
		if (execCommands == null) 
			execCommands = new ArrayList<>();
		else
			execCommands.clear();
		
		for (String jar : jars) {
			StringBuffer sb = new StringBuffer();
			sb.append("jar tvf ");
			if (jar.contains("/")) {
				String[] sa = jar.split("/");
				if (sa.length > 0)
					sb.append(sa[sa.length - 1]);
			}
			else
				sb.append(jar);
			
			sb.append(" >> jar_contents.dat");
			execCommands.add(sb.toString());
		}
		//System.out.println("\n@@@ execCommands: " + execCommands);
	}
	
	

	public boolean execute() {
		if (execCommands != null) {
			ExecCommand command = new ShellCommand("rm jar_contents.dat");
			this.setPauseBetweenCommandsMillis(80);
			command.execute();
			super.execute();
			return true;
		}
		else {
			System.out.println("execCommands not set in ExtractJarsData class");
			return false;
		}
	}
	
}
