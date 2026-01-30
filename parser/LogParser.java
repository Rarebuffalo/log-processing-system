package parser;

import model.LogEntry;
import java.time.LocalDateTime;

public class LogParser {
    public LogEntry parse(String line) {

    if (line == null || line.isBlank()) {
        return null;
    }

    String[] parts = line.split(" ");

    // basic format check
    if (parts.length < 5) {
        return null;
    }

    try {
        String dateTime = parts[0] + "T" + parts[1];
        LocalDateTime timestamp = LocalDateTime.parse(dateTime);

        String level = parts[2];

        String[] userParts = parts[3].split("=");
        String[] actionParts = parts[4].split("=");

        if (userParts.length < 2 || actionParts.length < 2) {
            return null;
        }

        String user = userParts[1];
        String action = actionParts[1];

        return new LogEntry(timestamp, level, action, user);

    } catch (Exception e) {
        // any parsing issue → ignore line safely
        return null;
    }
    }
}