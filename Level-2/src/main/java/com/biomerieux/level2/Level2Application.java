package com.biomerieux.level2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/* Unrelated to the exercise -- Starts here -- Please ignore */
@SpringBootApplication
public class Level2Application {

    public static void main(String[] args) {
        SpringApplication.run(Level2Application.class, args);
    }

    @RestController
    static class SourceController {

        @GetMapping("/")
        String source(@RequestParam("input") String input) throws Exception {
            TaxPayer taxPayer = new TaxPayer("foo", "bar");
            taxPayer.getTaxFormAttachment(input);
            taxPayer.getProfPicture(input);
            return "ok";
        }
    }
}
/* Unrelated to the exercise -- Ends here -- Please ignore */
