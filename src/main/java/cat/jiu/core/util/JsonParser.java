package cat.jiu.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

public class JsonParser {
	public static final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
	public static JsonElement parse(String json) {
		return parser.parse(json);
	}
	public static JsonElement parse(Reader json) {
		return parser.parse(json);
	}
	public static JsonElement parse(JsonReader json) {
		return parser.parse(json);
	}
	
	public static JsonElement parse(File file) throws FileNotFoundException {
		if(file==null || !file.exists()) return null;
		return parse(new FileInputStream(file));
	}
	
	public static JsonElement parse(InputStream path) {
		return parse(new InputStreamReader(path));
	}
}
