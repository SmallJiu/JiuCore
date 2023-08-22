package cat.jiu.core.util.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cat.jiu.core.api.IBatchProgress;
import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.system.file.FileDownload;

import net.minecraft.util.ResourceLocation;

public class DownloadResource {
	private static Map<String, Map<ResourceLocation, String>> urls;
	private static Map<ResourceLocation, File> files;
	
	public static void register(String link, ResourceLocation loc) {
		if(urls==null) urls = Maps.newHashMap();
		if(!urls.containsKey(loc.getResourceDomain())) urls.put(loc.getResourceDomain(), Maps.newHashMap());
		urls.get(loc.getResourceDomain()).put(loc, link);
	}
	
	public static File getFile(ResourceLocation loc) {
		if(files==null) return null;
		return files.get(loc);
	}
	
	public static Map<ResourceLocation, File> getFiles(String domain) {
		if(files==null || domain==null || files.isEmpty() || domain.isEmpty()) return Collections.emptyMap();
		
		Map<ResourceLocation, File> files = Maps.newHashMap();
		for(Entry<ResourceLocation, File> file : DownloadResource.files.entrySet()) {
			if(file.getKey().getResourceDomain().equals(domain)) {
				files.put(file.getKey(), file.getValue());
			}
		}
		return files;
	}
	
	public static final File TEMP_PATH = new File("./assets/%Temp%/");
	
	/** you need start other thread to download the resources */
	public static Info download(String domain, IBatchProgress call) {
		if(urls.containsKey(domain)) {
			Map<ResourceLocation, String> urls = DownloadResource.urls.get(domain);
			Map<File, String> md5s = Maps.newHashMap();
			List<Thread> current = Lists.newArrayList();
			AllTasksInfo infos = new AllTasksInfo();
			
			for(Entry<ResourceLocation, String> url : urls.entrySet()) {
				Thread t = newDownloadTask(url, infos, md5s, call);
				current.add(t);
				t.start();
			}
			
			while(true) {
				try {
					Thread.sleep(50);
					if(!isAlive(current)) {
						infos.endTimeMillis = System.currentTimeMillis();
						infos.done = true;
						break;
					}
				}catch(InterruptedException e1) {e1.printStackTrace();}
			}
			return new Info(urls.size(), infos, md5s);
		}
		return null;
	}
	
	static boolean isAlive(List<Thread> threads) {
		for(Thread t : threads) {
			if(t.isAlive()) return true;
		}
		return false;
	}
	
	static Thread newDownloadTask(Entry<ResourceLocation, String> url, AllTasksInfo infos, Map<File, String> md5s, IBatchProgress call) {
		Thread t = new Thread(()->{
			ResourceLocation key = url.getKey();
			String value = url.getValue();
			
			File file = new File(JiuUtils.other.toStringPath(key));
			File temp = new File(file.getParentFile(), file.getName() + ".temp");
			boolean isSuccess = false;
			if(file.exists()) {
				isSuccess = true;
			}else {
				isSuccess = FileDownload.download(value, temp, (fileLength, fileTotalLength, progress, speedPerSecond, speedPerTick)->{
					call.call(infos, file, fileLength, fileTotalLength, progress, speedPerSecond, speedPerTick);
				});
			}
			
			if(isSuccess) {
				try {
					boolean flag = temp.renameTo(file);
					if(flag) {
						md5s.put(file, DigestUtils.md5Hex(new FileInputStream(file)));
						if(files==null) files = Maps.newHashMap();
						files.put(key, file);
						infos.success++;
					}else {
						md5s.put(file, "fail");
						infos.fail++;
					}
				}catch(IOException e0) {
					e0.printStackTrace();
				}
			}else {
				md5s.put(file, "fail");
				infos.fail++;
			}
		});
		return t;
	}
	
	public static class AllTasksInfo {
		int success = 0;
		int fail = 0;
		long startTimeMillis = System.currentTimeMillis();
		long endTimeMillis = 0;
		boolean done = false;
		public int getSuccess() {
			return success;
		}
		public int getFail() {
			return fail;
		}
		public long getTook() {
			return (done ? endTimeMillis : System.currentTimeMillis()) - startTimeMillis;
		}
	}
	
	public static class Info {
		public final int total;
		public final int success;
		public final int fail;
		public final long currentMS;
		protected final Map<File, String> md5s;
		private Info(int total, AllTasksInfo e, Map<File, String> md5s) {
			this(total, e.success, e.fail, e.getTook(), md5s);
		}
		protected Info(int total, int success, int fail, long currentMS, Map<File, String> md5s) {
			this.total = total;
			this.success = success;
			this.fail = fail;
			this.currentMS = currentMS;
			this.md5s = md5s;
		}
		public Map<File, String> getFilesMD5() {
			return Maps.newHashMap(this.md5s);
		}
		@Override
		public String toString() {
			return "Total:" + this.total + ", Success:"+this.success + ", Fail:"+this.fail+", CurrentMs:"+this.currentMS;
		}
	}
}
