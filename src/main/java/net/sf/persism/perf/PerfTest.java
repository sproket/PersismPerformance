package net.sf.persism.perf;

import java.time.LocalDateTime;

public final class PerfTest {

    private int id;
    private String testClass;
    private String testMethod;
    private String testText;
    private long timing;

    private LocalDateTime startTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getTestText() {
        return testText;
    }

    public void setTestText(String testText) {
        this.testText = testText;
    }

    public long getTiming() {
        return timing;
    }

    public void setTiming(long timing) {
        this.timing = timing;
    }

    @Override
    public String toString() {
        return "PerfTest{" +
                "id=" + id +
                ", testClass='" + testClass + '\'' +
                ", testMethod='" + testMethod + '\'' +
                ", testText='" + testText + '\'' +
                ", timing=" + timing +
                ", startTime=" + startTime +
                '}';
    }
}
