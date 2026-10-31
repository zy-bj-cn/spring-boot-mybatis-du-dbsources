package com.example.dudb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class DuDbsourcesApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private DataSource dataSourceInjectedByType;

    @Test
    void jarDataSourceIsTheOnlyTypeMatchAndNeitherIsPrimary() {
        String[] names = context.getBeanNamesForType(DataSource.class);
        assertThat(names).containsExactlyInAnyOrder("db1DataSource", "db2DataSource");
        for (String name : names) {
            assertThat(context.getBeanFactory().getMergedBeanDefinition(name).isPrimary())
                    .as(name)
                    .isFalse();
        }
        assertThat(context.getBeanFactory().getMergedBeanDefinition("db2DataSource").isAutowireCandidate())
                .isFalse();
        assertThat(context.getBeanFactory().getMergedBeanDefinition("db2SqlSessionFactory").isAutowireCandidate())
                .isFalse();
        assertThat(dataSourceInjectedByType)
                .isSameAs(context.getBean("db1DataSource", DataSource.class));
    }

    @Test
    void writesAndReadsIsolatedNotesOnBothDataSources() throws Exception {
        long db1Id = create("/db1/notes", "from-db1");
        long db2Id = create("/db2/notes", "from-db2");

        mvc.perform(get("/db1/notes/{id}", db1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("from-db1"));
        mvc.perform(get("/db2/notes/{id}", db2Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("from-db2"));

        mvc.perform(get("/db1/notes/{id}", db2Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("from-db1"));
        mvc.perform(get("/db2/notes/{id}", db1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("from-db2"));

        assertThat(db1Id).isEqualTo(db2Id);
    }

    private long create(String path, String body) throws Exception {
        MvcResult result = mvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"body\":\"" + body + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(body))
                .andReturn();
        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
