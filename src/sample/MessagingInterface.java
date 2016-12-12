package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by yurireis on 10/12/16.
 */
public interface MessagingInterface extends Remote {
    void sendRemoteMessage(String msg, Integer player) throws RemoteException;
    String getRemoteMessage(Integer player) throws RemoteException;
    Integer getMyPlayer() throws RemoteException;
}
