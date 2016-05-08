package dacon.test.test;

import com.alibaba.fastjson.JSONObject;
import emengjzs.dacon.annotation.Data;
import emengjzs.dacon.testng.AbstractDataContextTest;
import org.testng.Assert;

/**
 * Created by emengjzs on 2016/4/15.
 */

public class ObjectTest extends AbstractDataContextTest {


    public void testDataContextWithJSON1(@Data("app") JSONObject req) {
        Assert.assertEquals(req.get("id"), 1212);
    }

    public void testDataContextWithJSON2(@Data("app") JSONObject req) {
        Assert.assertEquals(req.get("name"), "3434");
    }

    public void testDataContextWithBean1(@Data("app") TestObject req) {
        Assert.assertEquals(req.getId(), 1212);
    }

    @Data("app")
    public void testDataContextWithBean2(TestObject req) {
        Assert.assertEquals(req.getId(), 1212);
    }

    static class TestObject {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
