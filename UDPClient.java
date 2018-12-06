import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient {
    
   private Encryptor crypto;
   
    
    public UDPClient(){
        crypto = new Encryptor();
    }

    public void send(String text) {
        String sn = "192.168.212.146";
        int sp = 55555;
        try {
            DatagramSocket s = new DatagramSocket();
            String message = text;
            InetAddress localAdress = InetAddress.getLocalHost();
//            byte[] enMes =crypto.encrypteMessage(buf, buf);
            String sAddress = localAdress.getHostName();
            String completeMessage = message + "//" + sAddress;
            byte[] buf = completeMessage.getBytes();
            InetAddress address = InetAddress.getByName(sn);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, 
                                    address, sp);
            s.send(packet);
            s.receive(packet);
            if(new String(packet.getData()).startsWith("OK!",3)){
                boolean success = sendPeers(packet);
            }
            System.out.println("Received from gramma: " + new String(buf));
        } catch (SocketException ex) {
            System.out.println("Ooops" + ex);
        } catch (UnknownHostException ex) {
            System.out.println("Ooops" + ex);
        } catch (IOException ex) {
            System.out.println("Ooops" + ex);
        }
        
    }
    
    public boolean sendPeers(DatagramPacket info){
       
        try{
        DatagramSocket s = new DatagramSocket();
        String[] newDest = new String(info.getData()).split("//");
        if(newDest.length > 2){
            return false;
        }
        InetAddress address = InetAddress.getByName(newDest[1]);
        InetAddress localAdress = InetAddress.getLocalHost();
        String sAddress = localAdress.getHostName();
        String completeMessage = newDest[0] + "//" + sAddress;
        byte[] buf = completeMessage.getBytes();
        DatagramPacket newPacket = new DatagramPacket(buf, buf.length, address, 55555);
        s.send(newPacket);
        s.receive(newPacket);
        return true;
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
    
}
