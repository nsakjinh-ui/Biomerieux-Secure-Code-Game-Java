package com.biomerieux.level3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/* Unrelated to the exercise -- Starts here -- Please ignore */
@SpringBootApplication
public class Level3Application {

    public static void main(String[] args) {
        SpringApplication.run(Level3Application.class, args);
    }

    @RestController
    static class SourceController {

        @GetMapping("/")
        String source(@RequestParam("input") String input) throws Exception {
            DbCrudOps ops = new DbCrudOps();
            ops.getStockInfo(input);
            ops.getStockPrice(input);
            ops.updateStockPrice(input, 0.0);
            ops.execMultiQuery(input);
            ops.execUserScript(input);
            return "ok";
        }
    }
}
/* Unrelated to the exercise -- Ends here -- Please ignore */
