package net.sf.persism.gui;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import net.sf.persism.Session;
import net.sf.persism.perf.PerfTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static net.sf.persism.Parameters.params;
import static net.sf.persism.SQL.where;

public class PollingService extends ScheduledService<List<PerfTest>> {

    private Session session;
    private AtomicInteger lastId = new AtomicInteger(0);

    public PollingService(Session session) {
        this.session = session;
    }

    public void startService() {
        if (!isRunning()) {
            //...
            reset();
            start();
        }

    }
    // https://stackoverflow.com/questions/27853735/simple-example-for-scheduledservice-in-javafx

    @Override
    protected Task<List<PerfTest>> createTask() {
        return new Task<>() {
            List<PerfTest> list;
            @Override
            protected List<PerfTest> call() throws Exception {
                list = session.query(PerfTest.class, where("id > ? order by id"), params(lastId.get()));
                if (list.size() > 0) {
                    lastId.set(list.get(list.size() - 1).getId());
                }
                return list;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                System.out.println("notify ! " + list.size());
            }
        };
    }
}
