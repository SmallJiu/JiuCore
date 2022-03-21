package cat.jiu.core.util.system.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownload {
	public static String downLoadFile(String url, String fileName, String fileDir, String exName) {
//		String filePathDir = "c:/";
		String method = "GET";// 以Post方式提交表单，默认get方式
//		File file = new File(fileName);// 创建不同的文件夹目录
		File saveFilePath = new File(fileDir);
		
		// 判断文件夹是否存在
		if (!saveFilePath.exists()) {
			// 如果文件夹不存在，则创建新的的文件夹
			saveFilePath.mkdirs();
		}
		
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		String savePath = null;
		
		try {
			// 建立链接
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// post方式不能使用缓存
			conn.setUseCaches(false);
			// 连接指定的资源
			conn.connect();
			// 获取网络输入流
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			
			// 判断文件的保存路径后面是否以/结尾
			if (!fileDir.endsWith("/")) {
				fileDir += "/";
			}
			
			// 写入到文件（注意文件保存路径的后面一定要加上文件的名称）
			savePath = fileDir + fileName + "." + exName;
			fileOut = new FileOutputStream(savePath);
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);
			
			byte[] buf = new byte[8192];
			int length = bis.read(buf);
			// 保存文件
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
			bos.close();
			bis.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			e.fillInStackTrace();
		}
		return fileName;
	}
}
