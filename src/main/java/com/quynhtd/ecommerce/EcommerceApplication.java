package com.quynhtd.ecommerce;

import com.quynhtd.ecommerce.utils.LogUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EcommerceApplication.class);
        Map<String, Object> defProperties = new HashMap<>();
        defProperties.put("spring.profiles.default", "dev");
        app.setDefaultProperties(defProperties);
        Environment env = app.run(args).getEnvironment();
        LogUtils.logApplicationStartup(env);
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
