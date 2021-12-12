package net.sf.persism.perf;

import java.time.LocalDateTime;

public final class PerfTest {

    private int id;
    private String description;
    private LocalDateTime startTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "PerfTest{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
