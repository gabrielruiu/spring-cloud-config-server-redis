# Spring Cloud Config Server

This library is an extension to [Spring Cloud Config](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html),
which uses Redis as a storage solution for configuration properties, instead of the default Git repository.

## Disclaimer
The library is not yet released.

## Usage

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

## Notes

* There is no support for versioned properties, and therefor, all responses returned by the Redis config server
will contain a ```null``` version.

