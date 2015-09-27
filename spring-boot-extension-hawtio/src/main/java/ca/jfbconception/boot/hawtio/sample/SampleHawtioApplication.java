package ca.jfbconception.boot.hawtio.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Import(HawtioAutoConfiguration.class)
public class SampleHawtioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleHawtioApplication.class, args);
    }

}
