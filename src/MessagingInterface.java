import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by yurireis on 10/12/16.
 */
public interface MessagingInterface extends Remote {
    void sendRemoteMessage(String msg, Integer player) throws RemoteException;
    List<String> getRemoteMessage(Integer player) throws RemoteException;
    Integer getMyPlayer() throws RemoteException;
}
