package com.javaassign5.ex3.model;

import com.javaassign5.ex3.annotations.JsonName;
import com.javaassign5.ex3.annotations.MaxLength;
import com.javaassign5.ex3.annotations.Required;

/** A to-do item. Validation rules and JSON names are declared with our own annotations. */
public class Task {

    public enum Priority { LOW, MEDIUM, HIGH }

    @JsonName("task_id")
    private final int id;

    @Required(message = "title is required")
    @MaxLength(40)
    @JsonName("task_title")
    private final String title;

    @Required(message = "priority is required")
    @JsonName("priority_level")
    private final Priority priority;

    @JsonName("is_done")
    private boolean done;

    public Task(int id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }

    public int getId()             { return id; }
    public String getTitle()       { return title; }
    public Priority getPriority()  { return priority; }
    public boolean isDone()        { return done; }
    public void markDone()         { this.done = true; }
}
