# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.1/maven-plugin/reference/html/#build-image)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#boot-features-redis)

### Guides
The following guides illustrate how to use some features concretely:

* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)

## My References

https://medium.com/swlh/how-to-build-a-reactive-microservice-api-with-spring-boot-spring-webflux-and-dynamodb-using-kotlin-e1be3e99b15e

https://spring.io/guides/gs/spring-data-reactive-redis/

https://www.cnblogs.com/littleatp/p/11515458.html

https://segmentfault.com/a/1190000039133983

https://itembase.com/resources/blog/tech/spring-boot-2-best-practices-for-reactive-web-applications

https://medium.com/@cheron.antoine/reactor-java-1-how-to-create-mono-and-flux-471c505fa158

### Local DynomoDb

```shell
$ aws dynamodb --endpoint-url http://localhost:8042 create-table --table-name demo-user-info \
--attribute-definitions AttributeName=userId,AttributeType=S --key-schema AttributeName=userId,KeyType=HASH \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
```

