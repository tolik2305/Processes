package sample.classes;

import java.util.TimerTask;

public class TimerTaskCheckTime extends TimerTask {

    private Processes processes;

    public TimerTaskCheckTime(Processes processes){
        this.processes = processes;
    }

    @Override
    public void run() {
        for (Process process : processes.getList()) {
            if(process.getTypeState().equals(StateProcess.RUNNING)){
                if(process.getTime()>0) {
                    process.setTime((process.getTime() - FuncUtils.workOdds));
                    if(process.getTime() < 0){
                        process.setTime(0);
                    }
                }
            }
            if(process.getTime()<=0){
                process.setTypeState(StateProcess.TERMINATED);
                processes.findTerminatedProcesses();
            }
        }
    }
}