import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

	// Setting up class and naming queue.
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {

		// Create a connection to the server. Socket connection is abstracted
		// and protocol version negotiation and authentication and so on are taken care
		// of.
		ConnectionFactory factory = new ConnectionFactory();
		// Connect to a RabbitMQ node on the local machine
		factory.setHost("localhost");
		// Try with resources (Connection and Channel both implement
		// java.lang.AutoCloseable)
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			// Declare a queue to send to and publish message in the queue.
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "Hello World!";
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		}
	}
}