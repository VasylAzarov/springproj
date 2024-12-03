package com.example.mate.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.unwrap(SessionFactory.class);
    }
}
