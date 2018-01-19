import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.mq.sender.impl.SenderImpl;
import com.newtouch.fbb.service.impl.RushPayImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by steven on 2018/1/19.
 */
public class RushPayServiceTest {

    RushPayImpl rushPay;

    List<CommodyInfo> infoList = new ArrayList<>();

    public void initData(){
        rushPay = new RushPayImpl();
        CommodyInfo commodyInfo = CommodyInfo.builder().comodyCode("001").number(2l).build();
        infoList.add(commodyInfo);
        CommodyInfo commodyInfo2 = CommodyInfo.builder().comodyCode("002").number(2l).build();
        infoList.add(commodyInfo2);
        rushPay.init(commodyInfo);
        rushPay.init(commodyInfo2);
    }

    @Test
    public void doTest(){
        initData();
        Long start = System.currentTimeMillis();
        List<Future> list = new ArrayList<Future>();
        ExecutorService executorService = Executors.newFixedThreadPool(200);
       for (int i=0;i<200;i++){
          list.add(executorService.submit(new DemoThread()));
       }
       list.stream().forEach(future -> {
           try {
               future.get();
           } catch (InterruptedException e) {
               e.printStackTrace();
           } catch (ExecutionException e) {
               e.printStackTrace();
           }
       });
        Long end = System.currentTimeMillis();
        System.out.println(SenderImpl.queue.size());
        System.out.println(end-start);
    }

    class DemoThread implements Runnable{

        @Override
        public void run() {
            rushPay.rush(infoList);
        }
    }
}
