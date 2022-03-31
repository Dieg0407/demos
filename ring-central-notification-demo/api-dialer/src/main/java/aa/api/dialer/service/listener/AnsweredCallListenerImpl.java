package aa.api.dialer.service.listener;

import aa.api.dialer.model.AnsweredCallEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnsweredCallListenerImpl {
  SimpMessagingTemplate simpMessagingTemplate;

  @EventListener
  public void enrichCallerData(AnsweredCallEvent answeredCallEvent) {
    log.info("Answered call event");
  }
}
