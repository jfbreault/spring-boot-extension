package org.springframework.boot.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.HawtioAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@Import(HawtioAutoConfiguration.class)
public class SampleHawtioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleHawtioApplication.class, args);
    }

}
