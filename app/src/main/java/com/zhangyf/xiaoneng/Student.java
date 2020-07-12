package com.zhangyf.xiaoneng;

import android.icu.text.StringSearch;

import java.util.List;

public class Student {
    private String id;
    private String name;
    private List<Issue> issueList;

    public Student(String id, String name, List<Issue> issueList) {
        this.id = id;
        this.name = name;
        this.issueList = issueList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }
}
