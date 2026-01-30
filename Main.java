import java.util.List;
import java.util.ArrayList;
import reader.LogFileReader;
import parser.LogParser;
import processor.LogAggregator;
import model.LogEntry;
import db.DatabaseWriter;

public class Main {

    public static void main(String[] args) {
        List<LogEntry> entries = new ArrayList<>();

        LogFileReader reader = new LogFileReader();
        LogParser parser = new LogParser();
        LogAggregator aggregator = new LogAggregator();

        reader.readFile("logs/test1.log", line -> {
            LogEntry entry = parser.parse(line);
            if (entry != null) {
                entries.add(entry);
            }
        });
        aggregator.process(entries);
        aggregator.printResults();

        DatabaseWriter writer = new DatabaseWriter();
        writer.writeErrorsPerHour(aggregator.getErrorsPerHour());
        writer.writeErrorsPerUser(aggregator.getErrorsPerUser());
        writer.writeUserActivity(aggregator.getUserActivity());

    }
}

