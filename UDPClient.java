import java.io.IOException;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class UDPClient {
    
   private final Encryption crypto;
   private ArrayList<InetAddress> servers;
   private Client client;
   private String header;
    
    public UDPClient(){
        crypto = new Encryption();
    }

    public boolean connect() {
        String sn = "DESKTOP-IJENANE";
        //String sn = client.clientHostAddressField.getText();
        int sp = 55555;
        try {
            DatagramSocket s = new DatagramSocket();
            String message = "Request";
            byte[] enMes = message.getBytes();
            //byte[] enMes =crypto.encrypteMessage(buf);
            InetAddress address = InetAddress.getByName(sn);
            DatagramPacket packet = new DatagramPacket(enMes, enMes.length, address, sp);
            System.out.println("Test 1");
            s.send(packet);
            Thread receive = new Thread(new receive());
            receive.start();
            System.out.println("Test 2");
           
            
        } catch (SocketException ex) {
            System.out.println("Ooops" + ex);
            return false;
        } catch (UnknownHostException ex) {
            System.out.println("Ooops" + ex);
            return false;
        } catch (IOException ex) {
            System.out.println("Ooops" + ex);
            return false;
        }/* catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
           Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
       }*/
        return true;
    }
    
    public class receive implements Runnable{
       
       @Override 
       public void run(){
       try {    
           boolean received = true;
           DatagramSocket socket = new DatagramSocket(55555);
           byte[] receivedData = new byte[1024];
           while(received){
               DatagramPacket receivePack = new DatagramPacket(receivedData, receivedData.length);
               //System.out.println("Test 3");
               socket.receive(receivePack);
               header = new String(receivePack.getData());
               if(receivePack.getData() != null){
                   received = false;
               }
               
            if(header.startsWith("First")){
                
            }
            if(header.startsWith("Ok to send")){
                sendPeers(null, null);
            }
            if(header.startsWith("Recipient")){
                
                String[] originalMessage = receivePack.toString().split("//");
                
            }
            if(header.startsWith("Stop")){
                disconnect();
            }
            if(header.startsWith("From peer")){
                
            }
            if(header.startsWith("ACK")){
                
            }
            if(header.startsWith("Resend")){
                sendPeers(null, null);
            }
           
            
           }
       } catch (SocketException ex) {
           Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
           
       } catch (IOException ex) {
           Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    }
    
    public boolean sendPeers(DatagramPacket info, String input){
       
        try{
        DatagramSocket s = new DatagramSocket();
        String[] newDest = new String(info.getData()).split("//");
        if(newDest.length > 2){
            return false;
        }
        InetAddress address = InetAddress.getByName(newDest[1]);
        InetAddress localAdress = InetAddress.getLocalHost();
        String sAddress = localAdress.getHostName();
        String completeMessage = input + "//" + sAddress;
        boolean splitOccurred = true;
        byte[] buf = completeMessage.getBytes();
        DatagramPacket newPacket = new DatagramPacket(buf, buf.length, address, 55555);
        s.send(newPacket);
        s.receive(newPacket);
        boolean nowReceived = true;
        System.out.println(newPacket + " ");
        //return true;
        
        
        
        if(completeMessage.startsWith("Permission")){
            //1. "Permission" to server
            return true;
        }
        if(nowReceived){
            //2. recieve message from server
            return true;
        }
        if(completeMessage.startsWith("okToSend")){
            //3. starts with "okToSend"
            return true;
        }
        if(splitOccurred){
            //4. split by "//" = data[]
            return true;
        }
        if(newDest[1].equals(input)){
            //5. data[1] = message  
            return true;
        }
        if(newDest[2].equals(sAddress)){
            //6. data[2] = source peer
            return true;
        }
        
        
        }
        catch (UnknownHostException ex){
            System.out.println("Oops" + ex);
        } catch (SocketException ex) {
           Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
           Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       return false;
    }

    public boolean disconnect() {
        String sn = "10.18.40.20";
        int sp = 55555;
        try {
            DatagramSocket s = new DatagramSocket();
            String message = "Disconnect";
            byte[] enMes = message.getBytes();
            //byte[] enMes = crypto.encrypteMessage(buf);
            InetAddress address = InetAddress.getByName(sn);
            DatagramPacket packet = new DatagramPacket(enMes, enMes.length,address, sp);
            s.send(packet);
            Thread receive = new Thread(new receive());
            receive.start();
           
            System.out.println("Received from gramma: " + new String(enMes));
        } catch (SocketException ex) {
            System.out.println("Ooops" + ex);
            return false;
        } catch (UnknownHostException ex) {
            System.out.println("Ooops" + ex);
            return false;
        } catch (IOException ex) {
            System.out.println("Ooops" + ex);
            return false;
        } /*catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
           Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
       }*/
        return true;
        
    }
    
}

