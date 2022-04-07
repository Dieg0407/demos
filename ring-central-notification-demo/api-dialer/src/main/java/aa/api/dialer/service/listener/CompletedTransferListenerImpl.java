package aa.api.dialer.service.listener;

import aa.api.dialer.db.operations.RcCallEventOperations;
import aa.api.dialer.model.CallEvent;
import aa.api.dialer.model.CallEvent.CallParty;
import aa.api.dialer.model.InboundCallNotification;
import aa.api.dialer.model.Lead;
import aa.api.dialer.model.event.CompletedTransferEvent;
import aa.api.dialer.model.event.RcTelephonyEvent;
import aa.api.dialer.model.event.RcTelephonyEvent.CallStatus;
import aa.api.dialer.model.event.RcTelephonyEvent.Party;
import aa.api.dialer.model.event.RcTelephonyEvent.Peer;
import aa.api.dialer.service.cli.LeadService;
import aa.api.dialer.service.cli.rc.ExtensionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringcentral.RestClient;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedTransferListenerImpl implements CompletedTransferListener {
  private final ObjectMapper mapper;
  private final RestClient mainAccountClient;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final LeadService leadService;
  private final ExtensionService extensionService;
  private final RcCallEventOperations callEventOperations;

  @Override
  @EventListener
  public void createAnsweredByTransferEvent(CompletedTransferEvent completedTransferEvent) {
    final var callEvent = completedTransferEvent.getEvent();
    final var peerSessionId = findTransferSessionId(callEvent.getEvent());

    if (peerSessionId.isBlank()) {
      log.info("Wasn't able to find the peer session id");
      return;
    }
    final var peerExtensionId = callEventOperations.findAllBySessionId(peerSessionId)
        .stream()
        .map(CallEvent::getTo)
        .map(CallParty::getExtensionId)
        .filter(Objects::nonNull)
        .distinct()
        .findFirst();

    if (peerExtensionId.isEmpty()) {
      log.info("Wasn't able to find the peer extension id");
      return;
    }
    final var extensionInfo = extensionService.findExtensionInfo(mainAccountClient, peerExtensionId.get());
    var fromNumber = callEvent.getFrom().getPhoneNumber();
    final var lead = leadService.findLeadByPhoneNumber(fromNumber);

    // direct call or from queue
    if (fromNumber.contains("+")) {
      log.info(
          "The user with mail {} answered a call from {} [{}]",
          extensionInfo.contact.email,
          callEvent.getFrom().getPhoneNumber(),
          peerSessionId
      );
      sendNotification(
          extensionInfo.contact.email,
          fromNumber,
          lead.orElse(null)
      );
    }
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

  private String findTransferSessionId(String sourceEvent){
    try {
      final var telephony = mapper.readValue(sourceEvent, RcTelephonyEvent.class);

      return telephony.getBody()
          .getParties()
          .stream()
          .findFirst()
          .map(Party::getStatus)
          .map(CallStatus::getPeerId)
          .map(Peer::getSessionId)
          .orElse("");
    }
    catch (JsonProcessingException | NullPointerException e) {
      return "";
    }
  }
}
