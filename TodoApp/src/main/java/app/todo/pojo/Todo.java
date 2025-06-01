package app.todo.pojo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Todo {
	private int id;
	private String name;
	private String description;
	private Timestamp startTime;
	private Timestamp endTime;
}
