package shop;

import shop.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    // 마지막날추가 - bin간들의 참조하도록
    @Autowired
    OrderRepository orderRepository;

    // 마지막날추가
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverShipped_UpdateStatus(@Payload Shipped shipped){

        if(shipped.isMe()){
            //System.out.println("##### listener UpdateStatus : " + shipped.toJson());

            //  shipped 이벤트가 발생하면, 오더에서 조회시 state를 함께 조회할 수 있다.
            //배송상태에 따른 주문서비스의 배송 상태값 업데이트 ( Delivery  서비스의 shipped이번테에 listening하는 코드 작성 )

            // findById 후에 save = update
            // Optional : jdk 1.8 추가된  nullable한 객체
            Optional<Order> orderOptional = orderRepository.findById(shipped.getOrderId());
            Order order = orderOptional.get();
            order.setStatus(shipped.getStatus());

            orderRepository.save(order);

        }
    }

}
