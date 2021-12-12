package net.sf.persism.perf;

public final  class PerfTestResult {
    private int id;
    private int testId;
    private String testClass;
    private String testMethod;
    private String description;
    private long timing;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public long getTiming() {
        return timing;
    }

    public void setTiming(long timing) {
        this.timing = timing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PerfTestResult{" +
                "id=" + id +
                ", testId=" + testId +
                ", testClass='" + testClass + '\'' +
                ", testMethod='" + testMethod + '\'' +
                ", timing=" + timing +
                '}';
    }
}
