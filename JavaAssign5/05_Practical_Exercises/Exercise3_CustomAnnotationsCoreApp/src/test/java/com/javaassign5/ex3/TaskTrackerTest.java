package com.javaassign5.ex3;

import com.javaassign5.ex3.core.CommandDispatcher;
import com.javaassign5.ex3.core.EntityValidator;
import com.javaassign5.ex3.core.JsonExporter;
import com.javaassign5.ex3.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests for Exercise 3. Run with: mvn test */
class TaskTrackerTest {

    private TaskCommands commands;
    private CommandDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        commands = new TaskCommands();
        dispatcher = new CommandDispatcher(commands);
    }

    @Test
    void helpIsGeneratedFromAnnotations() {
        String help = dispatcher.dispatch("help");
        assertTrue(help.contains("add <priority> <title...>"));
        assertTrue(help.contains("mark a task as finished"));
    }

    @Test
    void addAndCompleteTask() {
        assertEquals("Added task #1: Write tests", dispatcher.dispatch("add high Write tests"));
        dispatcher.dispatch("done 1");
        assertTrue(commands.getTasks().get(0).isDone());
    }

    @Test
    void maxLengthRejectsLongTitle() {
        String result = dispatcher.dispatch("add low " + "x".repeat(41));
        assertTrue(result.startsWith("Rejected: title must be at most 40 characters"));
        assertTrue(commands.getTasks().isEmpty());
    }

    @Test
    void requiredDetectsMissingValues() {
        assertEquals(2, EntityValidator.validate(new Task(1, " ", null)).size());
    }

    @Test
    void jsonUsesJsonNameKeys() {
        String json = JsonExporter.toJson(new Task(7, "Read", Task.Priority.LOW));
        assertTrue(json.contains("\"task_id\": 7"));
        assertTrue(json.contains("\"task_title\": \"Read\""));
        assertTrue(json.contains("\"priority_level\": \"LOW\""));
        assertFalse(json.contains("\"title\""));

        Task back = JsonExporter.fromJson(json, Task.class);
        assertEquals("Read", back.getTitle());
    }

    @Test
    void unknownCommandAndMissingArgs() {
        assertTrue(dispatcher.dispatch("fly").startsWith("Unknown command"));
        assertEquals("Usage: done <id>", dispatcher.dispatch("done"));
    }
}
