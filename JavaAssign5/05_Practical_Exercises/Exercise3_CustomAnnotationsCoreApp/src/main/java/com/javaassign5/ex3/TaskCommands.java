package com.javaassign5.ex3;

import com.javaassign5.ex3.annotations.CliCommand;
import com.javaassign5.ex3.core.EntityValidator;
import com.javaassign5.ex3.core.JsonExporter;
import com.javaassign5.ex3.model.Task;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The application's commands. There is NO if/else or switch on the command name anywhere:
 * the @CliCommand annotations are all the CommandDispatcher needs.
 */
public class TaskCommands {

    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    @CliCommand(name = "add", usage = "add <priority> <title...>", minArgs = 2,
                description = "add a task (priority = low | medium | high)")
    public String add(String[] args) {
        Task.Priority priority;
        try {
            priority = Task.Priority.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error: priority must be low, medium or high";
        }
        String title = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        Task t = new Task(nextId, title, priority);

        List<String> errors = EntityValidator.validate(t);
        if (!errors.isEmpty()) return "Rejected: " + String.join("; ", errors);

        tasks.add(t);
        nextId++;
        return "Added task #" + t.getId() + ": " + t.getTitle();
    }

    @CliCommand(name = "list", description = "show all tasks")
    public String list(String[] args) {
        if (tasks.isEmpty()) return "(no tasks)";
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad("ID", 4)).append(StringUtils.rightPad("PRIORITY", 10))
          .append(StringUtils.rightPad("DONE", 6)).append("TITLE\n");
        for (Task t : tasks) {
            sb.append(StringUtils.rightPad(String.valueOf(t.getId()), 4))
              .append(StringUtils.rightPad(t.getPriority().name(), 10))
              .append(StringUtils.rightPad(t.isDone() ? "[x]" : "[ ]", 6))
              .append(StringUtils.abbreviate(t.getTitle(), 30)).append('\n');
        }
        return sb.toString().trim();
    }

    @CliCommand(name = "done", usage = "done <id>", minArgs = 1, description = "mark a task as finished")
    public String done(String[] args) {
        Task t = find(args[0]);
        t.markDone();
        return "Task #" + t.getId() + " marked as done";
    }

    @CliCommand(name = "remove", usage = "remove <id>", minArgs = 1, description = "delete a task")
    public String remove(String[] args) {
        Task t = find(args[0]);
        tasks.remove(t);
        return "Removed task #" + t.getId();
    }

    @CliCommand(name = "stats", description = "count tasks by status")
    public String stats(String[] args) {
        long done = tasks.stream().filter(Task::isDone).count();
        long high = tasks.stream().filter(t -> t.getPriority() == Task.Priority.HIGH && !t.isDone()).count();
        return tasks.size() + " task(s): " + done + " done, " + (tasks.size() - done) + " open, "
                + high + " open HIGH priority";
    }

    @CliCommand(name = "export", description = "print all tasks as JSON (keys from @JsonName)")
    public String export(String[] args) {
        return JsonExporter.toJson(tasks);
    }

    List<Task> getTasks() {
        return tasks;
    }

    private Task find(String idText) {
        if (!StringUtils.isNumeric(idText)) throw new IllegalArgumentException("id must be a number");
        int id = Integer.parseInt(idText);
        return tasks.stream().filter(t -> t.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no task with id " + id));
    }
}
