# Log Processing System

A comprehensive Java-based system for reading, parsing, processing, and analyzing application logs with persistent storage in PostgreSQL.

## Overview

This log processing system is designed to efficiently handle large volumes of application logs by:

- Reading log files line by line
- Parsing structured log entries
- Aggregating and analyzing log data
- Storing results in a PostgreSQL database for further analysis

## System Architecture

The system follows a modular, pipeline-based architecture with clear separation of concerns:

```bash

Log File → Reader → Parser → Aggregator → Database
```

### Components

#### 1. **LogFileReader** (`reader/LogFileReader.java`)

- Reads log files using buffered I/O for efficiency
- Uses a consumer-based callback pattern for flexible line processing
- Handles file I/O errors gracefully

#### 2. **LogParser** (`parser/LogParser.java`)

- Parses individual log lines into structured `LogEntry` objects
- Expects log format: `YYYY-MM-DD HH:MM:SS LEVEL user=USERNAME action=ACTION`
- Returns `null` for invalid log entries (safeguards against malformed data)
- Extracts: timestamp, log level, user information, and action performed

#### 3. **LogEntry** (`model/LogEntry.java`)

- Data model representing a single log entry
- Fields:
  - `timestamp`: LocalDateTime of when the event occurred
  - `level`: Log level (e.g., ERROR, INFO, DEBUG)
  - `action`: The action performed (e.g., login, logout, file access)
  - `user`: Username associated with the log entry

#### 4. **LogAggregator** (`processor/LogAggregator.java`)

- Processes a list of `LogEntry` objects
- Generates three key aggregations using Java Streams:
  - **Errors per Hour**: Count of ERROR-level logs grouped by hour of day
  - **Errors per User**: Count of ERROR-level logs grouped by username
  - **User Activity**: Total activity count per user across all log levels
- Provides results in `Map` format for easy database insertion

#### 5. **DatabaseWriter** (`db/DatabaseWriter.java`)

- Connects to PostgreSQL database at `localhost:5432/log_analytics`
- Writes aggregated data using UPSERT operations
- Tables supported:
  - `errors_per_hour`: Hour and error count
  - `errors_per_user`: Username and error count
  - `user_activity`: Username and total activity count
- Uses prepared statements for security and performance

#### 6. **Main** (`Main.java`)

- Orchestrates the entire pipeline
- Reads log file → Parses entries → Aggregates data → Writes to database
- Example usage processes `logs/test1.log`

## Prerequisites

### System Requirements

- Java 8 or higher
- PostgreSQL 10 or higher
- PostgreSQL JDBC driver

### Database Setup

Create the required PostgreSQL database and tables:

```sql
CREATE DATABASE log_analytics;

CREATE TABLE errors_per_hour (
    hour INT PRIMARY KEY,
    error_count BIGINT NOT NULL
);

CREATE TABLE errors_per_user (
    username VARCHAR(255) PRIMARY KEY,
    error_count BIGINT NOT NULL
);

CREATE TABLE user_activity (
    username VARCHAR(255) PRIMARY KEY,
    activity_count BIGINT NOT NULL
);
```

### Configuration

Update database credentials in `DatabaseWriter.java`:

- `URL`: PostgreSQL connection URL (default: `jdbc:postgresql://localhost:5432/log_analytics`)
- `USER`: Database username (default: `postgres`)
- `PASSWORD`: Database password

## Log File Format

The system expects log files with the following format:

```bash
YYYY-MM-DD HH:MM:SS LEVEL user=USERNAME action=ACTION
```

### Example

```bash
2024-01-15 10:23:45 ERROR user=admin action=failed_login
2024-01-15 10:24:12 INFO user=john action=login
2024-01-15 10:25:30 ERROR user=admin action=unauthorized_access
2024-01-15 10:26:01 INFO user=jane action=file_upload
```

## Usage

### Compiling the Project

```bash
javac -d bin src/**/*.java
```

### Running the System

```bash
java -cp bin:. Main
```

### Processing a Different Log File

Edit `Main.java` and change the log file path:

```java
reader.readFile("path/to/your/log/file.log", line -> {
    LogEntry entry = parser.parse(line);
    if (entry != null) {
        entries.add(entry);
    }
});
```

## Output

The system produces:

1. **Console Output**: Aggregated statistics (errors per hour, errors per user, user activity)
2. **Database Storage**: Persistent storage of aggregations for long-term analysis

## Error Handling

- Invalid log entries are silently skipped (logged as `null`)
- File I/O errors are printed to stderr
- Database connection errors are handled with exception output
- Malformed log entries don't crash the system; processing continues

## Performance Characteristics

- **Time Complexity**: O(n) where n is the number of log entries
- **Space Complexity**: O(m) where m is the number of unique hours/users
- **Scalability**: Buffered I/O and streaming operations support large log files
- **Database Operations**: Batch processing with UPSERT for efficient updates

## Future Enhancements

- Multi-threaded log processing for improved performance
- Support for additional log formats
- Real-time log streaming capabilities
- Advanced filtering and searching features
- Dashboard visualization of analytics
- Log rotation and compression support

## Project Structure

```bash
log-processing-system/
├── Main.java                    # Entry point and pipeline orchestration
├── README.md                    # This file
├── db/
│   └── DatabaseWriter.java      # PostgreSQL database operations
├── logs/
│   └── test1.log               # Sample log file
├── model/
│   └── LogEntry.java           # Log entry data model
├── parser/
│   └── LogParser.java          # Log line parsing logic
├── processor/
│   └── LogAggregator.java      # Log aggregation and analysis
└── reader/
    └── LogFileReader.java      # Log file reading operations
```

## License

This project is provided as-is for educational and organizational purposes.

## Support

For issues or questions regarding the log processing system, refer to the individual component documentation or review the source code comments.
