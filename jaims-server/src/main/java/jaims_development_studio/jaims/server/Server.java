package jaims_development_studio.jaims.server;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import jaims_development_studio.jaims.server.command.ICommandManager;
import jaims_development_studio.jaims.server.command.ICommandSender;
import jaims_development_studio.jaims.server.command.PendingCommand;
import jaims_development_studio.jaims.server.command.ServerCommandManager;
import jaims_development_studio.jaims.server.gui.ServerGui;
import jaims_development_studio.jaims.server.network.NetworkSystem;

public class Server implements Runnable, ICommandSender, ITickable {

	private static final Logger			LOG					= LoggerFactory.getLogger(Server.class);
	/** A reference to the server's main thread. */
	private Thread						serverThread;
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

	@Override
	public void run() {

		try {//Just in case
			if (!init())
				//Log error and terminate
				return;
			LOG.info("Server running");
			while (serverRunning)
				//accept clients here or in extra class?
				Thread.sleep(50);
			LOG.info("Server stopping...");
			//Saving data is handled by shutdown hook
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

	private boolean init() {

		Thread thread = new Thread(() -> {
			if (guiEnabled)
				return; //just for now to avoid IOException: "The handle is invalid"

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

	@Override
	public void tick() {
		executePendingCommands();
	}

	public void subscribeToTicker(ITickable subscriber) {
		tickables.add(subscriber);
	}

	public void unsubscribeFromTicker(ITickable subscriber) {
		tickables.remove(subscriber);
	}

	public static void main(String[] args) {
		boolean gui = true;
		int port = -1;
		int maxClients = -2;
		int tickRate = -1;

		for (int i = 0; i < args.length; i++) {
			String param = args[i];
			String nextParam = i == (args.length - 1) ? null : args[i + 1];
			boolean skipParam = false;

			if (param.equalsIgnoreCase("nogui") || param.equalsIgnoreCase("--nogui"))
				gui = false;
			else if (param.equalsIgnoreCase("--serverPort") && (nextParam != null)) {
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
			//help or ?

			if (skipParam)
				i++;
		}

		final Server server = new Server();

		if (port >= 0)
			server.setServerPort(port);

		if (maxClients >= -1)
			server.setMaxClients(maxClients);

		if (gui && !GraphicsEnvironment.isHeadless())
			server.setGuiEnabled();

		if (tickRate >= 0)
			server.setTickRate(tickRate);

		server.startServerThread();
	}

	/**
	 * Sets the serverRunning variable to false, in order to get the server to shut down.
	 */
	public void initiateShutdown() {
		LOG.info("Shutdown initiated");
		serverRunning = false;
	}

	private void startServerThread() {
		serverThread = new Thread(this, "Server thread");
		serverThread.start();
	}

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

	public void addPendingCommand(String input, ICommandSender sender) {
		pendingCommandList.add(new PendingCommand(input, sender));
	}

	public void executePendingCommands() {
		while (!pendingCommandList.isEmpty()) {
			PendingCommand command = pendingCommandList.remove(0);
			getCommandManager().executeCommand(command.getSender(), command.getCommand());
		}

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

	public ICommandManager getCommandManager() {
		return commandManager;
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

}
