package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @RestController
    @RequestMapping("/api")
    public static class ApiController {

        @Value("${app.owner:unknown}")
        private String owner;

        @GetMapping("/hello")
        public Greeting hello(
                @RequestParam(defaultValue = "world") String name) {

            return new Greeting(
                    "Hello, " + name + "!",
                    owner,
                    LocalDateTime.now()
            );
        }

        @GetMapping("/info")
        public Info info() {

            return new Info(
                    owner,
                    System.getProperty("java.version"),
                    Runtime.getRuntime().availableProcessors()
            );
        }

        public record Greeting(
                String message,
                String owner,
                LocalDateTime timestamp
        ) {
        }

        public record Info(
                String owner,
                String javaVersion,
                int cpuCores
        ) {
        }
    }
}