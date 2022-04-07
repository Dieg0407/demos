package aa.api.dialer.service.listener;

import aa.api.dialer.model.event.CompletedTransferEvent;
import org.springframework.context.event.EventListener;

public interface CompletedTransferListener {

  @EventListener
  void createAnsweredByTransferEvent(CompletedTransferEvent completedTransferEvent);
}
