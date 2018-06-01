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
                    "yuyutest",
                    Encrypt.SHA256("123456"),
                    "余钟亮",
                    "18057199133",
                    "yuzhongliang22@163.com",
                    "330106111111111117",
                    1);
        }catch (Exception e){
            e.printStackTrace();
        }

        DubboHandler.INSTANCE.accountService.userLogin("yuyuyzl",Encrypt.SHA256("123456"));
        System.out.println(DubboHandler.INSTANCE.accountService.getID("yuyuyzl",true));
        System.out.println(DubboHandler.INSTANCE.accountService.transferConsume(9,11,99999,false));
    }

}
