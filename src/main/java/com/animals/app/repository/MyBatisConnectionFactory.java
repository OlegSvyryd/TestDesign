package com.animals.app.repository;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rostyslav.Viner on 23.07.2015.
 */
public class MyBatisConnectionFactory {
    private static final Logger LOG = Logger.getLogger(MyBatisConnectionFactory.class);
    private static final String configurationFile = "mybatis-config.xml";
    private static SqlSessionFactory sqlSessionFactory;

    public MyBatisConnectionFactory() {
        InputStream inputStream;
        try {
            inputStream = Resources.getResourceAsStream(configurationFile);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            LOG.fatal(e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }
}
