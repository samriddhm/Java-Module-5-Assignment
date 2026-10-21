package com.javaassign5.ex3;

import com.javaassign5.ex3.core.CommandDispatcher;

import java.util.Scanner;

/**
 * Practical Exercise 3 - entry point.
 *
 *   mvn -q compile exec:java                    -> runs a scripted demo session
 *   mvn -q compile exec:java -Dexec.args="-i"   -> interactive: type commands yourself
 */
public class TaskTrackerApp {

    static final String[] DEMO_SCRIPT = {
            "help",
            "add high \"Finish Java assignment 5\"",
            "add medium Revise annotations for the quiz",
            "add low Clean desk",
            "add urgent Something",                                            // bad priority
            "add high This title is far too long to be accepted by MaxLength",  // > 40 chars
            "done 1",
            "done 99",                                                          // no such task
            "list",
            "stats",
            "remove 3",
            "export",
            "fly"                                                               // unknown command
    };

    public static void main(String[] args) {
        CommandDispatcher dispatcher = new CommandDispatcher(new TaskCommands());
        System.out.println("=== Task Tracker (commands discovered via @CliCommand) ===");

        if (args.length > 0 && args[0].equals("-i")) {
            Scanner in = new Scanner(System.in);
            System.out.println("Type 'help' for commands, 'exit' to quit.");
            while (true) {
                System.out.print("> ");
                if (!in.hasNextLine()) break;
                String line = in.nextLine();
                if (line.trim().equalsIgnoreCase("exit")) break;
                System.out.println(dispatcher.dispatch(line));
            }
        } else {
            for (String line : DEMO_SCRIPT) {
                System.out.println("> " + line);
                System.out.println(dispatcher.dispatch(line));
                System.out.println();
            }
        }
    }
}
