package jabtwitterback.jabtwitterback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class apiController {

    @GetMapping("/api")
    public String api(){
        return "화이팅";
    }

    @GetMapping("/indes")
    public String index(){
        return "인덱스";
    }
}
