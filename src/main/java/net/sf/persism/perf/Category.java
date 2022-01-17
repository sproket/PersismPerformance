package net.sf.persism.perf;

public enum Category {
    Any, Setup, Result, Other;

    @Override
    public String toString() {
        if (this == Any) {
            return "";
        }
        return name();
    }
}
