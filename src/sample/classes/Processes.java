package sample.classes;

import sample.Controller;

import java.util.ArrayList;
import java.util.Comparator;

public class Processes{
    private Scheduler scheduler;

    public ArrayList<Process> getList() {
        return list;
    }

    private ArrayList<Process> list;
    private Queue queue;

    public Processes(Scheduler scheduler){
        this.list = new ArrayList<>();
        this.scheduler = scheduler;
    }

    public void CheckByPriority(){
        Process runningProcess = null;

        for (Process process:getList()) {
            if(process.getTypeState().equals(StateProcess.RUNNING)){
                runningProcess = process;
            }

            if(runningProcess!=null) {
                if (process.getId() < runningProcess.getId()) {
                    getList().get(getList().indexOf(runningProcess)).setTypeState(StateProcess.WAITING);
                    try {
                        toWork(process);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Process processMinPriority(){
        int minPriority=31;
        Process currentProcess = null;
        for (Process process:getList()) {
            if(process.getPriority()<minPriority&&!process.getTypeState().equals(StateProcess.REJECTED)&&!process.getTypeState().equals(StateProcess.TERMINATED)){
                minPriority=process.getPriority();
                currentProcess = process;
            }
        }
    return currentProcess;
    }


    public void Work() throws InterruptedException {
        Process currentProcess = processMinPriority();
        queue = new Queue(scheduler);
        if(currentProcess!=null) {
            if (queue.add(currentProcess)) {
                toWork(currentProcess);
                queue.addConfirmedProcess(currentProcess);
            } else {
                getList().get(getList().indexOf(currentProcess)).setTypeState(StateProcess.REJECTED);
                queue.addRejectedProcess(currentProcess);
            }
        }
    }

    private void toWork(Process process) throws InterruptedException {
        getList().get(getList().indexOf(process)).setTypeState(StateProcess.RUNNING);
        MemoryBlock memoryBlock = new MemoryBlock(process.getId(), queue.getStart() + 1, queue.getStart() + process.getSize()+1);
        scheduler.add(memoryBlock);
        scheduler.getMemoryBlocks().sort(MemoryBlock.byAsc);
        Controller.Refresh();
        Thread.sleep(process.getTime()*100);
        process.setTypeState(StateProcess.TERMINATED);
        scheduler.deleteBlock(memoryBlock);
        Controller.Refresh();
    }
}
