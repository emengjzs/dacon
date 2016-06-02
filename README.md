# dacon
A data-driven emhancement for test framework TestNG, optimized for RESTful Web service testing
* * *

### Usage
Take a breif view of the lib by an example.
Create a file named **dataContext.json** and input:
```json
{
  "app": {
    "id": 1212,
    "name": "3434"
  }
}
```


Then, we can use the data directly from the json file in test.
```java
public class ObjectTest extends AbstractDataContextTest {
    public void testDataContextWithJSON1(@Data("app") JSONObject req) {
        Assert.assertEquals(req.get("id"), 1212);
        Assert.assertEquals(req.get("name"), "3434");
    }

    public void testDataContextWithBean1(@Data("app") TestObject req) {
        Assert.assertEquals(req.getId(), 1212);
    }
    static class TestObject {
        private int id;
        private String name;

        public int getId()               { return id;        }
        public void setId(int id)        { this.id = id;     }
        public String getName()          { return name;      }
        public void setName(String name) { this.name = name; }
    }
}
```

### Features
- Static test data injection with json + vm templates and annotations in tests.
- Properties mapping.
- Dynamic test data injection. (data provider that integrated with Spring)
- Static configuration declarations in json type other than xml.
- A very simple annotation driven model to build cli apps integrated with Spring.
