package tech.joes.Controllers; /**
 * Created by joe on 05/04/2017.
 */

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class MovieController {

    @RequestMapping("/movies/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

}