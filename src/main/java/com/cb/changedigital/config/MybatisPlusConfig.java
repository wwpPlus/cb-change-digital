package com.cb.changedigital.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.cb.changedigital.mapper")
public class MybatisPlusConfig {
}
