package com.gateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    /**
     * fallback需要和远请求方法入参与返回值一致
     */
    public String fallback(@RequestBody Goods goods){
        System.out.println(goods.toString());
        return "系统处理异常!!!";
    }
    /**
     * 添加数据
     */
    @PostMapping
    @HystrixCommand(fallbackMethod = "fallback")//降级处理
    public String addGoods(@RequestBody Goods goods) throws  Exception {
        System.out.println(goods.toString());
        if (goods.getId()==99){
            throw new Exception("请求参数有误！！");
        }
        return "商品添加成功";
    }
    /**
     * 通过/id查询
     */
    @GetMapping("/{id}")
    public Goods findByPage(@PathVariable Integer id){
        return new Goods(1,"笔记本电脑","图片",2000.55);
    }
    /**
     * 修改数据
     */
    @PutMapping
    public String updateGoods(@RequestBody Goods goods){
        return "商品修改成功";
    }
    /**
     * 根据/id删除
     */
    @DeleteMapping("/{id}")
    public String deleteGoods(@PathVariable("id") Integer id){
        return "商品删除成功";
    }
} 
