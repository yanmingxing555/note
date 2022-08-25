package com.fomp.note;

import com.yss.fomp.api.annotations.FompApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@FompApplication
@ComponentScan(basePackages = {"com.fomp.note" })
@EnableFeignClients({"com.fomp.note.**.feign"})
public class NoteApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class,args);
    }
}
