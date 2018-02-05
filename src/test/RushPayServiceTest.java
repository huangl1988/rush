import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.service.impl.RushPayImpl;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 2018/1/19.
 */
public class RushPayServiceTest {

    RushPayImpl rushPay;

    List<CommodyInfo> infoList = new ArrayList<>();
    int result1;
    public void initData(){
        rushPay = new RushPayImpl();
        CommodyInfo commodyInfo = CommodyInfo.builder().comodyCode("001").number(2l).build();
        infoList.add(commodyInfo);
        CommodyInfo commodyInfo2 = CommodyInfo.builder().comodyCode("002").number(2l).build();
        infoList.add(commodyInfo2);

    }
    RestTemplate restTemplate = new RestTemplate();
    @Test
    public void doTest() throws IOException {
        initData();
    }
    class DemoThread implements Runnable{



        @Override
        public void run() {

            String result=restTemplate.exchange("http://192.168.8.104:8080/test", HttpMethod.GET,null,String.class).getBody();
           if(result.equals("OK")){
               result1++;
           }
        }
    }
}
