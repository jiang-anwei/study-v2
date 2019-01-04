package com.example.filemonitor;

import com.example.filemonitor.service.FileAlterationObserverTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileMonitorApplication {

    public static void main(String[] args) throws Exception{

        SpringApplication.run(FileMonitorApplication.class, args);
        FileAlterationObserverTest fileAlter = new FileAlterationObserverTest();
        fileAlter.test();
    }

}

