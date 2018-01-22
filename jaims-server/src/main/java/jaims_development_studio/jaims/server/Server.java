package jaims_development_studio.jaims.server;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import jaims_development_studio.jaims.api.IServer;
import jaims_development_studio.jaims.api.command.ICommandManager;
import jaims_development_studio.jaims.api.command.ICommandSender;
import jaims_development_studio.jaims.server.command.PendingCommand;
import jaims_development_studio.jaims.server.command.ServerCommandManager;
import jaims_development_studio.jaims.server.gui.ServerGui;
import jaims_development_studio.jaims.server.network.NetworkSystem;

/**
 * This type represents the server side of JAIMS ( = Just Another Instant Messaging Service). The server can accept
 * multiple clients over a TCP connection. Furthermore it manages all the data received by clients. Everything is
 * persisted in a local database.
 * <br><br>
 * This type contains the {@link #main(String[])} method that accepts command line arguments for configuring the server.
 * It is the intended way of instantiating the {@link Server}.
 *
 * @author WilliGross
 * @see #main(String[]) command line arguments
 * @see IServer
 * @see ICommandSender
 * @see ITickable
 */
public class Server implements Runnable, IServer, ICommandSender, ITickable {
	
	private static final Logger			LOG					= LoggerFactory.getLogger(Server.class);
	/** A reference to the server's main thread. */
	private Thread						serverThread;
	/** Indicates that server is in start up process */
	private boolean						serverStarting		= true;
	/** Indicates whether the server is running or not. Set to false to initiate a shutdown. */
	private boolean						serverRunning		= true;
	/** Indicates to other classes that the server is safely stopped. */
	private boolean						serverStopped;
	/** The server's port. */
	private int							serverPort			= -1;
	/** The maximum number of clients that can connect to the server simultaneously. */
	private int							maxClients			= -2;
	/** Indicates that the server gui is enabled. */
	private boolean						guiEnabled;
	/** Tickrate in ms for all repeating actions on the server */
	private int							tickRate			= 0;
	/** Controls client connections */
	private NetworkSystem				networkSystem;
	/** Manages commands */
	private ICommandManager				commandManager;
	/** A list that stores all pending commands */
	private final List<PendingCommand>	pendingCommandList	= Collections.synchronizedList(new ArrayList<>());	// thread safe
	/** A set that contains all subscribers for ticking */
	private final Set<ITickable>		tickables			= Sets.newHashSet(this);
	/** The Server's UUID. Generated every time the server starts. Used for Messages */
	private final UUID					serverUUID			= UUID.randomUUID();
	
	/**
	 * The main method is called when the server application is started.<br></br>
	 * It processes the command line arguments the user provides when executing the application. If no arguments are
	 * provided the following default values will be applied during initialization:<br></br>
	 *
	 * <table>
	 * <tr> <th>Parameter</th> 		<th>Value</th> 	</tr>
	 * <tr> <td>--nogui</td> 		<td>false</td> 	</tr>
	 * <tr> <td>--port</td> 		<td>6000</td> 	</tr>
	 * <tr> <td>--maxClients</td> 	<td>100</td> 	</tr>
	 * <tr> <td>--tickRate</td> 	<td>50</td> 	</tr>
	 * </table>
	 *
	 * <br></br>
	 * Afterwards the <code>Server</code> and <code>ServerGui</code> are initialized.
	 *
	 * @param args command line arguments the user provides. Possible arguments are: <br></br>
	 *            [help | ?] [nogui | --nogui] [(--serverPort | --port) ###] [--maxClients ###] [--tickRate ###]
	 */
	public static void main(String[] args) {
		boolean gui = true;
		int port = -1;
		int maxClients = -2;
		int tickRate = -1;
		
		//parsing of command line arguments
		for (int i = 0; i < args.length; i++) {
			String param = args[i];
			String nextParam = i == (args.length - 1) ? null : args[i + 1];
			boolean skipParam = false; //used if to arguments belong together like '--port' and '#portValue#'
			
			if (param.equalsIgnoreCase("help") || param.equals("?")) {
				LOG.info("Possible arguments [help | ?] [nogui | --nogui] [(--serverPort | --port) ###] [--maxClients ###] [--tickRate ###]");
				return; //
			} else if (param.equalsIgnoreCase("nogui") || param.equalsIgnoreCase("--nogui"))
				gui = false;
			else if ((param.equalsIgnoreCase("--serverPort") || param.equalsIgnoreCase("--port")) && (nextParam != null)) {
				skipParam = true;
				try {
					port = Integer.parseInt(nextParam);
				} catch (NumberFormatException e) {
					LOG.warn("Couldn't parse port", e);
				}
			} else if (param.equalsIgnoreCase("--maxClients") && (nextParam != null)) {
				skipParam = true;
				try {
					maxClients = Integer.parseInt(nextParam);
				} catch (NumberFormatException e) {
					LOG.warn("Couldn't parse maxClients", e);
				}
			} else if (param.equalsIgnoreCase("--tickRate") && (nextParam != null)) {
				skipParam = true;
				try {
					tickRate = Integer.parseInt(nextParam);
				} catch (NumberFormatException e) {
					LOG.warn("Couldn't parse tickRate", e);
				}
			} else
				LOG.warn("Invalid parameter: " + param);
			
			if (skipParam)
				i++;
		}
		
		//Create a new Server instance
		final Server server = new Server();
		
		//update server properties if the user specified them
		if (port >= 0)
			server.setServerPort(port);
		
		if (maxClients >= -1)
			server.setMaxClients(maxClients);
		
		if (gui && !GraphicsEnvironment.isHeadless())
			server.setGuiEnabled();
		
		if (tickRate >= 0)
			server.setTickRate(tickRate);
		
		//start the server
		server.startServerThread();
	}

	/**
	 * This method initializes the {@link Server}. If the server port, maximum amount of clients and tick rate were not
	 * specified by the user in the <code>main</code> method through command line arguments the following default values will be
	 * applied:
	 * <br></br>
	 *
	 * <table>
	 * <tr> <th>Parameter</th> 		<th>Value</th> 	</tr>
	 * <tr> <td>--nogui</td> 		<td>false</td> 	</tr>
	 * <tr> <td>--port</td> 		<td>6000</td> 	</tr>
	 * <tr> <td>--maxClients</td> 	<td>100</td> 	</tr>
	 * <tr> <td>--tickRate</td> 	<td>50</td> 	</tr>
	 * </table>
	 *
	 * <br></br>
	 * Furthermore a console handler thread is initialized an started. It reads command line input and enqueues it as {@link PendingCommand}s.
	 * <br></br>
	 * A {@link ServerCommandManager} and the {@link NetworkSystem} are initialized.
	 * The server's ticker is started and set to execute all subscribed {@link Tickable}s in the interval specified by the tick rate.
	 *
	 * @return whether initialization was successful
	 */
	private boolean init() {
		
		Thread thread = new Thread(() -> {
			if (guiEnabled)
				return; //Disables command line input to avoid IOException: "The handle is invalid" (temporary)
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String line;

			try {
				while (serverRunning && ((line = bufferedReader.readLine().trim()) != null))
					addPendingCommand(line, this);
			} catch (IOException e) {
				LOG.warn("An unexpected error ocoured!", e);
			}
		}, "ConsoleHandler");
		
		thread.setDaemon(true);
		thread.start();
		
		LOG.info("Starting server...");

		//Configure Server with defaults if nothing else is set
		if (serverPort < 0)
			serverPort = 6000; //Have default values outside?

		if (maxClients < -1)
			maxClients = 100; //Our desktop pc's processor probably won't be able to manage that many clients; should be changed later

		if (tickRate <= 0)
			tickRate = 50;
		
		commandManager = new ServerCommandManager(this);
		
		networkSystem = new NetworkSystem(this);
		
		ScheduledExecutorService ticker = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("Ticker").build());
		ticker.scheduleAtFixedRate(() -> tickables.forEach(t -> t.tick()), 100, tickRate, TimeUnit.MILLISECONDS);
		
		//Load Properties
		
		return true;
	}

	/**
	 * This is the server's main loop. It first initiates initialization and afterwards notifies all threads waiting on
	 * this specific instance of {@link Server} that the server is running. The main loop keeps the Server-Thread alive
	 * until it receives a shutdown signal. When a Exception is caught the main loop initiates a controlled shutdown.
	 */
	@Override
	public void run() {
		
		try {//Just in case
			if (!init())
				//Log error and terminate
				return;
			serverStarting = false;
			LOG.info("Server running");
			synchronized (this) {
				notifyAll(); //used in UserList for example
			}
			while (serverRunning)
				Thread.sleep(50);
			LOG.info("Server stopping...");
			//Saving data is handled by a controlled shutdown of the network system initiated automatically when server is stopped
		} catch (Exception e) {
			LOG.error("Encountered an unexpected exception", e);
			//Implement crash reports
		} finally {
			try { //Just in case
				stopServer();
			} catch (Exception e) {
				LOG.error("Exception stopping the server", e);
			} finally {
				serverStopped = true;
			}
		}
	}
	
	/**
	 * Every tick (specified by command line arguments on server startup) all {@link PendingCommand}s are executed.
	 *
	 * @see ITickable#tick()
	 */
	@Override
	public void tick() {
		executePendingCommands();
	}
	
	/**
	 * This method subscribes a type implementing the interface {@link ITickable} to the {@link Server}'s ticker. The
	 * argument's {@link ITickable#tick()} method is executed at the {@link Server}'s tick rate which can be retrieved
	 * via {@link #getTickRate()}.
	 *
	 * @param subscriber the tickable that shall be subscribed to the {@link Server}'s ticker
	 * @see ITickable
	 */
	public synchronized void subscribeToTicker(ITickable subscriber) {
		tickables.add(subscriber);
	}
	
	/**
	 * This method unsubscribes a type implementing the interface {@link ITickable} from the {@link Server}'s ticker.
	 * The argument's {@link ITickable#tick()} method will not be executed anymore.
	 *
	 * @param subscriber the tickable that shall be unsubscribed from the {@link Server}'s ticker
	 * @see ITickable
	 */
	public synchronized void unsubscribeFromTicker(ITickable subscriber) {
		tickables.remove(subscriber);
	}
	
	/**
	 * This method initiates a shutdown by setting the serverRunning variable to false. This is only a signal to the
	 * server thread that it is kindly asked to stop what ever it is doing. This means that no immediate shutdown is
	 * forced.
	 */
	public synchronized void initiateShutdown() {
		LOG.info("Shutdown initiated");
		serverRunning = false;
	}
	
	/**
	 * At first this method initializes a new Thread with a reference to the instance of this class which implements the
	 * interface {@link Runnable}. Afterwards the thread is started which causes it to run the {@link Server}'s main
	 * loop.
	 *
	 * @see Runnable
	 */
	private void startServerThread() {
		serverThread = new Thread(this, "Server thread");
		serverThread.start();
	}
	
	/**
	 * This method stops the server after releasing all vital components that need to be closed explicitly, mainly the
	 * {@link NetworkSystem}.
	 * <br><br>
	 * Even though it might seem tempting this method shall not be called outside the main loop. 
	 * Use {@link #initiateShutdown()} instead. 
	 * 
	 * @see #initiateShutdown()
	 */
	private void stopServer() {
		// Save data and control shutdown
		
		networkSystem.close();
		
		serverStopped = true;

		LOG.info("Server stopped!");
		
		System.exit(0);
	}
	
	private void setGuiEnabled() {
		ServerGui.createServerGui(this);
		guiEnabled = true;
	}
	
	private void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}
	
	private void setTickRate(int tickRate) {
		this.tickRate = tickRate;
	}
	
	private void setServerPort(int port) {
		serverPort = port;
	}

	public int getServerPort() {
		return serverPort;
	}
	
	public synchronized void addPendingCommand(String input, ICommandSender sender) {
		pendingCommandList.add(new PendingCommand(input, sender));
	}
	
	private synchronized void executePendingCommands() {
		while (!pendingCommandList.isEmpty()) {
			PendingCommand command = pendingCommandList.remove(0);
			getCommandManager().executeCommand(command.getSender(), command.getCommand());
		}
		
	}

	public boolean isServerStarting() {
		return serverStarting;
	}
	
	public boolean isServerStopped() {
		return serverStopped;
	}
	
	public boolean isServerRunning() {
		return serverRunning;
	}
	
	public boolean isGuiEnabled() {
		return guiEnabled;
	}
	
	public int getTickRate() {
		return tickRate;
	}

	public NetworkSystem getNetworkSystem() {
		return networkSystem;
	}
	
	@Override
	public ICommandManager getCommandManager() {
		while (commandManager == null)
			try {
				Thread.sleep(20);
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				LOG.warn("Error while waiting for CommandManager to be initialized");
			}
		return commandManager;
	}
	
	@Override
	public UUID getServerUUID() {
		return serverUUID;
	}
	
	@Override
	public String getName() {
		return "Server";
	}
	
	@Override
	public void sendMessage(String msg) {
		LOG.info(msg);
	}
	
	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		// The server is allowed to execute every command
		return true;
	}
	
	@Override
	public boolean sendCommandFeedback() {
		// At lest for now command feedback shall be received for debugging purposes
		return true;
	}
	
	@Override
	public List<String> getAllRegisteredUsernames() {
		return getNetworkSystem().getClientManager().getUserManager().getAccountManager().getAllRegisteredUsernames();
	}
	
}
