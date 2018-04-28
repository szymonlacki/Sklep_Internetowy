package server.net;

import server.WritableGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PortListener extends Thread {


    private ArrayList<ClientListener> testHandlerList;
    private ServerSocket serverSocket;

    private WritableGUI gui;


    public PortListener(WritableGUI gui) {
        this.gui = gui;
        testHandlerList = new ArrayList<>();
    }


    @Override
    public void run() {
        System.out.println("Port Listener started");
        gui.write("Port Listener Started");

        try {


            try {
                serverSocket = new ServerSocket(2137);

                ClientListener listener;

                while (true) {
                    if (this.isInterrupted()) {
                        break;
                    }

                    Socket s = serverSocket.accept();
                    if (!testHandlerList.contains(s)) {
                        listener = new ClientListener(s, gui);
                        testHandlerList.add(listener);
                        listener.start();
                    }
                }

            } catch (IOException e) {

                for (ClientListener aTestHandlerList : testHandlerList) {
                    (aTestHandlerList).interrupt();
                }


            } finally {
                try {
                    if (serverSocket != null)
                        if (!serverSocket.isClosed())
                            serverSocket.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }


}
