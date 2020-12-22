package com.example.todo;

import com.example.todo.database.Todo;

import java.util.List;

public class Section {
    private String sectionName;
    private List<Todo> sectionItems;

    public Section(String sectionName, List<Todo> sectionItems) {
        this.sectionName = sectionName;
        this.sectionItems = sectionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<Todo> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(List<Todo> sectionItems) {
        this.sectionItems = sectionItems;
    }
}
