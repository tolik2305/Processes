package sample.classes;

import java.util.Date;
import java.util.Timer;

public class ClassExecutingCheckTime {

    public ClassExecutingCheckTime(Processes processes){
        CheckTime(processes);
    }

    public void CheckTime(Processes processes){
        Timer timer = new Timer("Check time");
        TimerTaskCheckTime timerTaskCheckTime = new TimerTaskCheckTime(processes);
        Date date = new Date();
        long delay = 1 * 1000;
        timer.scheduleAtFixedRate(timerTaskCheckTime, date, delay);
    }

}