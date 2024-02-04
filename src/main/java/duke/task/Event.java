package duke.task;

import duke.exception.DukeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Event extends Task {

    protected String from;
    protected String to;
    protected DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    protected DateTimeFormatter output = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public Event(String description, String from, String to) throws DukeException {
        super(description);
        try {
            LocalDateTime date1 = LocalDateTime.parse(from, input);
            LocalDateTime end1 = LocalDateTime.parse(to, input);
            this.from = date1.format(output);
            this.to = end1.format(output);

            if (!isBeforeDateTime(date1, end1)) {
                throw new DukeException("Invalid date range. 'From' should be before 'To'.");
            }

        } catch (DateTimeParseException | StringIndexOutOfBoundsException e) {
            throw new DukeException("Invalid time format. Please use yyyy-MM-dd HH:mm.");
        }
    }

    private boolean isBeforeDateTime(LocalDateTime from, LocalDateTime to) {
        return from.isBefore(to) || from.isEqual(to);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }

    @Override
    public String toFileString() {
        return "E" + super.toFileString() + " | " + LocalDateTime.parse(this.from, output).format(input) + " - "
                + LocalDateTime.parse(this.to, output).format(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Event event = (Event) obj;
        return this.isDone == event.isDone
                && this.description.equals(event.description)
                && this.from.equals(event.from)
                && this.to.equals(event.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.description, this.isDone, this.from, this.to);
    }
}
