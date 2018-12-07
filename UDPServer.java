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

public class UDPServer {
    

    private ArrayList<InetAddress> clients;
    private ArrayList<Integer> ports;
    private DatagramPacket toSave;
    private DatagramSocket serverSocket;
    private Boolean running;
    
    public UDPServer(){
        clients = new ArrayList<InetAddress>();
        ports = new ArrayList<Integer>();
        running = false;
    }
    
    public void listen() throws SocketException, IOException {
        serverSocket = new DatagramSocket(55555);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            if(sentence.startsWith("Request")) {
                toSave = receivePacket;
                addToNetwork();
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
    
    public void addToNetwork() { // This adds a user to the network environment
        if(clients.size() == 0) {
            sendFirstNotification();
        }
        if(clients.size() >= 2) {
            sendStopNotification();
        }
        clients.add(toSave.getAddress());
        ports.add(toSave.getPort());
        if(clients.size() >=2) {
            beginService();
        }
    }
    
    public void beginService() { //This sends the users their neighbor's IP info, The Client is okay to start transmitting packets.
        if(clients.size() < 2) {
            //TODO INFORM CLIENT THE NETWORK CANT RUN BECAUSE THERE IS ONLY ONE USER
        }
        else {
            int max = clients.size();
            int count = 0;
            while(count != max) {
                InetAddress address = clients.get(count);
                int port = ports.get(count);
                try {
                    String message = "Recipient//" + clients.get(count + 1) + "//" + ports.get(count + 1);
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
                String message = "Recipient//" + clients.get(0) + "//" + ports.get(0);
                byte[] buf = message.getBytes();
                InetAddress address = clients.get(max);
                int port = ports.get(max);
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

    private void sendStopNotification() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
