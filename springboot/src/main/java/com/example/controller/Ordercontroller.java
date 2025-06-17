package com.example.controller;

import com.example.common.Result;
import com.example.entity.Order;
import com.example.server.Orderserver;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class Ordercontroller {

    @Resource
    Orderserver orderserver;


    @PostMapping("/add")
    public Result add(@RequestBody Order order) {
        orderserver.add(order);
        return Result.success();
    }

    /*一个get请求接口*/
    @GetMapping("/selectall")  //完整网页路径@/order/selectall
    public Result selectall() {
        List<Order> orderlist = orderserver.selectall();
        return Result.success(orderlist);
    }


    @GetMapping("/selectpage")
    public Result selectpage(@RequestParam(defaultValue = "1")Integer pagenum,
                             @RequestParam(defaultValue = "10")Integer pagesize
                              ,Order order){
        PageInfo<Order> pageInfo=orderserver.selectpage(pagenum,pagesize,order);
        return Result.success(pageInfo);
    }
}
