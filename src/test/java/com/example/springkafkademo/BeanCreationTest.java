package com.example.springkafkademo;

import com.example.springkafkademo.kafka.producers.KafkaProducerConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

public class BeanCreationTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(KafkaProducerConfig.class);

    @Test
    @DisplayName("KafkaProducerConfig 안에 있는 모든 빈 출력")
    void findAllBeanInKafkaProducerConfig(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for(String beanDefinitionName : beanDefinitionNames){
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " Object= " + bean);
            }
        }
    }

    @Test
    void findBeanByTypeDuplicate(){
        KafkaTemplate bean = ac.getBean("userKafkaTemplate", KafkaTemplate.class);
        System.out.println(bean.toString());
    }
}
