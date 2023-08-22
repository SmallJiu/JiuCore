package cat.jiu.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

@SuppressWarnings("unchecked")
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
	
	public static <T extends JsonElement> T parse(File file) throws FileNotFoundException {
		if(file==null || !file.exists()) return null;
		return parse(new FileInputStream(file));
	}
	
	public static <T extends JsonElement> T parse(InputStream path) {
		return (T) parse(new InputStreamReader(path));
	}
	public static <T extends JsonElement> T parse(InputStream path, String charsetName) throws UnsupportedEncodingException {
		return (T) parse(new InputStreamReader(path, charsetName));
	}
	/**
	 * @param cs see {@link java.nio.charset.StandardCharsets}
	 */
	public static <T extends JsonElement> T parse(InputStream path, Charset cs) {
		return (T) parse(new InputStreamReader(path, cs));
	}
}
