package todoapp.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import todoapp.core.todos.application.TodoEditor;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;

@RestController
@RequestMapping("/api/todos")
public class TodoRestController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final TodoFinder finder;
	private final TodoEditor editor;
	
	public TodoRestController(TodoFinder finder, TodoEditor editor) {
		this.finder = finder;
		this.editor = editor;
	}

	@GetMapping
	public List<Todo> list() {
		return finder.getAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody @Valid WriteTodoCommand command) {
		logger.debug("request command: {}", command);
	
		editor.create(command.getTitle());
	}
	
	@PutMapping("/{id}")
	public void update(@PathVariable("id") Long id, @RequestBody @Valid WriteTodoCommand command) {
		logger.debug("request update id : {}, command: {}", id, command);
		
		editor.update(id, command.getTitle(), command.isCompleted());
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.debug("delete todo, id : {}",id);
		
		editor.delete(id);
	}
	
	
	static class WriteTodoCommand{
		
		@NotBlank
		@Size(min = 4, max = 140)
		private String title;
		private boolean completed;

		public String getTitle() {
			return title;
		}
		
		

		public void setTitle(String title) {
			this.title = title;
		}
		
		

		public boolean isCompleted() {
			return completed;
		}



		public void setCompleted(boolean completed) {
			this.completed = completed;
		}



		@Override
		public String toString() {
			return String.format("[title=%s, completed=%s]", title, completed);
		}
		
		
	}
}
 