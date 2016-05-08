package emengjzs.dacon.testng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;


@ContextConfiguration(locations = {"classpath*:*applicationContext.xml"})
// 隔离级别设置为READ_COMMITTED是为了测试中能够读取到被测代码的执行结果并进行验证
// 更改隔离级别在生产环境不被允许，仅在测试中这样做
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
// 设置事务时 需要设置 TransactionalTestExecutionListener 将test 配置为事务
@Transactional
@TestExecutionListeners(value = {
        TransactionalTestExecutionListener.class,
}, inheritListeners = true)
@Test(dataProvider = AbstractDataContextTest.DATA_CONTEXT)
public class AbstractTransactionalDataContextTest extends AbstractDataContextTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;


    public AbstractTransactionalDataContextTest() {
        // TODO Auto-generated constructor stub
    }

}
