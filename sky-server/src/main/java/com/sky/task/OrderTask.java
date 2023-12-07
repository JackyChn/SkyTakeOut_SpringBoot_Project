package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

//    @Scheduled(cron = "0 * * * * *")  // execute every minute
    public void processTimeOut() {
        log.info("Executing timeout order now at: {}", LocalDateTime.now());

//        select the timeout order: select * from order where status = ? and order_time < (current_time - 15)
//        current_time - order_time > 15 ==> current_time - 15 > order_time

        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-15);// deduct 15 min from now ==> currenTime-15
        List<Orders> orderList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, orderTime);

//        Then check the list and enumerate it
        if (orderList != null && !orderList.isEmpty()) {
            for (Orders order : orderList) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Order is time out (15min)");
                order.setCancelTime(LocalDateTime.now());
            }
        }
    }

//        deal with orders which are on delivery every day at 1 am in midnight
//    @Scheduled(cron = "0 0 1 * * *")
    public void processDeliveryOrder() {
        log.info("Executing on-delivery order at 1 am in midnight every day");

        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orderList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, orderTime);
//        Then check the list and enumerate it
        if (orderList != null && !orderList.isEmpty()) {
            for (Orders order : orderList) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Order is time out (15min)");
                order.setCancelTime(LocalDateTime.now());
            }
        }
    }
}



