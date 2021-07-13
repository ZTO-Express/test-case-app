package com.zto.testcase.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.zto.testcase.util.DESCodec;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

public abstract class BaseDruidConfig {
    protected static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKNRtGfEJRMnBbQmdPzXIsA4ubFTElenR8oEAK0Y/auFgVQ+XQmJnizFbrMzr/yPTde9wvn5A0/quZkYNHhjGxECAwEAAQ==";

    /**
     * Druid数据源配置
     *
     * @return
     * @throws SQLException
     * @see <a href="https://github.com/alibaba/druid/wiki/配置_DruidDataSource参考配置">DruidDataSource参考配置</a>
     */
    public DruidDataSource druidDataSource() throws Exception {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(getUrl());
        druidDataSource.setUsername(DESCodec.decode(getUsername()));
        druidDataSource.setPassword(DESCodec.decode(getPassword()));
        druidDataSource.setInitialSize(getInitialSize());
        druidDataSource.setMinIdle(getMinIdle());
        druidDataSource.setMaxActive(getMaxActive());
        druidDataSource.setMaxWait(getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(getValidationQuery());
        druidDataSource.setTestWhileIdle(isTestWhileIdle());
        druidDataSource.setTestOnBorrow(isTestOnBorrow());
        druidDataSource.setTestOnReturn(isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setFilters(getFilters());
        druidDataSource.setConnectionProperties(getConnectionProperties());
        druidDataSource.setUseGlobalDataSourceStat(isUseGlobalDataSourceStat());
        return druidDataSource;
    }

    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("allow", "127.0.0.1"); //白名单
        //reg.addInitParameter("deny",""); //黑名单
        reg.addInitParameter("loginUsername", "druid");
        reg.addInitParameter("loginPassword", "druid");
        return reg;
    }

    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    public SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mybatis/test/*.xml"));
        return bean.getObject();
    }

    public DataSourceTransactionManager createTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    public SqlSessionTemplate createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    abstract public String getUrl();

    abstract public String getPassword();

    abstract public String getUsername();

    abstract public int getInitialSize();

    abstract public int getMaxActive();

    abstract public int getMinIdle();

    abstract public int getMaxWait();

    abstract public int getTimeBetweenEvictionRunsMillis();

    abstract public int getMinEvictableIdleTimeMillis();

    abstract public String getValidationQuery();

    abstract public boolean isTestWhileIdle();

    abstract public boolean isTestOnBorrow();

    abstract public boolean isTestOnReturn();

    abstract public boolean isPoolPreparedStatements();

    abstract public int getMaxPoolPreparedStatementPerConnectionSize();

    abstract public String getFilters();

    abstract public String getConnectionProperties();

    abstract public boolean isUseGlobalDataSourceStat();
}