package com.it.uaa;

import com.it.uaa.common.domain.shared.BeanProvider;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App implements ApplicationContextAware
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanProvider.initialize(applicationContext);
    }
}
