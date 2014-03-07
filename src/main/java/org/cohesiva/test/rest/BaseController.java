package org.cohesiva.test.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by adam on 3/4/14.
 */
@Controller
@RequestMapping("/")
public class BaseController {


    @RequestMapping(method = RequestMethod.GET, value = "/rest/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String test(@PathVariable("id")Long id){
        StringBuilder sb = new StringBuilder("[");
        String delim ="";
        for(int i=0; i < id; i++){
         sb.append(delim).append("{\"status\":\"ok\",\"id\":").append(id).append("}");
         delim=",";
        }
        return sb.append("]").toString();
    };

    @RequestMapping(method = RequestMethod.GET, value = "/index/")
    public String index(){
        return "index";
    };

}

