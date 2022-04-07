package aa.api.dialer.service.listener;

import aa.api.dialer.model.event.AnsweredCallEvent;
import aa.api.dialer.model.InboundCallNotification;
import aa.api.dialer.service.cli.LeadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnsweredCallListenerImpl implements AnsweredCallListener {
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final ObjectMapper mapper;
  private final LeadService leadService;

  @Override
  @EventListener
  public void enrichCallerData(AnsweredCallEvent answeredCallEvent) {
    final var lead = leadService.findLeadByPhoneNumber(answeredCallEvent.getCallerPhoneNumber());

    final var notification = InboundCallNotification.builder()
        .userEmail(answeredCallEvent.getUserEmail())
        .lead(lead.isEmpty() ? null : lead.get())
        .leadPhoneNumber(answeredCallEvent.getCallerPhoneNumber())
        .build();

    final var base64 = Base64.getEncoder()
        .encodeToString(
            answeredCallEvent.getUserEmail().getBytes(StandardCharsets.UTF_8)
        );

    try {
      simpMessagingTemplate.convertAndSend("/topic/answered-calls/"+base64, mapper.writeValueAsBytes(notification));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
