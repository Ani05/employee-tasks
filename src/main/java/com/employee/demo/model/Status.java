package com.employee.demo.model;

public enum Status {
    NEW ("NEW"),
    INPROGRESS ("IN PROGRESS"),
    FINISH ("FINISH");

    Status(String name) {
        this.name=name;
    }
    private final String name;

    public String getName() {
        return name;
    }
}
