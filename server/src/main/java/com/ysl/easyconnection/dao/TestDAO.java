package com.ysl.easyconnection.dao;

import org.springframework.stereotype.Repository;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/18 21:11
 * @modifyTime :
 * @description :
 */
@Repository
public class TestDAO {

    public void add(String name){
        System.out.println("TestDAO1" + name);
    }
}
