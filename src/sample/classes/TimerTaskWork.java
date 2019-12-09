package sample.classes;

import java.util.TimerTask;

public class TimerTaskWork extends TimerTask {

    private Processes processes;

    public TimerTaskWork(Processes processes){
        this.processes = processes;
        this.run();
    }

    @Override
    public void run() {
        processes.Work();
    }
}