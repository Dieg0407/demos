package aa.api.dialer.service.hook;

import aa.api.dialer.model.event.AnsweredCallEvent;
import aa.api.dialer.model.CallEvent;
import aa.api.dialer.model.event.RcTelephonyEvent;
import aa.api.dialer.model.event.RcTelephonyEvent.Direction;
import aa.api.dialer.model.event.RcTelephonyEvent.Status;
import aa.api.dialer.service.cli.rc.ExtensionService;
import aa.api.dialer.service.validator.TelephonyValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringcentral.RestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomingCallHookServiceImpl implements IncomingCallHookService {
  private final ObjectMapper mapper;
  private final RestClient mainAccountClient;
  private final ExtensionService extensionService;
  private final ApplicationEventPublisher publisher;
  private final TelephonyValidator validator;

  @Override
  public void handle(String payload, String hookExtensionId) {
    try {
      final var telephonyEvent = mapper.readValue(payload, RcTelephonyEvent.class);
      if (!validator.isNotValid(telephonyEvent, payload))
        return;

      final var callEvent = CallEvent.fromTelephony(telephonyEvent, payload, hookExtensionId);
      // we only care for answered event
      if (callEvent.getDirection() != Direction.Inbound || callEvent.getStatus() != Status.Answered) {
        return;
      }

      if (!hookExtensionId.equals(callEvent.getPartyExtensionId()))
        return;

      final var extensionInfo = extensionService.findExtensionInfo(mainAccountClient, callEvent.getPartyExtensionId());
      log.info(
          "The user with mail {} answered a call from {}",
          extensionInfo.contact.email,
          callEvent.getFrom().getPhoneNumber()
      );

      publisher.publishEvent(new AnsweredCallEvent(
          this,
          extensionInfo.contact.email,
          callEvent.getFrom().getPhoneNumber())
      );
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to parse the incoming message",
          e
      );
    }
  }


}
