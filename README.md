# R2DBC and Spring Boot Example
 
This example app shows how to use R2DBC with Spring Boot, Spring Data, and Spring Security. Please read [R2DBC and Spring for Non-Blocking Database Access](https://developer.okta.com/blog/2021/05/12/spring-boot-r2dbc) to see how this app was created.

**Prerequisites:** [Java 11](https://adoptopenjdk.net/) and an [Okta Developer Account](https://developer.okta.com).

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example application, clone it.

```
git clone https://github.com/oktadev/okta-spring-boot-r2dbc-example.git
cd okta-spring-boot-r2dbc-example
```

Install the [Okta CLI](https://cli.okta.com) and run `okta register` to sign up for a new account. If you already have an account, run `okta login`. 

In the directory you cloned, run `okta apps create`. Select the default app name, or change it as you see fit. Choose **Web** and press **Enter**. Select **Okta Spring Boot Starter** to continue.

Open your IDE and view your application's configuration file at `src/main/resources/application.properties`. Make sure `MY_OKTA_DOMAIN`, `CLIENT_ID`, and `CLIENT_SECRET` are replaced with values from the Okta CLI.

```properties
okta.oauth2.issuer=https://MY_OKTA_DOMAIN.okta.com/oauth2/default
okta.oauth2.clientId=CLIENT_ID
okta.oauth2.clientSecret=CLIENT_SECRET

spring.data.r2dbc.repositories.enabled=true
spring.r2dbc.url=r2dbc:h2:mem://./testdb
```

Start the server.

```bash
./mvnw spring-boot:run
```

Open your browser and navigate to `http://localhost:8080/protected`. When the page loads you should see a streaming, constantly updating list of heartbeats.

## Links

This example uses the following open source libraries:

* [Okta Spring Boot Starter](https://github.com/okta/okta-spring-boot)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)
* [Spring Security](https://spring.io/projects/spring-security)

## Help

Please post any questions as comments on the [blog post](https://developer.okta.com/blog/2021/05/12/spring-boot-r2dbc), or visit our [Okta Developer Forums](https://devforum.okta.com/). 

## License

Apache 2.0, see [LICENSE](LICENSE).
