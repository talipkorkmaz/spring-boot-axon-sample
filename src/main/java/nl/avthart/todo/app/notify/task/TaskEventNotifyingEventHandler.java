package nl.avthart.todo.app.notify.task;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import nl.avthart.todo.app.domain.task.events.TaskCompletedEvent;
import nl.avthart.todo.app.domain.task.events.TaskCreatedEvent;
import nl.avthart.todo.app.domain.task.events.TaskEvent;
import nl.avthart.todo.app.domain.task.events.TaskStarredEvent;
import nl.avthart.todo.app.domain.task.events.TaskTitleModifiedEvent;
import nl.avthart.todo.app.domain.task.events.TaskUnstarredEvent;
import nl.avthart.todo.app.query.task.TaskEntry;
import nl.avthart.todo.app.query.task.TaskEntryRepository;

/**
 * @author albert
 */
@Component
public class TaskEventNotifyingEventHandler {
	
	private final SimpMessageSendingOperations messagingTemplate;
	
	private final TaskEntryRepository taskEntryRepository;
	
	@Autowired
	public TaskEventNotifyingEventHandler(SimpMessageSendingOperations messagingTemplate, TaskEntryRepository taskEntryRepository) {
		this.messagingTemplate = messagingTemplate;
		this.taskEntryRepository = taskEntryRepository;
	}
	
	@EventHandler
	void on(TaskCreatedEvent event) {
		publish(event.getUsername(), event);
	}

	@EventHandler
	void on(TaskCompletedEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		publish(task.get().getUsername(), event);
	}
	
	@EventHandler
	void on(TaskTitleModifiedEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		publish(task.get().getUsername(), event);
	}
	
	@EventHandler
	void on (TaskStarredEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		publish(task.get().getUsername(), event);
	}
	
	@EventHandler
	void on (TaskUnstarredEvent event) {
		Optional<TaskEntry> task = taskEntryRepository.findById(event.getId());
		publish(task.get().getUsername(), event);
	}
	
	private void publish(String username, TaskEvent event) {
		String type = event.getClass().getSimpleName();
		this.messagingTemplate.convertAndSendToUser(username, "/queue/task-updates", new TaskEventNotification(type, event));
	}
}
