package com.hexin.cbas.spider.web.controller;

import com.hexin.cbas.spider.constants.StatusCodeConstant;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Collections;

/** 应用内的 Prometheus 查询转发和 spider 数据库自定义操作。 */
@RestController
@RequestMapping("${api.prefix}")
public class CustomController {
    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate prometheusClient;
    private final URI prometheusQueryUri;
    private final URI prometheusRangeUri;

    public CustomController(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate,
                            @Value("${spider.prometheus-address}") String prometheusAddress) {
        this.jdbcTemplate = jdbcTemplate;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(30000);
        this.prometheusClient = new RestTemplate(factory);
        URI configured = URI.create(prometheusAddress);
        String path = configured.getPath().replaceAll("/+$", "");
        if (path.endsWith("/api/v1/query_range")) {
            path = path.substring(0, path.length() - "query_range".length()) + "query";
        } else if (!path.endsWith("/api/v1/query")) {
            path += "/api/v1/query";
        }
        this.prometheusQueryUri = UriComponentsBuilder.fromUri(configured)
                .replacePath(path).replaceQuery(null).fragment(null).build().toUri();
        this.prometheusRangeUri = UriComponentsBuilder.fromUri(prometheusQueryUri)
                .replacePath(path + "_range").build().toUri();
    }

    /** prompt 是 PromQL 表达式，使用表单编码转发，保留特殊字符。 */
    @PostMapping(value = "/prometheus", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> prometheus(@RequestParam("prompt") String prompt,
                                             @RequestParam(value = "time", required = false) String time) {
        if (!StringUtils.hasText(prompt)) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\":\"error\",\"error\":\"prompt不能为空\"}");
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("query", prompt);
        if (StringUtils.hasText(time)) {
            form.add("time", time);
        }
        return forwardPrometheus(prometheusQueryUri, form);
    }

    /** 区间查询，start/end 为秒级时间戳或 RFC3339，step 为秒数或时长。 */
    @PostMapping(value = "/prometheus/range", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> prometheusRange(
            @RequestParam("prompt") String prompt,
            @RequestParam(value = "start", defaultValue = "") String start,
            @RequestParam(value = "end", defaultValue = "") String end,
            @RequestParam(value = "step", defaultValue = "") String step) {
        if (!StringUtils.hasText(prompt) || !StringUtils.hasText(start)
                || !StringUtils.hasText(end) || !StringUtils.hasText(step)) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\":\"error\",\"error\":\"prompt、start、end、step不能为空\"}");
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("query", prompt);
        form.add("start", start);
        form.add("end", end);
        form.add("step", step);
        return forwardPrometheus(prometheusRangeUri, form);
    }

    private ResponseEntity<String> forwardPrometheus(URI uri, MultiValueMap<String, String> form) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        try {
            ResponseEntity<String> upstream = prometheusClient.exchange(uri,
                    HttpMethod.POST, new HttpEntity<>(form, headers), String.class);
            MediaType contentType = upstream.getHeaders().getContentType();
            return ResponseEntity.status(upstream.getStatusCode())
                    .contentType(contentType == null ? MediaType.APPLICATION_JSON : contentType)
                    .body(upstream.getBody());
        } catch (HttpStatusCodeException e) {
            MediaType contentType = e.getResponseHeaders() == null
                    ? null : e.getResponseHeaders().getContentType();
            return ResponseEntity.status(e.getRawStatusCode())
                    .contentType(contentType == null ? MediaType.APPLICATION_JSON : contentType)
                    .body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            boolean timeout = e.getMostSpecificCause() instanceof SocketTimeoutException;
            return ResponseEntity.status(timeout ? HttpStatus.GATEWAY_TIMEOUT : HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\":\"error\",\"error\":\"Prometheus连接失败或超时\"}");
        }
    }

    /** 直接执行 spider 数据源上的 SQL，返回影响行数。 */
    @PostMapping("/custom")
    @Transactional(transactionManager = "dataSourceTransactionManager", rollbackFor = Exception.class)
    public ResponseDTO custom(@RequestParam("sql") String sql) {
        if (!StringUtils.hasText(sql)) {
            return ResponseDTO.error(StatusCodeConstant.PARAM_ERROR, "sql不能为空");
        }
        return ResponseDTO.success(jdbcTemplate.update(sql));
    }
}
