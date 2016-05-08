package emengjzs.dacon.cmd.command;

import com.alibaba.fastjson.JSON;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandMapper {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, MethodMapper> mapper = new ConcurrentHashMap<String, MethodMapper>();
    private Options opts = new Options();
    private LocalVariableTableParameterNameDiscoverer paramNameFinder = new LocalVariableTableParameterNameDiscoverer();

    CommandMapper() {
        opts.addOption("exit", false, "exit the program.");
        opts.addOption("h", "help.");
    }

    public MethodMapper get(String name) {
        return mapper.get(name);
    }

    public Options getOptions() {
        return opts;
    }

    // 不支持重写方法
    public void regist(String value, String beanName, Method method, String description) {
        int paramLength = method.getParameterTypes().length;
        String[] paramNames = paramNameFinder.getParameterNames(method);
        Class[] paramTypes = method.getParameterTypes();
        MethodMapper methodMapper = new MethodMapper(beanName, paramLength);
        logger.debug("Add command {} - {}", value, description);
        opts.addOption(value, false, description);
        methodMapper.m = method;
        for (int i = 0; i < paramLength; ++i) {
            methodMapper.addParam(i, paramNames[i], paramTypes[i]);
            Class<?> cmdType = paramTypes[i].isArray() ? String[].class : String.class;
            logger.debug("Add param {}", paramNames[i]);
            Option opt = Option.builder(paramNames[i])
                    .hasArg(true)
                    .desc("")
                    // 这里参数分割符完全不起作用 todo
                    // , 将由ConvertService类型转换时分割
                    .valueSeparator(',')
                    .type(cmdType)
                    .build();
            opts.addOption(opt);
        }

        mapper.put(value, methodMapper);

    }

    public String toString() {
        return JSON.toJSONString(mapper, true);
    }

    class Parameter {
        int index;
        String name;
        Class<?> type;

        public Parameter(int index, String name, Class<?> type) {
            super();
            this.index = index;
            this.name = name;
            this.type = type;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }
    }

    class MethodMapper {
        Method m;
        String beanName;
        Parameter[] param;

        public MethodMapper(String beanName, int len) {
            this.beanName = beanName;
            param = new Parameter[len];
        }

        public Method getM() {
            return m;
        }

        public void setM(Method m) {
            this.m = m;
        }

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public Parameter[] getParam() {
            return param;
        }

        public void setParam(Parameter[] param) {
            this.param = param;
        }

        public void addParam(int index, String name, Class<?> type) {
            param[index] = new Parameter(index, name, type);
        }

    }
}
