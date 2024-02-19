package com.example.portatest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PortaApplication extends Application {

    int maxValue = 0;
    int minValue = 0;
    long sum = 0;
    float median = 0;
    public ArrayList<Integer> parsedList = new ArrayList<Integer>();

    public ArrayList<Integer> ascList = new ArrayList<>();
    public ArrayList<Integer> descList = new ArrayList<>();

    // launch the application
    public void start(Stage stage)
    {

        try {

            // set title for the stage
            stage.setTitle("Porta-one test app");

            // create a File chooser
            FileChooser fil_chooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");

            fil_chooser.getExtensionFilters().add(extFilter);

            // create a Label
            Label statusFileLabel = new Label("Файл не вибраний");
            String filePath = "";

            Label maxValueLabel = new Label("Максимальне число в файлі: ");
            Label minValueLabel = new Label("Мінімальне число в файлі: ");
            Label medianValueLabel = new Label("Медіана: ");
            Label middleValueLabel = new Label("Середнє арифметичне значення: ");
            Label ascValueLabel = new Label("Найбільша послідовність чисел яка збільшується: ");
            Label descValueLabel = new Label("Найбільша послідовність чисел яка зменшується: ");
            Label speedLabel = new Label("Щвидкість виконання програми в секундах: ");

            // create a Button
            Button buttonFileChoose = new Button("Виберіть файл");

            // create an Event Handler
            EventHandler<ActionEvent> event =
                    new EventHandler<ActionEvent>() {

                        public void handle(ActionEvent e)
                        {

                            // get the file selected
                            File file = fil_chooser.showOpenDialog(stage);

                            if (file != null) {

                                statusFileLabel.setText(((File) file).getAbsolutePath());

                            }
                        }
                    };

            buttonFileChoose.setOnAction(event);

            // create a Button
            Button buttonStart = new Button("Початок вираховування");

            // create an Event Handler
            EventHandler<ActionEvent> event1 =
                    new EventHandler<ActionEvent>() {

                        public void handle(ActionEvent e)
                        {
                            new Thread() {
                                String path = statusFileLabel.getText();
                                String str;

                                BufferedReader reader;

                                {
                                    try {

                                        long time = System.currentTimeMillis();

                                        reader = new BufferedReader(new FileReader(path));

                                        while((str = reader.readLine()) != null ){
                                            if(!str.isEmpty()){
                                                int number = Integer.parseInt(str);
                                                if (maxValue < number){maxValue = number;}
                                                if (minValue > number){minValue = number;}
                                                parsedList.add(number);
                                                sum += number;
                                            }
                                        }

                                        median = getMedian();
                                        ascList = getAscList(parsedList);
                                        descList = getDescList(parsedList);

                                        // update UI
                                        Platform.runLater(new Runnable() {
                                            public void run() {
                                                maxValueLabel.setText(maxValueLabel.getText() + maxValue);
                                                minValueLabel.setText(minValueLabel.getText() + minValue);

                                                long middle = sum / parsedList.size();
                                                middleValueLabel.setText(minValueLabel.getText() + middle);
                                                medianValueLabel.setText(medianValueLabel.getText() + median);
                                                ascValueLabel.setText(ascValueLabel.getText() + ascList);
                                                descValueLabel.setText(descValueLabel.getText() + descList);
                                                speedLabel.setText(String.valueOf(speedLabel.getText() + (System.currentTimeMillis() - time) / (float) 1000));
                                            }
                                        });

                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }

                            };

                        }
                    };

            buttonStart.setOnAction(event1);

            // create a VBox
            VBox vbox = new VBox(30,
                    statusFileLabel, buttonFileChoose, buttonStart,
                    maxValueLabel, minValueLabel, medianValueLabel, middleValueLabel, ascValueLabel, descValueLabel,
                    speedLabel);

            vbox.setPadding(new Insets(50));

            // set Alignment
            vbox.setAlignment(Pos.TOP_LEFT);

            // create a scene
            Scene scene = new Scene(vbox, 1200, 600);

            // set the scene
            stage.setScene(scene);

            stage.show();
        }

        catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    // Main Method
    public static void main(String args[])
    {
        // launch the application
        launch(args);
    }

    public float getMedian(){
        int[] arr = parsedList.stream().mapToInt(i -> i).toArray();
        Arrays.sort(arr);

        if (arr.length % 2 == 0){
            float median_number1 = (float) arr.length / 2;
            float median_number2 = (float) (arr.length + 2) / 2;
            return (float) (arr[Math.round(median_number1 - 1)] + arr[Math.round(median_number2 - 1)]) / 2;
        }
        float median_number = (float) (arr.length + 1) / 2;
        return arr[Math.round(median_number) - 1];
    }

    public ArrayList<Integer> getAscList(ArrayList <Integer> list){

        boolean newList = true;

        ArrayList<Integer> longListMain = new ArrayList<Integer>();
        ArrayList<Integer> longListSave = new ArrayList<Integer>();

        for(int i = 1; i < list.size(); i++){

            if (list.get(i) <= list.get(i - 1)){
                newList = true;
                if (longListSave.size() < longListMain.size()){
                    longListSave = (ArrayList<Integer>) longListMain.clone();
                }
                longListMain.clear();
            }
            else if (list.get(i) > list.get(i - 1) && newList){
                longListMain.add(list.get(i - 1));
                longListMain.add(list.get(i));
                newList = false;
            }
            else if (list.get(i) > list.get(i - 1) && !newList) {
                longListMain.add(list.get(i));
            }
        }
        if (longListSave.size() < longListMain.size()){
            longListSave = (ArrayList<Integer>) longListMain.clone();
        }

        return longListSave;

    }

    public ArrayList<Integer> getDescList(ArrayList<Integer> list){

        boolean newList = true;

        ArrayList<Integer> longListMain = new ArrayList<Integer>();
        ArrayList<Integer> longListSave = new ArrayList<Integer>();

        for(int i = 1; i < list.size(); i++){

            if (list.get(i) >= list.get(i - 1)){
                newList = true;
                if (longListSave.size() < longListMain.size()){
                    longListSave = (ArrayList<Integer>) longListMain.clone();
                }
                longListMain.clear();
            }
            else if (list.get(i) < list.get(i - 1) && newList){
                longListMain.add(list.get(i - 1));
                longListMain.add(list.get(i));
                newList = false;
            }
            else if (list.get(i) < list.get(i - 1) && !newList) {
                longListMain.add(list.get(i));
            }
        }
        if (longListSave.size() < longListMain.size()){
            longListSave = (ArrayList<Integer>) longListMain.clone();
        }

        return longListSave;

    }
}