package net.sf.persism.perf;

import java.sql.Timestamp;

public final class PerfTest {

    private long id;
    private Category category;
    private String testClass;
    private String testMethod;
    private String testText;
    private long timing;
    private long timingMS;
    private int lineCount;

    private Timestamp startTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
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

    public long getTimingMS() {
        return timingMS;
    }

    public void setTimingMS(long timingMS) {
        this.timingMS = timingMS;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    @Override
    public String toString() {
        return "PerfTest{" +
                "id=" + id +
                ", testClass='" + testClass + '\'' +
                ", testMethod='" + testMethod + '\'' +
                ", testText='" + testText + '\'' +
                ", timing=" + timing +
                ", timingms=" + timingMS +
                ", startTime=" + startTime +
                ", lineCount=" + lineCount +
                '}';
    }
}
