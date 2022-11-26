package cat.jiu.core.util.system.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownload {
	public static boolean download(String url, String fileName, String fileDir, String exName) {
		String method = "GET";
		File saveFilePath = new File(fileDir);
		
		if (!saveFilePath.exists()) {
			saveFilePath.mkdirs();
		}
		
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		if (!fileDir.endsWith("/")) {
			fileDir += "/";
		}
		File savePath = new File(fileDir + fileName + "." + exName);
		if(savePath.exists()) {
			return true;
		}
		try {
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.connect();
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			
			fileOut = new FileOutputStream(savePath);
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);
			
			byte[] buf = new byte[8192];
			int length = bis.read(buf);
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
			bos.close();
			bis.close();
			conn.disconnect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			e.fillInStackTrace();
			return false;
		}
	}
}
