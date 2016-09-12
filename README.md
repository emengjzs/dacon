# dacon
A data-driven emhancement library for test framework TestNG, optimized for RESTful Web service testing
* * *

### Usage
Take a breif view of the library by an example.
Create a file named **dataContext.json** and input as follows:
```json
{
  ## test data
  "app": {
    "id": 1212,
    "name": "3434"
  }
}
```
Import the bean difinition resources in your custom applicationContext file:
```xml
<import resource="classpath*:dacon-applicationContext-part.xml"/>
```
Then, we can use the data directly from the json file in our test.
```java
/** It's better to use lombok to simplify the POJO definition**/
class TestObject {
    private int id;
    private String name;

    public int getId()               { return id;        }
    public void setId(int id)        { this.id = id;     }
    public String getName()          { return name;      }
    public void setName(String name) { this.name = name; }
 }
```


```java
public class ObjectTest extends AbstractDataContextTest {

    /** Inject test data into the test method**/
    public void testDataContextWithJSON1(@Data("app") JSONObject req) {
        Assert.assertEquals(req.get("id"), 1212);
        Assert.assertEquals(req.get("name"), "3434");
    }

    /** Automatically tansform to POJO object**/
    public void testDataContextWithBean1(@Data("app") TestObject req) {
        Assert.assertEquals(req.getId(), 1212);
    }
    
}
```

### Features
- Static test data injection with json + vm templates and annotations in tests.
- Properties mapping.
- Dynamic test data injection. (data provider that integrated with Spring)
- Static configuration declarations in json type other than xml.
- A very simple annotation driven model to build cli apps integrated with Spring.

### Requirement
- Java 1.7 +
- Spring 3.0 +