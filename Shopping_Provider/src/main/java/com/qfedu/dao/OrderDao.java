package com.qfedu.dao;

import com.qfedu.entity.OrderForm;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Repository
public interface OrderDao {
    //生成订单
    @Insert("insert into t_order(id,aid,uid,totalmoney,freemoney,paymoney,paytype,ctime,utime,flag) values(#{id},#{aid},#{uid},#{totalmoney},#{freemoney},#{paymoney},#{paytype},now(),now(),#{flag})")
   int add(OrderForm orderForm);
    //查询代付款的订单
    @Select("select * from t_order where id=#{id} and flag=1")
    OrderForm query(long id);
    //更改订单状态
    @Update("update t_order set flag=#{flag} where id=#{id}")
    int update(@Param("id") long id,@Param("flag") int flag);
    //查询用户
    @Select("select * from t_order where uid=#{uid}")
    List<OrderForm> queryByUid(int id);
}
