# Spring Cloud Config Server

This library is an extension to [Spring Cloud Config](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html),
which uses Redis as a storage solution for configuration properties, instead of the default Git repository.

## Disclaimer
The library is not yet released.

## Usage

### Java configuration

In order to use this library you need to replace usage of the traditional ```@CloudConfigServer``` with ```@CloudConfigServerRedis```:

```java
import com.github.gabrielruiu.spring.cloud.config.redis.EnableConfigServerRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableConfigServerRedis
@SpringBootApplication
public class CentralizedConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralizedConfigServerApplication.class, args);
	}
}
```

### Property representation in Redis

In order for the Redis Config server to properly identify the properties within Redis, these need to have the following
format:

```
application-name:profile:label:property-name
```


For example, we have the following keys in Redis:
```
127.0.0.1:6379> set my-app:default:master:url "http://my-app.com"
OK
127.0.0.1:6379> set my-app:h2-db:master:spring:datasource:name "my-app-database"
OK
127.0.0.1:6379> set my-app:h2-db:master:spring:datasource:password "1nuron13037"
OK
127.0.0.1:6379> set my-app:h2-db:master:url "http://dev.my-app.com"
OK
```


And we perform the following ```cURL```:
```
curl -X GET "http://localhost:8080/my-app/h2-db"
```

which would return the following response:
```javascript
{
  "name": "my-app",
  "profiles": [
    "h2-db"
  ],
  "label": "master",
  "version": null,
  "propertySources": [
    {
      "name": "my-app-h2-db",
      "source": {
        "spring.datasource.name": "my-app-database",
        "url": "http://dev.my-app.com",
        "spring.datasource.password": "1nuron13037"
      }
    },
    {
      "name": "my-app",
      "source": {
        "url": "http://my-app.com"
      }
    }
  ]
}
```



## Notes

* There is no support for versioned properties, and therefore, all responses returned by the Redis config server
will contain a ```null``` version.

