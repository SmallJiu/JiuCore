package cat.jiu.core.util.system.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;

import cat.jiu.core.api.IProgress;

public class FileDownload {
	public static boolean download0(String url, String fileName, String fileDir, String exName) {
		String method = "GET";
		
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		if (!fileDir.endsWith("/")) {
			fileDir += "/";
		}
		File savePath = new File(fileDir, fileName + "." + exName);
		if(savePath.exists()) return true;
		savePath.getParentFile().mkdirs();
		
		try {
			savePath.createNewFile();
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

	static final DecimalFormat df = new DecimalFormat("0.000");
	public static boolean download(String url, File savePath, IProgress call) {
		if(savePath.exists()) return true;
		if(savePath.getParentFile() != null && !savePath.getParentFile().exists()) savePath.getParentFile().mkdirs();
		
		try(Auto conn = new Auto(new URL(url).openConnection()).connect();
			InputStream net = conn.connection.getInputStream();
			OutputStream localOut = new FileOutputStream(savePath)) {
			
			final long fileTotalLength = conn.connection.getContentLengthLong();
			long currentLength = 0;
			
			byte[] buf = new byte[8192];
			int read = 0;
			
			SpeedPer second = new SpeedPer();
			startCheckSpeed(second, 1000);
			
			SpeedPer tick = new SpeedPer();
			startCheckSpeed(tick, 50);
			
			while((read = net.read(buf)) != -1) {
				localOut.write(buf, 0, read);
				currentLength += read;
				double progress = Double.parseDouble(df.format(Math.ceil((double)currentLength / conn.connection.getContentLengthLong()*100000) / 100000 * 100));
				second.current = currentLength;
				tick.current = currentLength;
				call.call(currentLength, fileTotalLength, progress, second.speed, tick.speed);
			}
			second.done = true;
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	static class SpeedPer {
		boolean done = false;
		long current = 0;
		long speed = 0;
	}
	
	static void startCheckSpeed(SpeedPer per, long ms) {
		new Thread(()->{
			long l = 0;
			while(!per.done) {
				try {
					Thread.sleep(ms);
					per.speed = per.current - l;
					l = per.current;
				}catch(InterruptedException e) {}
			}
		}).start();
	}
	
	private static class Auto implements AutoCloseable {
		private final URLConnection connection;
		public Auto(URLConnection connection) {
			this.connection = connection;
		}
		public Auto connect() throws IOException {
			if(this.connection!=null) {
				this.connection.connect();
			}
			return this;
		}
		@Override
		public void close() throws Exception {
			if(this.connection instanceof HttpURLConnection) {
				((HttpURLConnection)this.connection).disconnect();
			}
			if(this.connection instanceof HttpsURLConnection) {
				((HttpsURLConnection)this.connection).disconnect();
			}
		}
	}
}
