package com.rain.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chendengyu on 2016/3/21.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
    {
        ModelAndView model = new ModelAndView();

        System.out.println("==============");
        model.addObject("show","num");

        model.setViewName("index");
        return model;
    }
}
