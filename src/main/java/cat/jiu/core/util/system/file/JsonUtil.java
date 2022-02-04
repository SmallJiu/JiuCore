package cat.jiu.core.util.system.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
	public static class Write {
		private final String mainName;
		private final JsonObject jobj;
		private final JsonObject jarray;
		
		public Write() {
			this("main");
		}
		
		public Write(String mainName) {
			this(mainName, new JsonObject());
		}
		
		public Write(JsonObject jobj) {
			this("main", jobj);
		}
		
		public Write(String mainName, JsonObject jobj) {
			this(mainName, jobj, new JsonObject());
		}
		
		public Write(String mainName, JsonObject jobj, JsonObject jarray) {
			this.mainName = mainName;
			this.jobj = jobj;
			this.jarray = jarray;
		}
		
		public void add(String name, JsonArray jarray) {
			if(this.jarray != null) {
				this.jarray.add(name, jarray);
			}
		}
		
		public void add(String name, JsonObject jobj) {
			if(this.jarray != null) {
				this.jarray.add(name, jobj);
			}
		}
		
		public void add(String group, String name, boolean value) {
			if(this.has(group)) {
				JsonElement e = this.jarray.get(group);
				if(e instanceof JsonObject) {
					JsonObject o = (JsonObject) e;
					o.addProperty(name, value);
				}else if(e instanceof JsonArray) {
					((JsonArray) e).add(value);
				}
			}else {
				JsonObject jobj = new JsonObject();
				jobj.addProperty(name, value);
				this.add(group, jobj);
			}
		}
		
		public void add(String group, String name, Number value) {
			if(this.has(group)) {
				JsonElement e = this.jarray.get(group);
				if(e instanceof JsonObject) {
					JsonObject o = (JsonObject) e;
					o.addProperty(name, value);
				}else if(e instanceof JsonArray) {
					((JsonArray) e).add(value);
				}
			}else {
				JsonObject jobj = new JsonObject();
				jobj.addProperty(name, value);
				this.add(group, jobj);
			}
		}
		
		public void add(String group, String name, String value) {
			if(this.has(group)) {
				JsonElement e = this.jarray.get(group);
				if(e instanceof JsonObject) {
					((JsonObject) e).addProperty(name, value);
				}else if(e instanceof JsonArray) {
					((JsonArray) e).add(value);
				}
			}else {
				JsonObject jobj = new JsonObject();
				jobj.addProperty(name, value);
				this.add(group, jobj);
			}
		}
		
		public void add(String group, String name, Character value) {
			if(this.has(group)) {
				JsonElement e = this.jarray.get(group);
				if(e instanceof JsonObject) {
					JsonObject o = (JsonObject) e;
					o.addProperty(name, value);
				}else if(e instanceof JsonArray) {
					((JsonArray) e).add(value);
				}
			}else {
				JsonObject jobj = new JsonObject();
				jobj.addProperty(name, value);
				this.add(group, jobj);
			}
		}
		
		public boolean toJsonFile(String path) {
			if(this.jobj != null) {
				try {
					File file = new File(path);
			        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
			            file.getParentFile().mkdirs();
			        }
			        if (file.exists()) { // 如果已存在,删除旧文件
			            file.delete();
			        }
			        
			        file.createNewFile();
			        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		            write.write(formatJson(this.toString()));
		            write.flush();
		            write.close();
		            
		            return true;
				} catch (Exception e) {return false;}
			}
			return false;
		}
		
		public static <T> boolean toJsonFile(String path, T obj) {
			String json = new GsonBuilder().create().toJson(obj);
			
			try {
				File file = new File(path);
		        if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
		            file.getParentFile().mkdirs();
		        }
		        if (file.exists()) { // 如果已存在,删除旧文件
		            file.delete();
		        }
		        
		        file.createNewFile();
		        Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
	            write.write(formatJson(json));
	            write.flush();
	            write.close();
		        return true;
			} catch (Exception e) {e.printStackTrace();return false;}
		}
		
		@Override
		public String toString() {
			if(this.jobj != null) {
				if(this.has(this.mainName)) {
					
				}else {
					this.jobj.add(this.mainName, this.jarray);
				}
				
				return this.jobj.toString();
			}
			
			return "null";
		}
		
		private boolean has(String name) {
			for(Map.Entry<String, JsonElement> entry : this.jarray.entrySet()) {
				if(entry.getKey().equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static class Read<T> {
		private final String path;
//		private T jobj;
		private final Class<T> clazz;
		
		public Read(String path, Class<T> classOfT) {
			this.path = path;
			this.clazz = classOfT;
//			this.jobj = new Gson().fromJson(path, classOfT);
		}
		
		public static <T> T read(String path, Class<T> clazz) {
			String result = "";
			InputStream stream = null;
	        try {
	            File file = new File(path);
	            stream = new FileInputStream(file);
	            result = IOUtils.toString(stream, StandardCharsets.UTF_8);
	        }catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            if (stream != null) {
	            	IOUtils.closeQuietly(stream);
	            }
	        }
			return new GsonBuilder().create().fromJson(result, clazz);
		}
		
		public T[] read() {
			return JsonUtil.read(this.path, this.clazz);
		}
	}
	
	public static<T> T[] read(String path, Class<T> clazz) {
		return null;
	}
	
	private static String formatJson(String json) {
        StringBuffer result = new StringBuffer();
        int length = json.length();
        int number = 0;
        char key = 0;
        
        for (int i = 0; i < length; i++) {
            // 1、获取当前字符。
            key = json.charAt(i);
            
            // 2、如果当前字符是前方括号/前花括号做如下处理：
            if ((key == '[') || (key == '{')) {
                // （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append(" ");
                }
                
                //增加str
                result.append(key);
                
                // （3）前方括号/前花括号，的后面必须换行。打印：换行。
                result.append('\n');
                
                // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));
                
                continue;
            }
            
            // 3、如果当前字符是后方括号、后花括号做如下处理：
            if ((key == ']') || (key == '}')) {
                // （1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');
                
                // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));
                
                //增加str
                result.append(key);
                continue;
            }
            
            //如果是","，则换行并缩进，并且不改变缩进次数
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }
            result.append(key);
        }
        
        result.append('\n');
        return result.toString();
    }
	
	private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append("	");
        }
        return result.toString();
    }
}
