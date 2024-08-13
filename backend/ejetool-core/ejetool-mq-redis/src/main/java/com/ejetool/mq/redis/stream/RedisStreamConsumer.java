// package com.ejetool.mq.redis.stream;

// import org.springframework.beans.factory.DisposableBean;
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.data.redis.connection.stream.Consumer;
// import org.springframework.data.redis.connection.stream.MapRecord;
// import org.springframework.data.redis.connection.stream.ReadOffset;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.stream.StreamListener;
// import org.springframework.data.redis.stream.StreamMessageListenerContainer;
// import org.springframework.data.redis.stream.Subscription;
// import org.springframework.stereotype.Component;

// import com.ejetool.mq.redis.helper.RedisStreamHelper;

// import ch.qos.logback.core.util.Duration;
// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// public class RedisStreamConsumer implements StreamListener<String, MapRecord<String, Object, Object>>, InitializingBean, DisposableBean 
// {
// 	private StreamMessageListenerContainer<String, MapRecord<String, Object, Object>> listenerContainer;
// 	private Subscription subscription;

// 	private final RedisTemplate<String, Object> redisTemplate;
//     private final Consumer consumer;

// 	@Override
// 	public void destroy() throws Exception {
// 		if (subscription != null) {
// 			subscription.cancel();
// 		}
// 		if (listenerContainer != null) {
// 			listenerContainer.stop();
// 		}
// 	}

// 	@Override
// 	public void afterPropertiesSet() throws Exception {
// 		// Consumer Group 초기화
// 		RedisStreamHelper.autoCreate(this.redisTemplate, null, null, null)

// 		// StreamMessageListenerContainer 설정
// 		listenerContainer = redisOperator.createStreamMessageListenerContainer();

// 		subscription = listenerContainer.receive(
// 			Consumer.from(consumerGroupName, consumerName),
// 			StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
// 			this
// 		);

// 		subscription.await(Duration.ofSeconds(2));

// 		listenerContainer.start();
// 	}

// 	/**
// 	 *
// 	 * Redis Stream 메세지를 처리하는 메소드
// 	 */
// 	@Override
// 	public void onMessage(MapRecord<String, Object, Object> message) {
// 		Set<Map.Entry<Object, Object>> entries = message.getValue().entrySet();
// 		Long[] depositData = new Long[3];
// 		int index = 0;

// 		// depositData에 send-pk, deposit-pk, money를 차례대로 담는다.
// 		for (Map.Entry<Object, Object> entry : entries) {
// 			String value = (String)entry.getValue();
// 			depositData[index++] = Long.valueOf(value);
// 		}

// 		// deposit-pk에 money를 추가하는 task 추가
// 		depositHandlerService.doDeposit(depositData[1], depositData[2], message.getId().toString());
// 	}
// }
