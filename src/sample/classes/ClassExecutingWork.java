package sample.classes;

import java.util.Date;
import java.util.Timer;

public class ClassExecutingWork {

    private Processes processes;

    public ClassExecutingWork(Processes processes){
        this.processes = processes;
        this.Work(processes);
    }

    public void Work(Processes processes) {
        Timer timer = new Timer("Work");
        TimerTaskWork timerTaskWork = new TimerTaskWork(this.processes);
        Date date = new Date();
        long delay = 1 * 1000;
        timer.scheduleAtFixedRate(timerTaskWork, date, delay);
    }
}