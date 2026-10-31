# spring-boot-mybatis-du-dbsources

Spring Boot 2.3.12 + MyBatis 2.1.4 双数据源示例。两个 `DataSource` 都不使用 `@Primary`。

数据源 1 在 `db1-jar` 里，通过 `META-INF/spring.factories` 自动装配。`db1SqlSessionFactory(DataSource)` 和 `db1SqlSessionTemplate(SqlSessionFactory)` 的参数没有 `@Qualifier`，jar 发布之后也不能改。

应用侧的数据源 2（`DataSource`、`SqlSessionFactory`、`SqlSessionTemplate`、事务管理器）全部 `autowireCandidate = false`。按类型注入时容器只看得到数据源 1，jar 里的原有参数因此仍然唯一匹配。数据源 2 由本模块的配置类用方法调用取得，Mapper 用 `sqlSessionTemplateRef = "db2SqlSessionTemplate"` 按名字绑定。

```bash
mvn test
mvn -pl db1-jar -am install -DskipTests
mvn -pl app spring-boot:run
```

示例接口：`POST /db1/notes`、`GET /db1/notes/{id}`，db2 路径相同。两个内存 H2 的地址在 `app/src/main/resources/application.yml`。
