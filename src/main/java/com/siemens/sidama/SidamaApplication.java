package com.siemens.sidama;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.siemens.sidama.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class SidamaApplication implements CommandLineRunner {
/*
    @Autowired
    private DistributorRepository distributorRepository;
*/
    public static void main(String[] args) {
        SpringApplication.run(SidamaApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        /*for (int i = 0; i < 5; i++) {
            distributorRepository.save(new Distributor(i, "username" + i, "password" + i, "name" + i, "email" + i));
        }*/
    }
}
