package cat.jiu.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.IllegalFormatException;

/**
 * a other Logger, use for not wan to implementation Log4j2
 * @author small_jiu
 */
public final class Logger {
    private static final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter formatYear = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");
    private static ArrayList<Logger> logs = new ArrayList<>();
    static {
    	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				for(Logger logger : logs) logger.backupLogFile();
			}catch(Exception e) {
				e.printStackTrace();
			}
		},"Backup log Thread"));
    }
    
    private String tag;
    
    public Logger() {
    	String name = new Throwable().getStackTrace()[1].getClassName();
    	this.setTag(name.substring(name.lastIndexOf(".")+1));
    	this.setFilterClass(Logger.class);
    	logs.add(this);
    }
    public Logger(String tag) {
        this.setTag(tag);
    	this.setFilterClass(Logger.class);
    	logs.add(this);
    }

    public void log(Level level, String msg, Object... args) {
        StringBuilder sb = new StringBuilder("[");
        
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        StackTraceElement stack=this.check(stacks);
        
        String clazzName = stack.getClassName();
        clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1, clazzName.length()) + ":" + stack.getLineNumber();
        
        sb.append(formatTime.format(LocalDateTime.now()))
          .append("] [")
          .append(Thread.currentThread().getName())
          .append("/")
          .append(level)
          .append("] [")
          .append(this.tag)
          .append("/")
          .append(clazzName)
          .append("]: ");
        
        try {
			sb.append(String.format(msg.replace("{}", "%s"), args));
		}catch(IllegalFormatException e) {
			sb.append("Format error: " + msg);
		}
        String format = sb.toString();
        if(level == Level.ERROR) {
        	System.err.println(format.toCharArray());
        }else {
            System.out.println(format.toCharArray());
        }
		
        try {
			this.logToFile(format);
		}catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    private StackTraceElement check(StackTraceElement[] stacks) {
    	if(this.clazzs!=null) for(int i = stacks.length-1; i > 0; i--) {
    		for(int j = 0; j < this.clazzs.size(); j++) {
    			if(stacks[i].getClassName().equals(this.clazzs.get(j))) {
    				return stacks[i+1];
    			}
    		}
    	}
    	return null;
    }
    
    private static ArrayList<String> logsName = new ArrayList<>();
    private File logFile;
    public void setLogFile(String name) {
		this.logFile = new File(name+".jiu.log");
		if(!logsName.contains(name)) {
			try {
				if(this.logFile.exists()) this.logFile.delete();
				this.logFile.createNewFile();
				logsName.add(name);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    public void backupLogFile() {
    	try {
            File backup = new File("./logs/latest/"+formatYear.format(LocalDateTime.now())+".jiu.log");
            InputStream input = new FileInputStream(this.logFile);
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(backup));
            int temp = 0;
            while((temp = input.read()) != -1){
            	out.write(temp);
            }
            input.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void logToFile(String msg) throws IOException {
    	if(this.logFile!=null) {
    		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(this.logFile, true));
    		out.write(msg + "\n");
    		out.close();
    	}
    }
    
    private ArrayList<String> clazzs;
    public void setFilterClass(Class<?>... clazzs) {
    	if(this.clazzs==null) this.clazzs = new ArrayList<>();
    	for(Class<?> clazz : clazzs) {
			this.clazzs.add(clazz.getName());
		}
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
	public String getName() {
		return this.tag;
	}
	
    public void info(String msg, Object... args) {
        log(Level.INFO, msg, args);
    }
    public void warning(String msg, Object... args) {
        log(Level.WARN, msg, args);
    }
    public void error(String msg, Object... args) {
        log(Level.ERROR, msg, args);
    }
    public void debug(String msg, Object... args) {
        log(Level.DEBUG, msg, args);
    }
    public void fatal(String msg, Object... args) {
        log(Level.FATAL, msg, args);
    }
    public static enum Level {
        INFO, ERROR, WARN, DEBUG, FATAL;
    }
}
