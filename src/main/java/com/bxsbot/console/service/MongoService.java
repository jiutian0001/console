package com.bxsbot.console.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.bxsbot.console.bean.ZiXun;

@Service
public class MongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    // 新增
    public void insert(ZiXun zx) {
        mongoTemplate.save(zx);
    }

    // 根据id查询
    public ZiXun findById(Integer id) {
        return mongoTemplate.findById(id, ZiXun.class);
    }

    // 根据id修改
    public void updateById(ZiXun zx) {
        Query query = Query.query(Criteria.where("id").is(zx.getId()));
        Update update = Update.update("text", zx.getText())
                            .set("sta", zx.getSta());
        mongoTemplate.updateFirst(query, update, ZiXun.class);
    }

    // 根据id删除
    public void deleteById(Integer id) {
        Query query = Query.query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, ZiXun.class);
    }
}