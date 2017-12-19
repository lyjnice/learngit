package springboot_product.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Helloworld {

    @RequestMapping(value = "hello",)
   public void sayHello(){
       return "hello lady";
   }
}
