package aa.api.dialer.service.hook;

public interface TelephonyService {

  void handleIncomingEvent(String payload, String hookExtensionId);
}
