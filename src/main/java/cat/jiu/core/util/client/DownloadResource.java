package cat.jiu.core.util.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.Maps;

import cat.jiu.core.util.JiuUtils;
import cat.jiu.core.util.system.file.FileDownload;

import net.minecraft.util.ResourceLocation;

public class DownloadResource {
	private static Map<String, Map<ResourceLocation, String>> urls;
	
	public static void register(ResourceLocation loc, String directLink) {
		if(urls==null) urls = Maps.newHashMap();
		if(!urls.containsKey(loc.getResourceDomain())) urls.put(loc.getResourceDomain(), Maps.newHashMap());
		urls.get(loc.getResourceDomain()).put(loc, directLink);
	}
	
	/** you need start other thread to download the resource */
	public static Info download(String domain) {
		if(urls.containsKey(domain)) {
			Map<ResourceLocation, String> urls = DownloadResource.urls.get(domain);
			long currentMS = 0;
			int success = 0;
			int fail = 0;
			Map<String, String> md5s = Maps.newHashMap();
			for(Entry<ResourceLocation, String> url : urls.entrySet()) {
				long time = System.currentTimeMillis();
				
				String path = JiuUtils.other.toStringPath(url.getKey());
				
				int last0 = path.lastIndexOf("/");
				int last1 = path.lastIndexOf(".");
				String fileDir = path.substring(0, last0+1);
				String fileName = path.substring(last0+1, last1);
				String fileExName = path.substring(last1+1);
				boolean isSuccess = FileDownload.download(url.getValue(), fileName, fileDir, fileExName);
				if(isSuccess) {
					success++;
					try {
						md5s.put(url.getKey().getResourcePath(), DigestUtils.md5Hex(new FileInputStream(path)));
					}catch(IOException e) {
						e.printStackTrace();
					}
				}else {
					fail++;
					md5s.put(url.getKey().getResourcePath(), "fail");
				}
				currentMS += time = System.currentTimeMillis() - time;
			}
			return new Info(urls.size(), success, fail, currentMS, md5s);
		}
		return null;
	}
	
	public static class Info {
		public final int total;
		public final int success;
		public final int fail;
		public final long currentMS;
		protected final Map<String, String> md5s;
		protected Info(int total, int success, int fail, long currentMS, Map<String, String> md5s) {
			this.total = total;
			this.success = success;
			this.fail = fail;
			this.currentMS = currentMS;
			this.md5s = md5s;
		}
		public Map<String, String> getFileMD5s() {
			return Maps.newHashMap(this.md5s);
		}
		@Override
		public String toString() {
			return "Total:" + this.total + ", Success:"+this.success + ", Fail:"+this.fail+", CurrentMs:"+this.currentMS;
		}
	}
}
