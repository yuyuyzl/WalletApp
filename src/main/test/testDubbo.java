import buaa.jj.accountservice.Encrypt;
import buaa.jj.accountservice.api.AccountService;
import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

public class testDubbo {
    @Test
    public void TestDubbo(){
        DubboHandler.init();
        Map<String,Object> l=DubboHandler.INSTANCE.accountService.agencyInformation(1);

        try{
            int res=DubboHandler.INSTANCE.accountService.userRegister(
                    "yuyuyzl1",
                    Encrypt.SHA256("123456"),
                    "YZL",
                    "18057199133",
                    "yuzhongliang22@163.com",
                    "330106111111111112",
                    1);
        }catch (Exception e){
            e.printStackTrace();
        }

        DubboHandler.INSTANCE.accountService.userLogin("yuyuyzl",Encrypt.SHA256("123456"));

    }

}
