package cat.jiu.core.util;

import com.google.gson.*;
import net.minecraft.nbt.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public class JsonUtils {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final Gson GSON =        new GsonBuilder().serializeNulls()                    .create();
    public static final Gson GSON_FORMAT = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    public static final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();

    public static JsonElement get(JsonObject data, String k, JsonElement failBack) {
        if (data.has(k)) {
            return data.get(k);
        }
        return failBack;
    }
    public static byte get(JsonObject data, String k, byte failBack) {
        if (data.has(k)) {
            return data.get(k).getAsByte();
        }
        return failBack;
    }
    public static short get(JsonObject data, String k, short failBack) {
        if (data.has(k)) {
            return data.get(k).getAsShort();
        }
        return failBack;
    }
    public static int get(JsonObject data, String k, int failBack) {
        if (data.has(k)) {
            return data.get(k).getAsInt();
        }
        return failBack;
    }
    public static long get(JsonObject data, String k, long failBack) {
        if (data.has(k)) {
            return data.get(k).getAsLong();
        }
        return failBack;
    }
    public static String get(JsonObject data, String k, String failBack) {
        if (data.has(k)) {
            return data.get(k).getAsString();
        }
        return failBack;
    }
    public static boolean get(JsonObject data, String k, boolean failBack) {
        if (data.has(k)) {
            return data.get(k).getAsBoolean();
        }
        return failBack;
    }
    public static JsonObject get(JsonObject data, String k, JsonObject failBack) {
        if (data.has(k)) {
            return data.getAsJsonObject(k);
        }
        return failBack;
    }
    public static JsonArray get(JsonObject data, String k, JsonArray failBack) {
        if (data.has(k)) {
            return data.getAsJsonArray(k);
        }
        return failBack;
    }
    public static float get(JsonObject data, String k, float failBack) {
        if (data.has(k)) {
            return data.get(k).getAsFloat();
        }
        return failBack;
    }
    public static double get(JsonObject data, String k, double failBack) {
        if (data.has(k)) {
            return data.get(k).getAsDouble();
        }
        return failBack;
    }
    public static Number get(JsonObject data, String k, Number failBack) {
        if (data.has(k)) {
            return data.get(k).getAsNumber();
        }
        return failBack;
    }
    public static BigInteger get(JsonObject data, String k, BigInteger failBack) {
        if (data.has(k)) {
            return data.get(k).getAsBigInteger();
        }
        return failBack;
    }
    public static BigDecimal get(JsonObject data, String k, BigDecimal failBack) {
        if (data.has(k)) {
            return data.get(k).getAsBigDecimal();
        }
        return failBack;
    }

    public static <T extends JsonElement> T parse(File file) {
        return parse(file, DEFAULT_CHARSET);
    }
    public static <T extends JsonElement> T parse(File file, String charset) {
        try {
            return parseThrow(file, charset);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends JsonElement> T parse(String path) {
        return parse(path, DEFAULT_CHARSET);
    }
    public static <T extends JsonElement> T parse(String path, String charset) {
        try {
            return parseThrow(path, charset);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends JsonElement> T parse(InputStream path) {
        return parse(path, DEFAULT_CHARSET);
    }
    public static <T extends JsonElement> T parse(InputStream path, String charset) {
        try {
            return parseThrow(path, charset);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends JsonElement> T parseThrow(File file) throws Exception {
        return parseThrow(file, DEFAULT_CHARSET);
    }
    public static <T extends JsonElement> T parseThrow(File file, String charset) throws Exception {
        return (T) JsonParser.parseReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static <T extends JsonElement> T parseThrow(String path) throws Exception {
        return parseThrow(path, DEFAULT_CHARSET);
    }
    public static <T extends JsonElement> T parseThrow(String path, String charset) throws Exception {
        return (T) JsonParser.parseReader(new InputStreamReader(new FileInputStream(path), charset));
    }

    public static <T extends JsonElement> T parseThrow(InputStream path) throws Exception {
        return parseThrow(path, DEFAULT_CHARSET);
    }
    public static <T extends JsonElement> T parseThrow(InputStream path, String charset) throws Exception {
        return (T) JsonParser.parseReader(new InputStreamReader(path, charset));
    }

    public static boolean toJsonFile(String path, Object src, boolean format) {
        return toJsonFile(path, src, format, DEFAULT_CHARSET);
    }
    public static boolean toJsonFile(String path, Object src, boolean format, String charset) {
        return toJsonFile(new File(path), src, format, charset);
    }
    public static boolean toJsonFile(File file, Object src, boolean format) {
        return toJsonFile(file, src, format, DEFAULT_CHARSET);
    }
    public static boolean toJsonFile(File file, Object src, boolean format, String charset) {
        try {
            return toJsonFileThrow(file, src, format, charset);
        } catch (Exception e) {e.printStackTrace();return false;}
    }

    public static boolean toJsonFileThrow(String file, Object src, boolean format) throws Exception {
        return toJsonFileThrow(file, src, format, DEFAULT_CHARSET);
    }
    public static boolean toJsonFileThrow(String file, Object src, boolean format, String charset) throws Exception {
        return toJsonFileThrow(new File(file), src, format, charset);
    }
    public static boolean toJsonFileThrow(File file, Object src, boolean format) throws Exception {
        return toJsonFileThrow(file, src, format, DEFAULT_CHARSET);
    }
    public static boolean toJsonFileThrow(File file, Object src, boolean format, String charset) throws Exception {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (file.exists()) file.delete();

        file.createNewFile();
        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), charset);
        write.write((format ? GSON_FORMAT : GSON).toJson(src));
        write.flush();
        write.close();
        return true;
    }
}
