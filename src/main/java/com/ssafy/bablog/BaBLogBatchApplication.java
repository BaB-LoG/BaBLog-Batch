package com.ssafy.bablog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan({
        "com.ssafy.bablog.member.repository.mapper",
        "com.ssafy.bablog.meal.repository.mapper",
        "com.ssafy.bablog.meal_log.repository.mapper",
        "com.ssafy.bablog.food.repository.mapper",
        "com.ssafy.bablog.member_nutrient.repository.mapper",
        "com.ssafy.bablog.report.repository.mapper",
        "com.ssafy.bablog.batch.failure.mapper"
})
public class BaBLogBatchApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BaBLogBatchApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
