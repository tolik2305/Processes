package sample.classes;

import sample.Controller;

import java.util.TimerTask;

public class TimerTaskRefreshData extends TimerTask{

    @Override
    public void run() {
        Controller.Refresh();
    }
}