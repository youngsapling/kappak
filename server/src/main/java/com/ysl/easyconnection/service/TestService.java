package com.ysl.easyconnection.service;

import com.ysl.easyconnection.dao.TestDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/18 21:10
 * @modifyTime :
 * @description :
 */
@Slf4j
@Service
public class TestService {
    @Autowired
    TestDAO testDAO;

    public void add(String name){
        testDAO.add(name);
    }
}
