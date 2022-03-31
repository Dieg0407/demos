package aa.api.dialer.service.listener;

import aa.api.dialer.model.AnsweredCallEvent;
import org.springframework.context.event.EventListener;

public interface AnsweredCallListener {

  @EventListener
  void enrichCallerData(AnsweredCallEvent answeredCallEvent);
}
