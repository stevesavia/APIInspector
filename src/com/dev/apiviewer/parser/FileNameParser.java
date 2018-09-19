package com.dev.apiviewer.parser;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class FileNameParser implements ParserToList {
	
	private final String path;
	private final String frag;
	private final boolean isEnd;
	
	
	FileNameParser(String path, String frag, boolean isEnd) {
		this.path = path;
		this.frag = frag;
		this.isEnd = isEnd;
	}
	
	
	public Optional<List<String>> parse() {
		return parse(isEnd, path, frag);
	}
	
	
	private Optional<List<String>> parse(boolean isEnd, String... args) {
		DirectoryStream.Filter<Path> filter = isEnd 
				?
				path -> path.toFile().toString().endsWith(frag)
				:
				path -> path.toString().contains(frag);

			Iterator<Path> iterator = null;
			try {
				iterator = Files.newDirectoryStream(
						Paths.get(path),
						filter
						)
						.iterator();
			} catch (IOException e) {
				e.printStackTrace();
				return Optional.empty();
			}
			
			List<Path> pathList = new ArrayList<>();
			iterator.forEachRemaining(pathList::add);
			
			Optional<List<String>> optList =
					Optional.ofNullable(
							pathList.stream()
							.map(m -> m.toString())
							.collect(Collectors.toList())
							);
			return optList;
	}
}
