import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Recv {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection;
		Channel channel = null;
		// Don't use try-with-resources so that the process stays alive while
		// the consumer is listening asynchronously for messages to arrive.
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		// Since RabbitMQ server pushes us messages asynchronously, provide a callback
		// in the form of an object that will buffer the messages until we're ready to
		// use them.
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message + "'");
		};
		try {
			channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}