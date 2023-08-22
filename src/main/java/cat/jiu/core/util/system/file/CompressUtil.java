package cat.jiu.core.util.system.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CompressUtil {
	public static void decompression(File targetZip, File targetDir) throws Exception {
		try(FileInputStream fis = new FileInputStream(targetZip);
			ZipInputStream zis = new ZipInputStream(fis)) {
			
			ZipEntry entry = null;
			byte[] buf = new byte[1024];
			
			while((entry = zis.getNextEntry()) != null) {
				if(entry.isDirectory()) continue;
				
				File file = new File(targetDir, entry.getName());
				if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
				
				try(FileOutputStream fos = new FileOutputStream(file)) {
					int count = -1;
					while((count = zis.read(buf)) != -1) {
						fos.write(buf, 0, count);
						fos.flush();
					}
					zis.closeEntry();
				}
			}
		}
	}
	
	public static void compress(File target, File... sources) throws Exception {
		compress(target, 7, sources);
	}
	
	public static void compress(File target, int compressLevel, File... sources) throws Exception {
		try(FileOutputStream fos = new FileOutputStream(target);
			ZipOutputStream zos = new ZipOutputStream(fos)) {
			zos.setLevel(compressLevel);
			for(File source : sources) {
				if(source == null) continue;
				
				if(source.isDirectory()) {
					String s = source.getName() + File.separator;
					for(File file : source.listFiles()) {
						addEntry(zos, s, file);
					}
				}else {
					addEntry(zos, "", source);
				}
			}
		}
	}
	
	static void addEntry(ZipOutputStream zos, String base, File source) throws Exception {
		if(source.isDirectory()) {
			for(File file : source.listFiles()) {
				addEntry(zos, base + source.getName() + File.separator, file);
			}
		}else {
			try(FileInputStream fis = new FileInputStream(source)) {
				
				byte[] buf = new byte[1024];
				int count = -1;
				zos.putNextEntry(new ZipEntry(base + source.getName()));
				while((count = fis.read(buf)) != -1) {
					zos.write(buf, 0, count);
					zos.flush();
				}
				zos.closeEntry();
			}
		}
	}
}
