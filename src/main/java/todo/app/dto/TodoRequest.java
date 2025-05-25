package todo.app.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TodoRequest {
	private Long id;
	private String name;
	private String description;
	private Date startTime;
	private Date endTime;
}