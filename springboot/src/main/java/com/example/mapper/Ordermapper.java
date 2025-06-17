package com.example.mapper;

import com.example.entity.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface Ordermapper {
    List<Order>selectall(Order order);

    @Select("select * from `ddorder` where name = #{name}")
    Order selectByOrdername(String name);


    @Select("select * from `ddorder` where id = #{id}")
    Order selectById(String id);

    @Select("select * from `ddorder` where id = #{orderno}")
    Order selectOrderNo(String orderno);

    void insert(Order order);

    void update(Order order);
}
