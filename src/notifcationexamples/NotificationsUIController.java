/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */

/*  
 Document looks like it specifies that we use our own states as opposed to built in states
    This is ok though. - Trevor
*/
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
    
    private boolean task1Running = false;
    private boolean task2Running = false;
    private boolean task3Running = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        notify("No tasks are currently running.");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    // The thread is stopped in a funky manner, if timed correctly you may receive
    //  another progress message on the same line as the stop message
    @FXML
    public void startTask1(ActionEvent event) {
        if ("Task 1".equals(button1.getText())) {
            button1.setText("End Task 1");
            task1 = new Task1(2147483647, 1000000, button1);
            task1.setNotificationTarget(this);
            task1.start();
        } else {                    // The task is running and the button was pressed to end it.
            button1.setText("Task 1");
            task1.setTaskState(TaskState.STOPPED);
            notify("Task 1 State: " + task1.getTaskState());
            task1.end();    
        }
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("Task1 done.")) {
            task1 = null;
        }
        textArea.appendText(message + "\n");
    }
    
    // Same as startTask1()
    @FXML
    public void startTask2(ActionEvent event) {
        if ("Task 2".equals(button2.getText())) {
            button2.setText("End Task 2");
            task2 = new Task2(2147483647, 1000000, button2);
            task2.setOnNotification((String message) -> {
                textArea.appendText(message + "\n");
            });
            task2.start();
        } else {
            button2.setText("Task 2");
            task2.setTaskState(TaskState.STOPPED);
            notify("Task 2 State: " + task2.getTaskState());
            task2.end(); 
        }                
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        if ("Task 3".equals(button3.getText())) {
            button3.setText("End Task 3");
            task3 = new Task3(2147483647, 1000000, button3);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                textArea.appendText((String)evt.getNewValue() + "\n");
            });
            task3.start();
        } else {
            button3.setText("Task 3");
            task3.setTaskState(TaskState.STOPPED);
            notify("Task 3 State: " + task3.getTaskState());
            task3.end(); 
        }
    } 
    
    @FXML
    public void displayStatus(ActionEvent event) {
        if (task1 != null) {
            notify("State of Task 1: " + task1.getTaskState());
        } else notify("Task 1 has not been started.");
        if (task2 != null) {
            notify("State of Task 2: " + task2.getTaskState());
        } else notify("Task 2 has not been started");
        if (task3 != null) {
            notify("State of Task 3: " + task3.getTaskState());
        } else notify("Task 3 has not been started");        
    }
    
}
