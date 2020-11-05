package com.qfedu.dao;


import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrderItemDao {
    int [] insertBatch(Collection<Object> list);

}
