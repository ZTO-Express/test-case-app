package com.zto.testcase.config;

import com.alibaba.druid.pool.DruidDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@Data
@MapperScan(basePackages = "com.zto.testcase.mapper.testcase",
        sqlSessionTemplateRef = "testcaseSqlSessionTemplate")
public class TestCaseDruidConfig extends BaseDruidConfig {

    @Value("${spring.datasources.testcase.url}")
    private String url;

    @Value("${spring.datasources.testcase.password}")
    private String password;

    @Value("${spring.datasources.testcase.username}")
    private String username;

    @Value("${spring.datasources.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasources.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasources.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasources.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasources.druid.timeBetweenEvictionRunsMillis:60000}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasources.druid.minEvictableIdleTimeMillis:300000}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasources.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasources.druid.testWhileIdle:true}")
    private boolean testWhileIdle;

    @Value("${spring.datasources.druid.testOnBorrow:false}")
    private boolean testOnBorrow;

    @Value("${spring.datasources.druid.testOnReturn:false}")
    private boolean testOnReturn;

    @Value("${spring.datasources.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasources.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasources.druid.filters}")
    private String filters;

    @Value("${spring.datasources.druid.connectionProperties:}")
    private String connectionProperties;

    @Value("${spring.datasource.druid.useGlobalDataSourceStat:false}")
    private boolean useGlobalDataSourceStat;

    /**
     * Druid数据源配置
     *
     * @return
     * @throws SQLException
     * @see <a href="https://github.com/alibaba/druid/wiki/配置_DruidDataSource参考配置">DruidDataSource参考配置</a>
     */
    @Bean(name = "testcaseDataSource")
    @Qualifier("testcaseDataSource")
    @Primary
    @Override
    public DruidDataSource druidDataSource() throws Exception {
        return super.druidDataSource();
    }

    @Bean
    @Override
    public ServletRegistrationBean druidServlet() {
        return super.druidServlet();
    }

    @Bean
    @Override
    public FilterRegistrationBean filterRegistrationBean() {
        return super.filterRegistrationBean();
    }

    @Bean(name = "testcaseSqlSessionFactory")
    @Primary
    @Override
    public SqlSessionFactory createSqlSessionFactory(@Qualifier("testcaseDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/testcase/*Mapper.xml"));
        bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return bean.getObject();
    }

    @Bean(name = "testcaseTransactionManager")
    @Primary
    @Override
    public DataSourceTransactionManager createTransactionManager(@Qualifier("testcaseDataSource") DataSource dataSource) {
        return super.createTransactionManager(dataSource);
    }

    @Bean(name = "testcaseSqlSessionTemplate")
    @Primary
    @Override
    public SqlSessionTemplate createSqlSessionTemplate(@Qualifier("testcaseSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return super.createSqlSessionTemplate(sqlSessionFactory);
    }
}