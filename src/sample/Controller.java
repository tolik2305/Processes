package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.Controllers.InputPriority;
import sample.classes.*;
import sample.classes.Process;

import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    private static ObservableList<Process> list = FXCollections.observableArrayList();
    private static ObservableList<Process> listConfirmed = FXCollections.observableArrayList();
    private static ObservableList<Process> listRejected = FXCollections.observableArrayList();
    private static ObservableList<MemoryBlock> listMemory = FXCollections.observableArrayList();

    @FXML
    private TextField txtMemory;

    @FXML
    private TextField txtQuantityInPending;

    @FXML
    private ChoiceBox choiceSpeed;

    @FXML
    private TextField txtQuantityRejectedProcesses;

    @FXML
    private TextField txtQuantityTerminatedProcesses;

    @FXML
    private TextField txtQuantityConfirmedProcesses;

    @FXML
    private TextField txtQuantityProcesses;

    @FXML
    private ProgressBar progressBarMemory;

    @FXML
    private TableView<Process> tableProcesses;

    @FXML
    private TableColumn<Process, Integer> idColumn;

    @FXML
    private TableColumn<Process, String> nameColumn;

    @FXML
    private TableColumn<Process, Integer> timeOfTactsColumn;

    @FXML
    private TableColumn<Process, Integer> timeInColumn;

    @FXML
    private TableColumn<Process, Integer> timeColumn;

    @FXML
    private TableColumn<Process, String> stateColumn;

    @FXML
    private TableColumn<Process, Integer> priorityColumn;

    @FXML
    private TableColumn<Process, Integer> sizeColumn;

    @FXML
    private TableView<Process> tableConfirmedProcesses;

    @FXML
    private TableColumn<Process, Integer> idConfirmedColumn;

    @FXML
    private TableColumn<Process, String> nameConfirmedColumn;

    @FXML
    private TableColumn<Process, Integer> timeOfTactsConfirmedColumn;

    @FXML
    private TableColumn<Process, Integer> timeInConfirmedColumn;

    @FXML
    private TableColumn<Process, String> stateConfirmedColumn;

    @FXML
    private TableColumn<Process, Integer> priorityConfirmedColumn;

    @FXML
    private TableColumn<Process, Integer> sizeConfirmedColumn;

    @FXML
    private TableView<Process> tableRejectedProcesses;

    @FXML
    private TableColumn<Process, Integer> idRejectedColumn;

    @FXML
    private TableColumn<Process, String> nameRejectedColumn;

    @FXML
    private TableColumn<Process, String> stateRejectedColumn;

    @FXML
    private TableColumn<Process, Integer> sizeRejectedColumn;

    @FXML
    private TableView<MemoryBlock> tableMemoryBlocks;

    @FXML
    private TableColumn<MemoryBlock, Integer> startMemoryBlockColumn;

    @FXML
    private TableColumn<MemoryBlock, Integer> endMemoryBlockColumn;

    private Process selectedItem = null;

    private static Scheduler scheduler = new Scheduler();
    private static Processes processes = new Processes(scheduler);

    public void initialize(){
        idConfirmedColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("id"));
        nameConfirmedColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("name"));
        timeOfTactsConfirmedColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("timeOfTacts"));
        timeInConfirmedColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("timeIn"));
        stateConfirmedColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("typeState"));
        priorityConfirmedColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("priority"));
        sizeConfirmedColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("size"));

        idRejectedColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("id"));
        nameRejectedColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("name"));
        stateRejectedColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("typeState"));
        sizeRejectedColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("size"));

        idColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("name"));
        timeOfTactsColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("timeOfTacts"));
        timeInColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("timeIn"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("time"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("typeState"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("priority"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("size"));

        startMemoryBlockColumn.setCellValueFactory(new PropertyValueFactory<MemoryBlock, Integer>("start"));
        endMemoryBlockColumn.setCellValueFactory(new PropertyValueFactory<MemoryBlock, Integer>("end"));

        scheduler.add(new MemoryBlock(0,0,112));
        scheduler.add(new MemoryBlock(0,767,823));
        scheduler.add(new MemoryBlock(0,1330,1510));
        scheduler.add(new MemoryBlock(0, FuncUtils.maxMemorySize-148, FuncUtils.maxMemorySize));

        listMemory.addAll(scheduler.getMemoryBlocks());
        list.addAll(processes.getList());

        tableProcesses.setItems(list);
        tableConfirmedProcesses.setItems(listConfirmed);
        tableRejectedProcesses.setItems(listRejected);
        tableMemoryBlocks.setItems(listMemory);



        ObservableList<String> speed = FXCollections.observableArrayList("x1", "x2", "x4");
        choiceSpeed.setItems(speed);
        choiceSpeed.setValue("x1");
        choiceSpeed.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("x1")){
                    FuncUtils.workOdds = 1;
                } else if(newValue.equals("x2")){
                    FuncUtils.workOdds = 2;
                } else if(newValue.equals("x4")){
                    FuncUtils.workOdds = 4;
                }
            }
        });

        listMemory.addListener(new ListChangeListener<MemoryBlock>() {
            @Override
            public void onChanged(Change<? extends MemoryBlock> c) {
                int memory = 0;
                for (MemoryBlock memoryBlock : scheduler.getMemoryBlocks()) {
                    memory += memoryBlock.getEnd() - memoryBlock.getStart();
                }
                float progressInMemory = 1 / 2048f;
                double result = progressInMemory * memory;
                progressBarMemory.setProgress(result);
            }
        });

        new Thread() {
            ClassExecutingTask classExecutingTask = new ClassExecutingTask(processes);
        };

        new Thread() {
            ClassExecutingWork classExecutingWork = new ClassExecutingWork(processes);
        };

        new Thread() {
            ClassExecutingCheckTime classExecutingCheckTime = new ClassExecutingCheckTime(processes);
        };

        new Thread() {
            ClassExecutingCheck classExecutingCheck = new ClassExecutingCheck(processes);
        };

        new Thread() {
            ClassExecutingRefreshData classExecutingRefreshData = new ClassExecutingRefreshData();
        };

        Statistics();

        tableProcesses.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedItem = tableProcesses.getSelectionModel().getSelectedItem();
            }
        });
    }

    public static void Refresh(){
        if(!list.isEmpty()){
            list.clear();
            list.addAll(processes.getList());
        } else {
            list.addAll(processes.getList());
        }

        listMemory.clear();
        listMemory.addAll(scheduler.getMemoryBlocks());
        if(listConfirmed!=null) {
            listConfirmed.clear();
        }
        for (Process process : processes.getList()) {
            if (process.getTypeState().equals(StateProcess.READY) || process.getTypeState().equals(StateProcess.RUNNING) || process.getTypeState().equals(StateProcess.TERMINATED)) {
                listConfirmed.add(process);
            }
        }
        if(listRejected.size()!=0) {
            listRejected.clear();
        }
        for (Process process : processes.getList()) {
            if (process.getTypeState().equals(StateProcess.REJECTED)) {
                listRejected.add(process);
            }
        }
    }

    public void changePriority(ActionEvent actionEvent) {
        Main.InputPriority();
        if(!InputPriority.isCancel) {
            int priority = InputPriority.priority;
            if(selectedItem!=null) {
                processes.ChangePriority(selectedItem.getId(), priority);
            }
        }
    }

    public void Statistics() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!processes.getList().isEmpty()) {
                    int quantityInPending = 0;
                    int quantityTerminated = 0;
                    int memory = 0;
                    for (Process process:processes.getList()) {
                        if(process.getTypeState().equals(StateProcess.WAITING)){
                            quantityInPending++;
                        } else if(process.getTypeState().equals(StateProcess.TERMINATED)){
                            quantityTerminated++;
                        }
                    }

                    for (MemoryBlock memoryBlock:scheduler.getMemoryBlocks()) {
                        memory += memoryBlock.getEnd() - memoryBlock.getStart();
                    }
                    txtQuantityProcesses.setText(String.valueOf(processes.getList().size()));
                    txtQuantityRejectedProcesses.setText(String.valueOf(listRejected.size()));
                    txtQuantityConfirmedProcesses.setText(String.valueOf(listConfirmed.size()));
                    txtQuantityTerminatedProcesses.setText(String.valueOf(quantityTerminated));
                    txtQuantityInPending.setText(String.valueOf(quantityInPending));
                    txtMemory.setText(memory+" Мб");
                }
            }
        };
        Timer timer = new Timer("Timer");
        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(timerTask, delay, period);
    }
}
