# spring-boot-mybatis-du-dbsources

Spring Boot 2.3.12 + MyBatis 2.1.4 双数据源示例。两个 `DataSource` 都不使用 `@Primary`。

自动配置 `DataSourceAutoConfiguration` 和 `MybatisAutoConfiguration` 已排除。db1、db2 各自声明数据源、`SqlSessionFactory`、`SqlSessionTemplate` 和事务管理器，Mapper 通过 `sqlSessionTemplateRef` 绑定，注入使用 `@Qualifier`。事务写在 `@Transactional(transactionManager = "db1TransactionManager")` 上。

```bash
mvn test
mvn spring-boot:run
```

示例接口：`POST /db1/notes`、`GET /db1/notes/{id}`，db2 路径相同。默认使用两个内存 H2 库，配置在 `src/main/resources/application.yml`。
