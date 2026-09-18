package com.hexin.cbas.spider.dal.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author viruser
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = {"com.hexin.cbas.spider.dal.mapper.metacollection"},
        sqlSessionFactoryRef = "metacollectionSqlSessionFactory")
public class MetaCollectionDbConfig {

    @Value("${spring.datasource.metacollection.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(name = "metacollectionDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.metacollection")
    public DataSource metacollectionSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "metacollectionSqlSessionFactory")
    public SqlSessionFactory metacollectionSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sf = new SqlSessionFactoryBean();
        sf.setDataSource(metacollectionSource());
        return sf.getObject();
    }

//    @Bean(name = "metacollectionSourceTransactionManager")
//    public DataSourceTransactionManager metacollectionSourceTransactionManager() {
//        return new DataSourceTransactionManager(metacollectionSource());
//    }

    @Bean(name = "metacollectionSqlSessionTemplate")
    public SqlSessionTemplate metacollectionSqlSessionTemplate(@Qualifier("metacollectionSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
