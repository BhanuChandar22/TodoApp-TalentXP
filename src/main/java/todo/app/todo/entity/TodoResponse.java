package todo.app.todo.entity;

import java.util.Date;

import lombok.Data;

@Data
public class TodoResponse {
	private String name;
	private String description;
	private Date startTime;
	private Date endTime;
}