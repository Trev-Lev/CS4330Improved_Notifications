/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import javafx.application.Platform;
import javafx.scene.control.Button;
import notifcationexamples.TaskState;

/**
 *
 * @author dalemusser
 * 
 * This example uses an object passed in with a notify()
 * method that gets called when a notification is to occur.
 * To accomplish this the Notifiable interface is needed.
 * 
 */
public class Task1 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private Notifiable notificationTarget;
    private TaskState taskState;
    private Button button;
    
    public Task1(int maxValue, int notifyEvery, Button button)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.taskState = TaskState.STOPPED;
        this.button = button;
    }
    
    @Override
    public void run() {
        
        taskState = TaskState.RUNNING;
        doNotify("Task1 start.");
        
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task1: " + i + " State: " + taskState);
            }

            if (exit) {
                return;
            }
        }
        
        taskState = TaskState.ENDED;
        doNotify("Task1 done. State: " + taskState);
        Platform.runLater(() -> {
            button.setText("Task 1");
        });
    }
    
    public void end() {
        exit = true;
    }
    
    public void setNotificationTarget(Notifiable notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
    
    private void doNotify(String message) {
        // this provides the notification through a method on a passed in object (notificationTarget)
        if (notificationTarget != null) {
            Platform.runLater(() -> {
                notificationTarget.notify(message);
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
