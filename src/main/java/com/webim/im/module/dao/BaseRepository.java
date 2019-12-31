package com.webim.im.module.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.mysql.MySQLQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRepository {

    @Autowired
    private EntityManager em;

    protected JPAQueryFactory queryFactory;

    protected MySQLQueryFactory sqlQueryFactory;
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() throws SQLException {
        Connection connection = dataSource.getConnection();
        queryFactory = new JPAQueryFactory(em);
        sqlQueryFactory = new MySQLQueryFactory(()->connection);
    }

}
