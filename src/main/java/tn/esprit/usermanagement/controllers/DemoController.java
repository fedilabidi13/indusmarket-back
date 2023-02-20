package tn.esprit.usermanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class DemoController {
    @GetMapping("/yo")
    public String addEquipe ()
    {
        return "ih";
    }
    @GetMapping
    public ResponseEntity<String> sayHello()
    {
        return ResponseEntity.ok("the job is done");
    }
}
