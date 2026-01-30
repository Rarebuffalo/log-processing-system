package processor;

import model.LogEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LogAggregator {

    private Map<Integer, Long> errorsPerHour = new HashMap<>();
    private Map<String, Long> errorsPerUser = new HashMap<>();
    private Map<String, Long> userActivity = new HashMap<>();

    public void process(java.util.List<LogEntry> entries) {
        this.errorsPerHour = entries.stream()
           .filter(e -> "ERROR".equals(e.getLevel()))
           .collect(Collectors.groupingBy(
               e -> e.getTimestamp().getHour(),
               Collectors.counting()
           ));

        this.errorsPerUser = entries.stream()
           .filter(e -> "ERROR".equals(e.getLevel()))
           .collect(Collectors.groupingBy(
               LogEntry::getUser,
               Collectors.counting()
           ));

        this.userActivity = entries.stream()
           .collect(Collectors.groupingBy(
               LogEntry::getUser,
               Collectors.counting()
           ));
    }

    public Map<Integer, Long> getErrorsPerHour() {
    return errorsPerHour;
    }

    public Map<String, Long> getErrorsPerUser() {
        return errorsPerUser;
    }

    public Map<String, Long> getUserActivity() {
        return userActivity;
    }


    public void printResults(){
       System.out.println("Errors per hour:");
       errorsPerHour.forEach((hour, count) ->
       	   System.out.println(hour + " => " + count)
	   );
       System.out.println("\nErrors per user:");
       errorsPerUser.forEach((user, count) ->
       System.out.println(user + " → " + count)
           );
       System.out.println("\nUser activity:");
       userActivity.forEach((user, count) ->
       System.out.println(user + " → " + count)
           );
    } 
}
