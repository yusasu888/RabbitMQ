package simpleRabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
//������
public class RabbitProducer {
	private static final String EXCHANGE_NAME="exchange_demo";
	private static final String ROUTING_KEY="routingkey_demo";
	private static final String QUEUE_NAME="queue_demo";
	private static final String IP_ADDRESS="192.168.1.116";
	private static final int PORT=5672;
	public static void main(String[] args) {
         ConnectionFactory factory = new ConnectionFactory();
         factory.setHost(IP_ADDRESS);
         factory.setPort(PORT);     
         factory.setUsername("admin");
         factory.setPassword("admin");
         try {
			Connection connection = factory.newConnection();//��������
			Channel channel = connection.createChannel();//�����ŵ�
			//����һ�� type="direct" �־û������Զ�ɾ���Ľ�����
			channel.exchangeDeclare(EXCHANGE_NAME, "direct",true,false,null);
			//����һ�� �־û����������ģ����Զ�ɾ���Ķ���
			channel.queueDeclare(QUEUE_NAME,true,false,false,null);
			//�������������ͨ��·�ɼ���
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
			//����һ���־û�����Ϣ
			String message="Hello Worldaaaa";
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
					MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			//�ر���Դ
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}//��������
         
	}

}
