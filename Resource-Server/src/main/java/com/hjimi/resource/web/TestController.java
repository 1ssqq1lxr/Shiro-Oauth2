package com.hjimi.resource.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: lxr
 * @Date: 2018/10/9 14:22
 * @Description:
 */
@RestController
public class TestController {

    @RequiresPermissions({"test"})
    @PostMapping("/test")
    public Map<String, Object> getTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "true");
        return map;
    }


    @RequiresPermissions({"test1"})
    @PostMapping("/test1")
    public Map<String, Object> getTest1() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "true");
        return map;
    }

    @PostMapping("/403")
    public ResponseEntity<Void> getUn() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}