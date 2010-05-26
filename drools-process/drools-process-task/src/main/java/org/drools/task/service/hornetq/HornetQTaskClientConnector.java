package org.drools.task.service.hornetq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.drools.task.service.BaseHandler;
import org.drools.task.service.TaskClientConnector;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.integration.transports.netty.NettyConnectorFactory;
import org.hornetq.integration.transports.netty.TransportConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HornetQTaskClientConnector implements TaskClientConnector {

	private static final Logger logger = LoggerFactory.getLogger(HornetQTaskClientConnector.class);

	protected ClientSession session;

	protected final BaseHornetQHandler handler;
	protected final String name;   
	protected AtomicInteger counter;

	private String address;
	private Integer port;

	private ClientProducer producer;
	private ClientConsumer consumer;

	public HornetQTaskClientConnector(String name, BaseHornetQHandler handler) {
		if (name == null) {
			throw new IllegalArgumentException("Name can not be null");
		}
		this.name = name;
		this.handler = handler;
		this.counter = new AtomicInteger();
	}

	public boolean connect(String address, int port) {
		this.port = port;
		this.address = address;
		return connect();
	}

	public boolean connect() {
		if (session != null && !session.isClosed()) {
			throw new IllegalStateException("Already connected. Disconnect first.");
		} 
		try {
			Map<String, Object> connectionParams = new HashMap<String, Object>();
			if (address==null) {
				address = "127.0.0.1";
			}
			if (port==null) {
				port = 5445;
			}
			connectionParams.put(TransportConstants.PORT_PROP_NAME, port);
			connectionParams.put(TransportConstants.HOST_PROP_NAME, address);

			TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getCanonicalName(), connectionParams);
			ClientSessionFactory factory = HornetQClient.createClientSessionFactory(transportConfiguration);
			session = factory.createSession();
			producer = session.createProducer(HornetQTaskServer.SERVER_TASK_COMMANDS_QUEUE);

			createClientQueue();

			Thread responsesThread = new Thread(new Runnable() {

				public void run() {
					try {
						consumer = session.createConsumer(name);
						while (true) {
							ClientMessage serverMessage = consumer.receive();
							if (serverMessage!=null) {
								((HornetQTaskClientHandler)handler).messageReceived(session, readMessage(serverMessage), BaseHornetQTaskServer.SERVER_TASK_COMMANDS_QUEUE);
							}
						}
					}
					catch (HornetQException e) {
						if (e.getCode()!=HornetQException.OBJECT_CLOSED) {
							throw new RuntimeException("Client Exception with class " + getClass() + " using port " + port, e);
						}
						logger.info(e.getMessage());
					}
					catch (Exception e) {
						// TODO: remove this printStackTrace()
						e.printStackTrace();
						throw new RuntimeException("Client Exception with class " + getClass() + " using port " + port, e);
					}
				}
			});
			responsesThread.start();
			session.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private Object readMessage(ClientMessage serverMessage) throws IOException, ClassNotFoundException {
		int bodySize = serverMessage.getBodySize();
		byte[] message = new byte[bodySize];
		serverMessage.getBodyBuffer().readBytes(message);
		ByteArrayInputStream bais = new ByteArrayInputStream(message);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}
	
	private void createClientQueue() {
		try {
			session.createQueue(name, name, true);
		}
		catch (HornetQException e) {
			if (e.getCode()!=HornetQException.QUEUE_EXISTS) {
				throw new RuntimeException("Client Exception with class " + getClass() + " using port " + port, e);
			}
			logger.info(e.getMessage());
		}
	}

	public void disconnect() {
		if (session!= null && !session.isClosed()) {
			try {
				producer.close();
				consumer.close();
				session.close();
			} catch (HornetQException e) {
				e.printStackTrace();
			}
		}
	}

	public void write(Object object) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oout;
		try {
			oout = new ObjectOutputStream(baos);
			oout.writeObject(object);
			ClientMessage message = session.createMessage(true);
			message.getBodyBuffer().writeBytes(baos.toByteArray());
			message.putStringProperty("producerId", name);
			producer.send(message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HornetQException e) {
			e.printStackTrace();
		}
	}

	public AtomicInteger getCounter() {
		return counter;
	}

	public BaseHandler getHandler() {
		return handler;
	}

	public String getName() {
		return name;
	}

}