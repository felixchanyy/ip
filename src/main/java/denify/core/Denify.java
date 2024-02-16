package denify.core;

import denify.exception.DenifyException;
import denify.parser.Parser;
import denify.storage.Storage;
import denify.task.Task;
import denify.task.TaskList;
import denify.ui.Ui;

/**
 * The `Denify` class represents the main application that manages tasks.
 * It handles user interactions, parses commands, and performs operations on tasks.
 */
public class Denify {
    /**
     * The file path where tasks are stored.
     */
    private static final String FILEPATH = "./data/denify.txt";
    /**
     * Represents the storage component responsible for loading and saving tasks.
     */
    private final Storage storage;
    /**
     * Represents the list of tasks managed by Denify.
     */
    private TaskList tasks;
    /**
     * Represents the user interface component for interacting with the user.
     */
    private final Ui ui;
    /**
     * Constructs a `Denify` instance with the specified file path for storage.
     */
    public Denify() {
        this.ui = new Ui();
        this.storage = new Storage(FILEPATH);
        try {
            this.tasks = new TaskList(storage.loadTasks());
        } catch (DenifyException e) {
            ui.displayError(e.getMessage());
            this.tasks = new TaskList();
        }
    }
    /**
     * Enumeration representing valid commands for the Denify application.
     */
    public enum Command {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND
    }
    /**
     * Main loop for processing user commands and executing corresponding actions.
     */
    public String getResponse(String msg) {
        StringBuilder response = new StringBuilder();

        Parser parser = new Parser(msg);
        try {
            if (msg.toUpperCase().startsWith(Command.BYE.name()) && parser.parseBye()) {
                response.append(ui.exit());
            } else if (msg.toUpperCase().startsWith(Command.LIST.name()) && parser.parseList()) {
                response.append(ui.showAllTasks(tasks.getTasks()));
            } else if (msg.toUpperCase().startsWith(Command.FIND.name())) {
                String description = parser.parseFind();
                response.append(ui.showFoundTasks(tasks.findTasks(description)));
            } else if (msg.toUpperCase().startsWith(Command.MARK.name())) {
                int taskIndex = parser.parseMark();
                Task t = tasks.markTask(taskIndex);
                response.append(ui.showMarkTaskMessage(t));
                tasks.saveToStorage(storage);
            } else if (msg.toUpperCase().startsWith(Command.UNMARK.name())) {
                int taskIndex = parser.parseUnmark();
                Task t = tasks.unmarkTask(taskIndex);
                response.append(ui.showUnmarkTaskMessage(t));
                tasks.saveToStorage(storage);
            } else if (msg.toUpperCase().startsWith(Command.DELETE.name())) {
                int taskIndex = parser.parseDelete();
                Task t = tasks.deleteTask(taskIndex);
                response.append(ui.showDeleteTaskMessage(t, tasks.getTasks().size()));
                tasks.saveToStorage(storage);
            } else if (msg.toUpperCase().startsWith(Denify.Command.TODO.name())) {
                Task t = parser.parseTodo();
                tasks.addTask(t);
                response.append(ui.showAddTaskMessage(t, tasks.getTasks().size()));
                tasks.saveToStorage(storage);
            } else if (msg.toUpperCase().startsWith(Command.DEADLINE.name())) {
                Task t = parser.parseDeadline();
                tasks.addTask(t);
                response.append(ui.showAddTaskMessage(t, tasks.getTasks().size()));
                tasks.saveToStorage(storage);
            } else if (msg.toUpperCase().startsWith(Command.EVENT.name())) {
                Task t = parser.parseEvent();
                tasks.addTask(t);
                response.append(ui.showAddTaskMessage(t, tasks.getTasks().size()));
                tasks.saveToStorage(storage);
            } else {
                response.append("Unable to understand the command. Please enter a valid command.");
            }
        } catch (DenifyException e) {
            response.append(e.getMessage());
        }
        return response.toString();
    }
    /**
     * Initializes the Denify application, greets the user, and starts command processing.
     */
    public void run() {
        String msg = ui.getInput();
        this.getResponse(msg);
    }
    /**
     * Main method to launch the Denify application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        new Denify().run();
    }
}