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
    private DatagramPacket toSave;
    private DatagramSocket serverSocket;
    
    public UDPServer(){
        clients = new ArrayList<InetAddress>();
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
            String message = "";
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
    
    public void addToNetwork() {
        if(clients.size() == 0) {
            sendFirstNotification();
        }
        clients.add(toSave.getAddress());
    }
    
    public void beginService() {
        if(clients.size() < 2) {
            
        }
        else {
            int max = clients.size();
            int count = 0;
            while(count != max) {
                
            }
        }
    }
    
    
}
