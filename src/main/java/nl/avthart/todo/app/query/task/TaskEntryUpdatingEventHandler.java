package nl.avthart.todo.app.query.task;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nl.avthart.todo.app.domain.task.events.TaskCompletedEvent;
import nl.avthart.todo.app.domain.task.events.TaskCreatedEvent;
import nl.avthart.todo.app.domain.task.events.TaskStarredEvent;
import nl.avthart.todo.app.domain.task.events.TaskTitleModifiedEvent;
import nl.avthart.todo.app.domain.task.events.TaskUnstarredEvent;

/**
 * @author albert
 */
@Component
public class TaskEntryUpdatingEventHandler {

	private final TaskEntryRepository taskEntryRepository;
	
	@Autowired
	public TaskEntryUpdatingEventHandler(TaskEntryRepository taskEntryRepository) {
		this.taskEntryRepository = taskEntryRepository;
	}
	
	@EventHandler
	void on(TaskCreatedEvent event) {
		TaskEntry task = new TaskEntry(event.getId(), event.getUsername(), event.getTitle(), false, false);
		taskEntryRepository.save(task);
	}

	@EventHandler
	void on(TaskCompletedEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		task.get().setCompleted(true);
		
		taskEntryRepository.save(task.get());
	}
	
	@EventHandler
	void on(TaskTitleModifiedEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		task.get().setTitle(event.getTitle());
		
		taskEntryRepository.save(task.get());
	}
	
	@EventHandler
	void on (TaskStarredEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		task.get().setStarred(true);
		
		taskEntryRepository.save(task.get());
	}
	
	@EventHandler
	void on (TaskUnstarredEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		task.get().setStarred(false);
		
		taskEntryRepository.save(task.get());
	}
}
