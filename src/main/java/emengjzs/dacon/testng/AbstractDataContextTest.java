package emengjzs.dacon.testng;

import emengjzs.dacon.context.DataContext;
import emengjzs.dacon.context.DataContextDataProvider;
import emengjzs.dacon.context.DynamicDataProviderProcessor;
import emengjzs.dacon.http.BasicHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TestNG 娴嬭瘯鏁版嵁娉ㄨВ澹版槑鏀寔test鍩虹被 缁ф壙姝ょ被鍚庡彲浠ヤ娇鐢ㄥ０鏄庡紡鐨勬祴璇曟暟鎹瀯閫� 绠�鍖栨暟鎹瀯閫犺繃绋� 鎻愪緵post http璁块棶,baseDao 鏁版嵁搴撴煡璇�, log 鏃ュ織, 浠ュ強DataContext鐨勭被浣跨敤 榛樿閰嶇疆浜嬪姟涓嶅洖婊�
 *
 * @author jiaozishun
 */
@ContextConfiguration(locations = {"classpath*:*applicationContext.xml"})
// 闅旂绾у埆璁剧疆涓篟EAD_COMMITTED鏄负浜嗘祴璇曚腑鑳藉璇诲彇鍒拌娴嬩唬鐮佺殑鎵ц缁撴灉骞惰繘琛岄獙璇�
// 鏇存敼闅旂绾у埆鍦ㄧ敓浜х幆澧冧笉琚厑璁革紝浠呭湪娴嬭瘯涓繖鏍峰仛
@TestExecutionListeners(value = {}, inheritListeners = true)
@Test(dataProvider = AbstractDataContextTest.DATA_CONTEXT)
public class AbstractDataContextTest extends AbstractTestNGSpringContextTests {

    public static final String DATA_CONTEXT = "dataContext";
    protected static final String RESULT = "result";
    protected static final String SUCCESS = "success";
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected DataContext dataContext;

    @Autowired(required = false)
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected BasicHttpService httpService;

    @Autowired
    private DataContextDataProvider dataProvider;

    @Autowired
    private DynamicDataProviderProcessor dynamicDataProviderProcessor;


    @DataProvider(name = DATA_CONTEXT)
    protected final Object[][] getData(Method m) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {

        return dataProvider.getData(m);
    }

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "springTestContextBeforeTestMethod")
    protected final void dynamicDataInjected(Method m, Object[] params) {
        dynamicDataProviderProcessor.process(m, params);
    }

}
