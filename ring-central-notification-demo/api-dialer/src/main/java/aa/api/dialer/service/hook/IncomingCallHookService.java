package aa.api.dialer.service.hook;

public interface IncomingCallHookService {
  void handle(String payload, String hookExtensionId);
}
