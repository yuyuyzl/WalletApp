import buaa.jj.accountservice.Encrypt;
import buaa.jj.accountservice.api.AccountService;
import com.yuyuyzl.WalletApp.Dubbo.DubboHandler;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Map;

public class testDubbo {
    @Test
    public void TestDubbo(){
        DubboHandler.init();
        Map<String,Object> l=DubboHandler.INSTANCE.accountService.agencyInformation(1);
        /*
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
*/

        System.out.println(DubboHandler.INSTANCE.accountService.foundPasswd("1","11",""));
        DubboHandler.INSTANCE.accountService.userLogin("yuyuyzl",Encrypt.SHA256("123456"));
        System.out.println(DubboHandler.INSTANCE.accountService.getID("yuyuyzl",true));
        List<Map<String, String>> m=DubboHandler.INSTANCE.accountService.userTradeInformation(9,"2018/5/1","2018/6/3",2);
        System.out.println("PAUSE HERE");
    }
}
