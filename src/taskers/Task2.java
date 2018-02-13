/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Button;
import notifcationexamples.TaskState;

/**
 *
 * @author dalemusser
 * 
 * This example uses a Notification functional interface.
 * This allows the use of anonymous inner classes or
 * lambda expressions to define the method that gets called
 * when a notification is to be made.
 */
public class Task2 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private ArrayList<Notification> notifications = new ArrayList<>();
    private TaskState taskState;
    private Button button;
    
    public Task2(int maxValue, int notifyEvery, Button button)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.taskState = TaskState.STOPPED;
        this.button = button;
    }
    
    @Override
    public void run() {
        taskState = TaskState.RUNNING;
        doNotify("Started Task2!");
        
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task2: " + i + " State: " + taskState);
            }
            
            if (exit) {
                return;
            }
        }
        taskState = TaskState.ENDED;
        doNotify("Task2 done. State: " + taskState);
        Platform.runLater(() -> {
            button.setText("Task 2");
        });
    }
    
    public void end() {
        exit = true;
    }
    
    // this method allows a notification handler to be registered to receive notifications
    public void setOnNotification(Notification notification) {
        this.notifications.add(notification);
    }
    
    private void doNotify(String message) {
        // this provides the notification through the registered notification handler
        for (Notification notification : notifications) {
            Platform.runLater(() -> {
                notification.handle(message);
            });
        }
    }
    
    public TaskState getTaskState() {
        return this.taskState;
    }
    
    public void setTaskState(TaskState state) {
        this.taskState = state;
    }
}