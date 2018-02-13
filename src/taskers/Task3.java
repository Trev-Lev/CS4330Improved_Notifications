/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.scene.control.Button;
import notifcationexamples.TaskState;

/**
 *
 * @author dalemusser
 * 
 * This example uses PropertyChangeSupport to implement
 * property change listeners.
 * 
 */
public class Task3 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private TaskState taskState;
    private Button button;
    
    public Task3(int maxValue, int notifyEvery, Button button)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.taskState = TaskState.STOPPED;
        this.button = button;
    }
    
    @Override
    public void run() {
        taskState = taskState.RUNNING;
        doNotify("Task3 start.");
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("It happened in Task3: " + i + " State: " + taskState);
            }
            
            if (exit) {
                return;
            }
        }
        taskState = TaskState.ENDED;
        doNotify("Task3 done. State: " + taskState);
        Platform.runLater(() -> {
            button.setText("Task 3");
        });
    }
    
    public void end() {
        exit = true;
    }
    
    // the following two methods allow property change listeners to be added
    // and removed
    public void addPropertyChangeListener(PropertyChangeListener listener) {
         pcs.addPropertyChangeListener(listener);
     }

     public void removePropertyChangeListener(PropertyChangeListener listener) {
         pcs.removePropertyChangeListener(listener);
     }
    
    private void doNotify(String message) {
        // this provides the notification through the property change listener
        Platform.runLater(() -> {
            // I'm choosing not to send the old value (second param).  Sending "" instead.
            pcs.firePropertyChange("message", "", message);
        });
    }
    
    public TaskState getTaskState() {
        return this.taskState;
    }
    
    public void setTaskState(TaskState state) {
        this.taskState = state;
    }
}