package com.ecommerse.config_server;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BuildInfoController {

    private BuildInfo buildInfo;

    @GetMapping("/build-info")
    public String getBuildInfo(){
        return "Build id: "+ buildInfo.getId() +", Build version: "+ buildInfo.getVersion() + ", Build Name: "+ buildInfo.getName();
    }
}
