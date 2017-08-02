package com.lh.main;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;

/**
 * 生产者
 * 
 * @author linhao
 * 
 */
public class KafkaProducerExample {

	public static void zookeeperTest() {
		try {
			ZooKeeper zk = new ZooKeeper("192.168.238.128:2181", 500000,
					new Watcher() {
						// 监控所有被触发的事件
						public void process(WatchedEvent event) {
							System.out.println(event.getPath());
							System.out.println(event.getType().name());
							// System.out.println(event.getState().getIntValue());
						}
					});
			
			if(zk.getState() == States.CONNECTED){
				Stat stat = zk.exists("/root", true);// 观察这个节点发生的事件
				System.out.println(stat == null ? "null" : stat.toString());
			}else{
				System.out.println("============================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//zookeeperTest();
		
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.238.128:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		
		props.put("producer.type", "sync");  
		props.put("request.required.acks", "1");  
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");

		//生产者将消息发送到
		Producer<String, String> producer = new KafkaProducer<>(props);
		
		int count = 0;
		
		//定期发送
		while(++count <= 5){
			for (int i = 0; i < 100; i++)
				producer.send(new ProducerRecord<>("topic1", Integer
						.toString(i), "[" + count + "]这是第[" + (i+1) +"]条消息呢！") , new Callback() {
							@Override
							public void onCompletion(RecordMetadata metadata, Exception exception) {
								if(metadata != null){
									System.out.println("the offset of the send record is "+ metadata.offset() + " partition=" + metadata.partition()); 
								} 
								if(null != exception) {  
									System.out.println(exception.getMessage());  
				                }  
								
								System.out.println("生产者消息：calback ...");
							}
						});
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		producer.close();

	}
}
