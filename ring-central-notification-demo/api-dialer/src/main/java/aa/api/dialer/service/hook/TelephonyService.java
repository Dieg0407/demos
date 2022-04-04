package aa.api.dialer.service.hook;

public interface TelephonyService {

  void handleIncomingCallEvent(String payload, String hookExtensionId);
}
