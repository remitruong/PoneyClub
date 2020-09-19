package fr.esieaproject.poneyclub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${welcome}")
    private String welcome;
    

    @GetMapping(value = "/")
    public String ipaddress() throws Exception {
        return "Reply: " + welcome;
    }
    
}
