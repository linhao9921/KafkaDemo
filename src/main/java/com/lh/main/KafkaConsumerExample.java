package com.lh.main;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * 消费者
 * 
 * @author linhao
 *
 */
public class KafkaConsumerExample { 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.238.128:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
       
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        
        //订阅主题
        consumer.subscribe(Arrays.asList("topic1"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            if(!records.isEmpty())
            	System.out.println("#######################################################################");
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("我是订阅者（消费者）：offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            if(!records.isEmpty())
            	System.out.println("#######################################################################\n");
        }

	}

}
