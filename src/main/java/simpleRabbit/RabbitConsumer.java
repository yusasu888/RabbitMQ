package simpleRabbit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class RabbitConsumer {

	private static final String EXCHANGE_NAME="exchange_demo";
	private static final String ROUTING_KEY="routingkey_demo";
	private static final String QUEUE_NAME="queue_demo";
	private static final String IP_ADDRESS="192.168.1.116";
	private static final int PORT=5672;
	public static void main(String[] args) {
		Address[] addresses = new Address[]{
				new Address(IP_ADDRESS,PORT)
		};
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("admin");
		factory.setPassword("admin") ;
		try {
			//�������ӷ�ʽ�������ߵ�demo �Բ�ͬ��ע��������
			Connection connection = factory.newConnection(addresses);
			final Channel channel = connection.createChannel();
			channel.basicQos(64);//���ÿͻ��������� δ��ack����Ϣ����
			Consumer consumer = new DefaultConsumer(channel){
				public void handleDelivery(String consumerTag,
						 Envelope envelope,
						 AMQP.BasicProperties properties,byte[] body){
					System.out.println("recv message: "+new String(body));
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						channel.basicAck(envelope.getDeliveryTag(), false);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			};
			channel.basicConsume(QUEUE_NAME,consumer);
			//�ȴ��ص�����ִ�����֮��ر���Դ
			try {
				TimeUnit.SECONDS.sleep(5);
				channel.close();
				connection.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
