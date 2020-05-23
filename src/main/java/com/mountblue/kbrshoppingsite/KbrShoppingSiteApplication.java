package com.mountblue.kbrshoppingsite;

import com.mountblue.kbrshoppingsite.controller.FileController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;


@SpringBootApplication
public class KbrShoppingSiteApplication {

    public static void main(String[] args) {
        new File(FileController.uploadDirectory).mkdir();
        SpringApplication.run(KbrShoppingSiteApplication.class, args);
        
    }

}
