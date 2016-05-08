package emengjzs.dacon.cmd.command;

import emengjzs.dacon.cmd.annotation.CommandController;
import emengjzs.dacon.cmd.annotation.CommandMapping;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

public class CommandActionBeanProcessor implements BeanPostProcessor, CommandResolve {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    CommandMapper commandMapper;

    public CommandActionBeanProcessor() {
        commandMapper = new CommandMapper();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // TODO Auto-generated method stub
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(CommandController.class)) {
            Class<?> beanClass = bean.getClass();
            logger.debug("Process @CommandController bean {} ", beanClass.getSimpleName());
            for (Method method : bean.getClass().getMethods()) {
                processMethod(beanClass, bean, beanName, method);
            }

        }
        return bean;
    }

    private void processMethod(Class<?> beanClass, Object bean, String beanName, Method method) {
        CommandMapping mapping = method.getAnnotation(CommandMapping.class);
        if (mapping != null) {
            logger.debug("Process mapping {}, {}, {}, {}", beanClass.getSimpleName(), bean, beanName, method.getName());
            commandMapper.regist(mapping.value()[0], beanName, method, mapping.description());
        }
    }

    @Override
    public CommandMapper.MethodMapper getMappedMethod(String name) {

        return this.commandMapper.get(name);
    }

    @Override
    public Options getCommands() {
        return this.commandMapper.getOptions();
    }
}
