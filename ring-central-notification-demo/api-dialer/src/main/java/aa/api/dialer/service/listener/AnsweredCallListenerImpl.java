package aa.api.dialer.service.listener;

import aa.api.dialer.model.Lead;
import aa.api.dialer.model.event.AnsweredCallEvent;
import aa.api.dialer.model.InboundCallNotification;
import aa.api.dialer.service.cli.LeadService;
import aa.api.dialer.service.cli.rc.ExtensionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringcentral.RestClient;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnsweredCallListenerImpl implements AnsweredCallListener {
  private final ObjectMapper mapper;
  private final RestClient mainAccountClient;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final LeadService leadService;
  private final ExtensionService extensionService;

  @Override
  @EventListener
  public void enrichCallerData(AnsweredCallEvent answeredCallEvent) {
    final var callEvent = answeredCallEvent.getEvent();
    final var extensionInfo = extensionService.findExtensionInfo(mainAccountClient,
        callEvent.getPartyExtensionId());

    final var lead = leadService.findLeadByPhoneNumber(callEvent.getFrom().getPhoneNumber());
    var fromNumber = callEvent.getFrom().getPhoneNumber();

    // direct call or from queue
    if (fromNumber.contains("+")) {
      log.info(
          "The user with mail {} answered a call from {} [{}]",
          extensionInfo.contact.email,
          callEvent.getFrom().getPhoneNumber(),
          callEvent.getSessionId()
      );
      sendNotification(
          extensionInfo.contact.email,
          fromNumber,
          lead.orElse(null)
      );
      return;
    }
    log.info("the from number may be a transfer or an internal call [{}]", fromNumber);
  }

 private void sendNotification(String email, String fromPhone, Lead lead) {
    final var notification = InboundCallNotification.builder()
        .userEmail(email)
        .lead(lead)
        .leadPhoneNumber(fromPhone)
        .build();

    final var base64 = Base64.getEncoder().encodeToString(email.getBytes(StandardCharsets.UTF_8));

    try {
      simpMessagingTemplate.convertAndSend("/topic/answered-calls/"+base64, mapper.writeValueAsBytes(notification));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
