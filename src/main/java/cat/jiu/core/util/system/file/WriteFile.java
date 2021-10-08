package cat.jiu.core.util.system.file;

import java.io.*;

public class WriteFile {
	private final String modid;
	
	public WriteFile(String modid) {
		this.modid = modid;
	}
	
	public static String read(String path) {
		File file = new File(path);
		if(!file.isDirectory()) {
			file.mkdirs();
		}
		try {
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String s = null;
			
			while((s = in.readLine().toString()) != null) {
				return s;
			}
			
			in.close();
		} catch (Exception e) {
			e.fillInStackTrace();
			e.printStackTrace();
		}
		
		return read(path);
	}
	
	public static void writeString(String filePath, String utf) {
		File file = new File(filePath);
		if(!file.isDirectory()) {
			file.mkdirs();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, true));
			out.write(utf);
			out.close();
		} catch (IOException e) {
			e.fillInStackTrace();
			e.printStackTrace();
		} 
	}
	
	public void writeItemModel(String itemName, String itemTextureSrc) {
		String path = this.modid + "/models/item/items/" + itemName;
		File file = new File(path);
		if(!file.isDirectory()) {
			file.mkdirs();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
			out.write("{");
			out.write("\n\"parent\":\"item/generated\",");
			out.write("\n	\"textures\": {");
			out.write("\n		\"layer0\": \"" + this.modid +  ":" + itemTextureSrc +  "\"");
			out.write("\n	}");
			out.write("\n}");
			out.close();
		} catch (IOException e) {
			e.fillInStackTrace();
			e.printStackTrace();
		} 
	}
	
	public void writeBlockModel(String blockName, String blockTextureSrc) {
		String path = this.modid + "/models/block/" + blockName;
		File file = new File(path);
		if(!file.isDirectory()) {
			file.mkdirs();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
			out.write("{");
			out.write("\n\"parent\":\"block/cube_all\",");
			out.write("\n	\"textures\": {");
			out.write("\n		\"all\": \"" + this.modid +  ":" + blockTextureSrc +  "\"");
			out.write("\n	}");
			out.write("\n}");
			out.close();
		} catch (IOException e) {
			e.fillInStackTrace();
			e.printStackTrace();
		} 
	}
	
	public void writeItemBlockModel(String blockName, String blockTextureSrc) {
		String path = this.modid + "/models/item/blocks/" + blockName;
		File file = new File(path);
		if(!file.isDirectory()) {
			file.mkdirs();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
			out.write("{");
			out.write("\n\"parent\":\"" + this.modid +":block/" + blockTextureSrc);
			out.write("\n}");
			out.close();
		} catch (IOException e) {
			e.fillInStackTrace();
			e.printStackTrace();
		} 
	}
	
	public void writeLang(String langName, boolean blockOrItem, String name, int meta, boolean hasName) {
		String path = this.modid + "/" + "lang" + "/" + langName + ".lang";
		File file = new File(path);
		if(!file.isDirectory()) {
			file.mkdirs();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, true));
			out.write((blockOrItem ? "tile" : "item") + "." + this.modid + "." + name + "." + meta + (hasName ? ".name" : "") + "=\n");
			out.close();
		} catch (IOException e) {
			e.fillInStackTrace();
			e.printStackTrace();
		}
	}
}
