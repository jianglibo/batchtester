﻿/**
 * 2016 jianglibo@gmail.com
 *
 */
package com.jianglibo.batch.domain;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jianglibo.batch.Tbase;
import com.jianglibo.batch.domain.projection.FooSimple;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public class TestProjection extends Tbase{
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void tProjection() throws JsonProcessingException {
        Foo foo = new Foo();
        foo.setName("abc");
        String s = objectMapper.writeValueAsString(foo);
        printme(s);
        
        FooSimple foos = (FooSimple)foo;
    }
}
