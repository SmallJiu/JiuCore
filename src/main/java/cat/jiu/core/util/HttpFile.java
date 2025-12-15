package cat.jiu.core.util;

import javax.annotation.Nullable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class HttpFile {
    // Test
    public static void main(String[] args) {
        HttpFile file1 = new HttpFile()
                .setSaveFile(new File("C:/test.png"))
                .setOverwrite(true)
                .addUrls(
                        "http://localhost:3000/mods/Inbox/image/%E9%82%AE%E7%AE%B1%E4%B8%BB%E7%95%8C%E9%9D%A2.png"
                );
        System.out.println();
        for (int i = 0; i < 512; i++) {
            file1
                    .setSaveFile(new File("C:/test/test"+i+".png"))
                    .tryDownload(null, file->
                                System.out.println("Download success: " + file.getName() + ", Thread: " + Thread.currentThread().getName())
                            ,e-> {
                                System.err.println("Downloading fail ! " + e);
                                e.printStackTrace();
                            }, null);
        }
    }

    private File saveFile;
    private List<String> urls;
    protected boolean overwrite;

    public HttpFile() {
    }

    public HttpFile(File saveFile, String... urls) {
        this.setSaveFile(saveFile);
        this.addUrls(urls);
    }

    public File getSaveFile() {
        return saveFile;
    }

    public HttpFile setSaveFile(File saveFile) {
        this.saveFile = saveFile;
        return this;
    }

    public List<String> getUrls() {
        if (this.urls == null) {
            this.urls = new ArrayList<>();
        }
        return this.urls;
    }

    public HttpFile addUrls(String... urls) {
        if (this.urls == null) {
            this.urls = new ArrayList<>(Arrays.asList(urls));
        }else {
            this.urls.addAll(Arrays.asList(urls));
        }
        return this;
    }
    public HttpFile setUrls(String... urls) {
        this.urls = new ArrayList<>(Arrays.asList(urls));
        return this;
    }
    public HttpFile setUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public HttpFile setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }

    public void tryDownload(@Nullable IProgress progress, @Nullable Consumer<File> done, @Nullable Consumer<Exception> exception, @Nullable Runnable fail) {
        File file = this.getSaveFile();
        boolean overwrite = this.isOverwrite();
        List<String> urls = new ArrayList<>(this.getUrls());

        DownloadThread.addTask(()->{
            boolean downloaded = false;
            if (!urls.isEmpty()) {
                for (String url : urls) {
                    try {
                        try {
                            download(url, file, overwrite, progress);
                        }catch (FileExistsException ignored) {}
                        if(done != null) done.accept(file);
                        downloaded = true;
                        break;
                    } catch (Exception e) {
                        if(exception != null) exception.accept(e);
                    }
                }
            }
            if (!downloaded && fail != null) {
                fail.run();
            }
        });
    }

    public static void download(String url, File savePath, boolean overwrite, IProgress call) throws IOException {
        if(savePath.exists() && !overwrite) throw new FileExistsException(savePath);
        if(savePath.getParentFile() != null && !savePath.getParentFile().exists()) {
            savePath.getParentFile().mkdirs();
        }
        if (!savePath.exists()) {
            savePath.createNewFile();
        }

        try(Auto conn = new Auto(new URL(url).openConnection()).connect();
            InputStream net = conn.connection.getInputStream();
            OutputStream localOut = new FileOutputStream(savePath)) {

            long currentLength = 0;

            long fileTotalLength = conn.connection.getContentLengthLong();
            byte[] buf = new byte[8192];
            int read;

            while((read = net.read(buf)) != -1) {
                localOut.write(buf, 0, read);

                if (call != null) {
                    currentLength += read;

                    double progress = Math.ceil((double)currentLength / fileTotalLength * 10000000) / 10000000 * 100;
                    progress = Double.parseDouble(String.format("%.2f", progress));
                    call.call(savePath, currentLength, fileTotalLength, progress);
                }
            }
        }
    }

    public static class FileExistsException extends IOException {
        public FileExistsException() {
        }
        public FileExistsException(String message) {
            super(message);
        }
        public FileExistsException(File file) {
            super("File " + file + " exists");
        }
    }

    public interface IProgress {
        void call(File file, long fileCurrentLength, long fileTotalLength, double progress);
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
        public void close() throws IOException {
            if(this.connection instanceof HttpURLConnection) {
                ((HttpURLConnection)this.connection).disconnect();
            }
        }
    }
    public static class DownloadThread extends Thread {
        private static final int THREAD_TIMEOUT_MS = 1000;
        private static final Object LOCK = new Object();
        private static final HashMap<Integer, DownloadThread> CURRENT_THREAD_LIST = new HashMap<>();
        private static final BlockingQueue<Runnable> GLOBAL_TASK_QUEUE = new LinkedBlockingQueue<>();

        private static int MAX_THREAD = 5;
        private static boolean started;

        public static void startThread() {
            if (!started) {
                synchronized (LOCK) {
                    for (int i = 0; i < MAX_THREAD; i++) {
                        newThread();
                    }
                    started = true;
                }
            }
        }
        private static void newThread() {
            synchronized(LOCK) {
                int index = CURRENT_THREAD_LIST.size();
                DownloadThread thread = new DownloadThread(index);
                CURRENT_THREAD_LIST.put(index, thread);
                thread.start();
            }
        }

        public static void setMaxThread(int maxThread) {
            synchronized (LOCK) {
                MAX_THREAD = Math.max(1, maxThread);

                // 调整线程数量
                int currentThreads = CURRENT_THREAD_LIST.size();
                if (currentThreads < MAX_THREAD) {
                    // 启动新线程
                    for (int i = currentThreads; i < MAX_THREAD; i++) {
                        newThread();
                    }
                } else if (currentThreads > MAX_THREAD) {
                    // 停止多余线程
                    for (int i = currentThreads - 1; i >= MAX_THREAD; i--) {
                        if (i < CURRENT_THREAD_LIST.size()) {
                            CURRENT_THREAD_LIST.remove(i).shutdown();
                        }
                    }
                }
            }
        }
        public static int getMaxThread() {
            return MAX_THREAD;
        }
        public static void addTask(Runnable task) {
            GLOBAL_TASK_QUEUE.offer(task);
            startThread();
        }
        public static void shutdownAll() {
            synchronized (LOCK) {
                System.out.println("Shutting down all download threads...");

                CURRENT_THREAD_LIST.forEach((k,v)->
                        v.shutdown()
                );

                CURRENT_THREAD_LIST.clear();
                GLOBAL_TASK_QUEUE.clear();

                started = false;

                System.out.println("All download threads shut down");
            }
        }

        private final int index;
        private boolean running = true;
        private DownloadThread(int index) {
            this.index = index;
            this.setName("JiuCoreDownloadThread-" + index);
        }
        @Override
        public void run() {
            try {
                while (this.running) {
                    Runnable task = null;
                    try {
                        task = GLOBAL_TASK_QUEUE.poll(THREAD_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    } catch (Exception ignored) {
                        if (!this.running) {
                            break;
                        }
                        this.interrupt();
                    }
                    if (task != null) {
                        task.run();
                    }else {
                        System.out.println("Download thread " + index + " exiting (exceeds max threads)");
                        break;
                    }
                }
            }finally {
                this.running = false;
                CURRENT_THREAD_LIST.remove(this.index);
                if (CURRENT_THREAD_LIST.isEmpty()) {
                    GLOBAL_TASK_QUEUE.clear();
                    started = false;
                }
            }
        }

        public void shutdown() {
            this.running = false;
            this.interrupt();
        }
    }
}
