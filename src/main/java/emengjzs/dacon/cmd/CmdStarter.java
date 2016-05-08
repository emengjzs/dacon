package emengjzs.dacon.cmd;

import emengjzs.dacon.cmd.command.CommandRouter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdStarter {

    public static void start(String contextName) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextName);
        CommandRouter router = context.getBean(CommandRouter.class);
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str;
            try {
                if (!StringUtils.isEmpty(str = br.readLine())) {
                    int code = router.action(("-" + str).split(" "));
                    if (code == -1) {
                        context.close();
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

}
