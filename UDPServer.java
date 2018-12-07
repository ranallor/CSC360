/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ranal
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class UDPServer {
    

    private ArrayList<InetAddress> clients;
    private DatagramPacket toSave;
    private DatagramSocket serverSocket;
    private Boolean running;
    private JTextArea textBox;
    
    public UDPServer(JTextArea textArea) throws SocketException {
        clients = new ArrayList<InetAddress>();
        running = false;
        textBox = textArea;
        serverSocket = new DatagramSocket(55555);
    }
    
    
    public class listen implements Runnable {
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        @Override
        public void run() {
            while(true) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String sentence = new String( receivePacket.getData());
                    if(sentence.startsWith("Request")) {
                        toSave = receivePacket;
                        addToNetwork();
                    }
                    else if(sentence.startsWith("Permission")) {
                        toSave = receivePacket;
                        messagePermission();
                    }
                    System.out.println("RECEIVED: " + sentence);
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = 55555;
                    String capitalizedSentence = sentence;
                    sendData = capitalizedSentence.getBytes();
                    DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                }
                catch (Exception ex) {}
            }    
        }
        
    }
    
    public void sendFirstNotification() { 
        InetAddress address = toSave.getAddress();
        int port = toSave.getPort();
        try {
            String message = "First";
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, 
                                    address, port);
            serverSocket.send(packet);
        } catch (SocketException ex) {
            System.out.println("Ooops" + ex);
        } catch (UnknownHostException ex) {
            System.out.println("Ooops" + ex);
        } catch (IOException ex) {
            System.out.println("Ooops" + ex);
        }
        
    }
    
    public void ListenThread() 
    {
         Thread listener = new Thread(new listen());
         listener.start();
    }
    
    public void addToNetwork() { // This adds a user to the network environment
        if(clients.size() == 0) {
            sendFirstNotification();
            textBox.append("First user added");
        }
        if(clients.size() >= 2) {
            sendStopNotification();
            textBox.append("Adding another user, stopping and restarting network service");
        }
        clients.add(toSave.getAddress());
        if(clients.size() >=2) {
            beginService();
            textBox.append("User added, network service activated");
        }
    }
    
    public void beginService() { //This sends the users their neighbor's IP info, The Client is okay to start transmitting packets.
        if(clients.size() < 2) {
            //TODO INFORM CLIENT THE NETWORK CANT RUN BECAUSE THERE IS ONLY ONE USER
        }
        else {
            int max = clients.size() - 1;
            int count = 0;
            while(count != max) {
                InetAddress address = clients.get(count);
                int port = 55555;
                try {
                    String message = "Recipient//" + clients.get(count + 1);
                    byte[] buf = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, 
                                    address, port);
                    serverSocket.send(packet);
                    count++;
                } catch (SocketException ex) {
                    System.out.println("Ooops" + ex);
                } catch (UnknownHostException ex) {
                    System.out.println("Ooops" + ex);
                } catch (IOException ex) {
                    System.out.println("Ooops" + ex);
                }
            }
            try {
                String message = "Recipient//" + clients.get(0);
                byte[] buf = message.getBytes();
                InetAddress address = clients.get(max);
                int port = 55555;
                DatagramPacket packet = new DatagramPacket(buf, buf.length, 
                                    address, port);
                serverSocket.send(packet);
                running = true;
            } catch (SocketException ex) {
                System.out.println("Ooops" + ex);
            } catch (UnknownHostException ex) {
                System.out.println("Ooops" + ex);
            } catch (IOException ex) {
                System.out.println("Ooops" + ex);
            }
        }
    }
    
    public void messagePermission() {
        String message = new String(toSave.getData());
        String[] data = message.split("//");
    }

    public void sendStopNotification() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
    
}
