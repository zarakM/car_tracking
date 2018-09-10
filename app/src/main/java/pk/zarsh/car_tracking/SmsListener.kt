package pk.zarsh.car_tracking

/**
 * Created by HP on 6/19/18.
 */
public interface SmsListener {
    fun messageReceived(messageText: String){}
}