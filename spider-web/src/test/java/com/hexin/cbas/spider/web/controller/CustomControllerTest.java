package com.hexin.cbas.spider.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomControllerTest {
    private JdbcTemplate jdbc;
    private MockMvc mvc;
    private MockRestServiceServer upstream;

    @Before
    public void setup() {
        jdbc = mock(JdbcTemplate.class);
        CustomController controller = new CustomController(jdbc, "http://prometheus:9090/api/v1/query_range");
        upstream = MockRestServiceServer.bindTo((RestTemplate)
                ReflectionTestUtils.getField(controller, "prometheusClient")).build();
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .addPlaceholderValue("api.prefix", "/spider").build();
    }

    @Test
    public void forwardsEncodedPromqlToInstantQuery() throws Exception {
        String prompt = "up{job=~\"a+b|c\"} + 1";
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("query", prompt);
        upstream.expect(requestTo("http://prometheus:9090/api/v1/query"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().formData(form))
                .andRespond(withSuccess("{\"status\":\"success\"}", MediaType.APPLICATION_JSON));
        mvc.perform(post("/spider/prometheus").param("prompt", prompt))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("success"));
        upstream.verify();
    }

    @Test
    public void preservesPrometheusErrors() throws Exception {
        upstream.expect(requestTo("http://prometheus:9090/api/v1/query"))
                .andRespond(withStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                        .contentType(MediaType.APPLICATION_JSON).body("{\"status\":\"error\"}"));
        mvc.perform(post("/spider/prometheus").param("prompt", "invalid("))
                .andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.status").value("error"));
        upstream.verify();
    }

    @Test
    public void forwardsInstantQueryAtSpecifiedTime() throws Exception {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("query", "up");
        form.add("time", "2026-09-18T15:00:00+08:00");
        upstream.expect(requestTo("http://prometheus:9090/api/v1/query"))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().formData(form))
                .andRespond(withSuccess("{\"status\":\"success\"}", MediaType.APPLICATION_JSON));
        mvc.perform(post("/spider/prometheus").param("prompt", "up")
                .param("time", "2026-09-18T15:00:00+08:00"))
                .andExpect(status().isOk());
        upstream.verify();
    }

    @Test
    public void forwardsRangeQueryAndPreservesResponseBody() throws Exception {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("query", "up{job=\"kubernetes-pods\"}");
        form.add("start", "1789728134.81");
        form.add("end", "1789729934.81");
        form.add("step", "7");
        String body = "{\"status\":\"success\",\"data\":{\"resultType\":\"matrix\",\"result\":[]}}";
        upstream.expect(requestTo("http://prometheus:9090/api/v1/query_range"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(org.springframework.test.web.client.match.MockRestRequestMatchers.content().formData(form))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));
        mvc.perform(post("/spider/prometheus/range").param("prompt", form.getFirst("query"))
                .param("start", form.getFirst("start")).param("end", form.getFirst("end"))
                .param("step", "7"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string(body));
        upstream.verify();
    }

    @Test
    public void rejectsIncompleteRangeWithoutCallingUpstream() throws Exception {
        mvc.perform(post("/spider/prometheus/range").param("prompt", "up")
                .param("start", "1789728134.81").param("end", "1789729934.81"))
                .andExpect(status().isBadRequest());
        upstream.verify();
    }

    @Test
    public void executesEachDmlAndReturnsAffectedRows() throws Exception {
        String[] statements = {"INSERT INTO demo(id) VALUES(1)",
                "UPDATE demo SET value=2 WHERE id=1", "DELETE FROM demo WHERE id=1"};
        for (String sql : statements) {
            when(jdbc.update(sql)).thenReturn(1);
            mvc.perform(post("/spider/custom").param("sql", sql))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.data").value(1));
            verify(jdbc).update(sql);
        }
    }

    @Test
    public void rejectsBlankSqlWithoutExecutingIt() throws Exception {
        mvc.perform(post("/spider/custom").param("sql", " "))
                .andExpect(jsonPath("$.code").value("400"));
        verifyZeroInteractions(jdbc);
    }
}
