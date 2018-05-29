import buaa.jj.accountservice.api.AccountService;
import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

public class testDubbo {
    @Test
    public void TestDubbo(){
        Map<String,Object> l=DubboHandler.INSTANCE.accountService.agencyInformation(1);
        System.out.println(l);

    }

}
