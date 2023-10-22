package cat.jiu.core.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
	
	public static <T extends JsonElement> T parse(File file) throws IOException {
		if(file==null || !file.exists()) return null;
		return parse(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
	}
	public static <T extends JsonElement> T parse(File path, String charsetName) throws IOException {
		return parse(Files.newInputStream(path.toPath()), charsetName);
	}
	/**
	 * @param cs see {@link java.nio.charset.StandardCharsets}
	 */
	public static <T extends JsonElement> T parse(File path, Charset cs) throws IOException {
		return parse(Files.newInputStream(path.toPath()), cs);
	}
	
	public static <T extends JsonElement> T parse(InputStream path) {
		return (T) parse(new InputStreamReader(path, StandardCharsets.UTF_8));
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
