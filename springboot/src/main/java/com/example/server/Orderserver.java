package com.example.server;

import com.example.entity.Order;
import com.example.mapper.Ordermapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Orderserver {

    @Resource
    Ordermapper ordermapper;
    

    public void add(Order order) {
        ordermapper.insert(order);
    }

    public List<Order>selectall(){
        return ordermapper.selectall(null);
    }

    public PageInfo<Order> selectpage(Integer pagenum, Integer pagesize,Order order) {
        PageHelper.startPage(pagenum,pagesize);
        List<Order> list = ordermapper.selectall(order);
        return PageInfo.of(list);
    }

    public void selectById(String id) {
        ordermapper.selectById(id);
    }

    public Order selectByOrderNo(String orderno) {
        return  ordermapper.selectOrderNo(orderno);
    }

    public void updateById(Order order) {
        ordermapper.update(order);
    }
}
