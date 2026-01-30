package model;
import java.time.LocalDateTime;
public class LogEntry {
    private LocalDateTime timestamp;
    private String level;
    private String action;
    private String user;

    public LogEntry(LocalDateTime timestamp, String level, String action, String user){
        this.timestamp = timestamp;
        this.level = level;
        this.action = action;
        this.user = user;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public String getLevel(){
        return level;
    }
    public String getAction(){
        return action;
    }
    public String getUser(){
        return user;
    }
    public String toString(){
        return "[" + timestamp.toString() + "] " + level + " - " + action + " by " + user;
    }
    
    public static void main(String[] args){
        LogEntry entry = new LogEntry(LocalDateTime.now(), "ERROR", "login", "Krishna");
        System.out.println(entry.toString());
    }
}
