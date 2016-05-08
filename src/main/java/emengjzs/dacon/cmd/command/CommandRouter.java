package emengjzs.dacon.cmd.command;

import com.alibaba.fastjson.JSON;
import emengjzs.dacon.util.TypeConvertService;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class CommandRouter {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ApplicationContext ac;

    @Autowired
    CommandResolve cr;


    ConversionService convertService;

    public CommandRouter() {
        convertService = TypeConvertService.getInstance();
    }

    public int action(String[] args) throws ParseException {
        CommandLine cl;
        DefaultParser parser = new DefaultParser();
        try {
            cl = parser.parse(cr.getCommands(), args);
        } catch (UnrecognizedOptionException e) {
            logger.debug(e.getMessage());
            return 0;
        }
        logger.debug("Command Input -{}", JSON.toJSONString(cl, true));
        if (cl.hasOption('h')) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", cr.getCommands());
            return 0;
        } else if (cl.hasOption("exit")) {
            return -1;
        } else {
            action(cl);
        }
        return 0;
    }

    public void action(CommandLine cl) {
        String command = cl.getOptions()[0].getOpt();
        logger.debug("Command - {}", command);
        CommandMapper.MethodMapper mapper = cr.getMappedMethod(command);

        if (mapper == null) {
            logger.error("Command action " + command + " not found!");
            throw new ActionNotFoundException(command);
        }

        logger.debug(JSON.toJSONString(mapper));
        Object action = ac.getBean(mapper.beanName);
        Method m = mapper.m;
        int len = mapper.param.length;
        Object[] params = new Object[len];
        for (int i = 0; i < len; i++) {

            String p = cl.getOptionValue(mapper.param[i].name);
            Class<?> clazz = mapper.param[i].type;
            Object o;

            if (this.convertService.canConvert(String.class, clazz)) {
                o = this.convertService.convert(p, clazz);
            } else {
                o = p;
            }
            params[i] = o;
        }
        try {
            logger.debug("{}, {}", m, JSON.toJSONString(params));
            m.invoke(action, params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
