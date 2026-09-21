# 自定义接口

Controller: `spider-web/.../controller/CustomController.java`。
路径前缀沿用 `api.prefix`，默认 `/spider`。所有接口都使用 POST 表单参数（`application/x-www-form-urlencoded`），沿用项目现有认证过滤器和配置。

## Prometheus 转发

```sh
curl -X POST 'http://localhost:8080/spider/prometheus' \
  --data-urlencode 'prompt=sum(rate(http_requests_total[5m]))'
```

`prompt` 必填，内容是 PromQL，不是自然语言。服务端使用 `spider.prometheus-address` 的主机及路径前缀，统一访问 `/api/v1/query`，配置中的 `/api/v1/query_range` 仅在本接口内部转换，不修改原配置或其他查询。请求以表单字段 `query` 转发，原样返回上游响应体及 HTTP 状态，不包裹 ResponseDTO。连接超时 5 秒、读取超时 30 秒；连接失败返回 502、socket 超时返回 504。本接口是即时查询，可选参数 `time` 指定计算时刻，不传或传空则使用当前时刻。`time` 支持秒级 Unix 时间戳（可以含小数）或 RFC3339，例如 `2026-09-18T15:00:00+08:00`。

```sh
curl -X POST 'http://localhost:8080/spider/prometheus' \
  --data-urlencode 'prompt=up' \
  --data-urlencode 'time=1789729934.81'
```

## Prometheus 区间查询

```sh
curl -X POST 'http://localhost:8080/spider/prometheus/range' \
  --data-urlencode 'prompt=org_apache_flume_source_s_exec_mobile_2_EventAcceptedCount_Rate{job="kubernetes-pods",tree="datacollector-source-mobile"}' \
  --data-urlencode 'start=1789728134.81' \
  --data-urlencode 'end=1789729934.81' \
  --data-urlencode 'step=7'
```

四个参数都必填，`prompt` 转为上游的 `query`，其余参数原样转发到 `/api/v1/query_range`。`start/end` 支持秒级时间戳或 RFC3339，`step` 支持秒数（如 `7`）或时长（如 `15s`、`1m`），表示计算间隔。返回用于绘制曲线的时间序列数据，不返回 graph 页面或图片。参数格式、时间顺序和步长合法性由 Prometheus 校验，其错误状态码及响应体透传。

两个查询接口均通过 POST 表单调用上游；Prometheus 的 query/query_range 同时支持 GET 和 POST，因此与浏览器截图中的 GET 查询语义一致。除状态码、响应体和 Content-Type 外，不透传其他上游响应头。

协议参考：https://prometheus.io/docs/prometheus/latest/querying/api/#instant-queries

## 自定义 SQL

```sh
curl -X POST 'http://localhost:8080/spider/custom' \
  --data-urlencode 'sql=UPDATE business_server_info SET in_use=0 WHERE id=-1'
```

方法名 `custom`，只有一个请求参数 `sql`。SQL 原样交给 spider 专用的 `jdbcTemplate.update`，用于 INSERT、UPDATE、DELETE，返回影响行数，例如：

```json
{"code":"200","msg":null,"data":1}
```

事务管理器显式指定 `dataSourceTransactionManager`，对应 `spring.datasource.spider`；异常交给现有全局异常处理器。此接口不返回 SELECT 结果集，不添加分页、where 条件或其他改写，也未打开 JDBC 多语句配置。

## 验证状态

新增 MockMvc/模拟上游测试，覆盖 PromQL 特殊字符编码、query_range 地址转换、上游 422 透传、SQL 表单绑定、三种 DML 传递及影响行数、空 SQL 拦截，以及指定 time、区间参数转发、区间响应体保留、区间缺参拦截。未连接真实 Prometheus 或数据库、未执行真实 SQL。

当前环境没有 Java/Maven，尚未编译和执行测试。在具备 Java 8 与项目依赖的环境中运行：

```sh
mvn -pl spider-web -am -Dtest=CustomControllerTest -Dsurefire.failIfNoSpecifiedTests=false test
```
