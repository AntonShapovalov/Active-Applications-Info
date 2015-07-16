package ru.org.adons.slog;

public class LogItem implements Comparable<LogItem> {

    private String name;
    private int count;
    private String time;

    public LogItem(String name, int count, String time) {
        this.name = name;
        this.count = count;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(LogItem o) {
        // set itself on top the list
        if (this.name == "ru.org.adons.slog/LogSummary") {
            return -1;
        }
        if (o.getName() == "ru.org.adons.slog/LogSummary") {
            return 1;
        }
        return o.getCount() - this.count;
    }

}
