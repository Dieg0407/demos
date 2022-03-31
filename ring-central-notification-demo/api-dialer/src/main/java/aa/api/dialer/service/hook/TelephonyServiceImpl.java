package aa.api.dialer.service.hook;

import aa.api.dialer.model.AnsweredCallEvent;
import aa.api.dialer.model.CallEvent;
import aa.api.dialer.model.event.Telephony;
import aa.api.dialer.model.event.Telephony.Direction;
import aa.api.dialer.model.event.Telephony.Origin;
import aa.api.dialer.model.event.Telephony.Status;
import aa.api.dialer.service.cli.ExtensionService;
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
public class TelephonyServiceImpl implements TelephonyService {
  private final ObjectMapper mapper;
  private final RestClient mainAccountClient;
  private final ExtensionService extensionService;
  private final ApplicationEventPublisher publisher;

  @Override
  public void handleIncomingEvent(String payload, String hookExtensionId) {
    try {
      final var telephonyEvent = mapper.readValue(payload, Telephony.class);
      final var body = telephonyEvent.getBody();
      if (body == null ||
          body.getOrigin() == null ||
          body.getParties() == null) {
        log.debug("Payload was not valid {}", payload);
        return;
      }

      if (body.getOrigin().getType() != Origin.Call && body.getOrigin().getType() != Origin.CallOut) {
        log.debug("Payload was not valid {}", payload);
        return;
      }

      final var callEvent = CallEvent.fromTelephony(telephonyEvent, payload, hookExtensionId);

      if (callEvent.getDirection() == Direction.Inbound &&
        callEvent.getStatus() == Status.Answered) {
        if (!hookExtensionId.equals(callEvent.getExtensionId()))
          return;

        final var extensionInfo = extensionService.findExtensionInfo(mainAccountClient, callEvent.getExtensionId());

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
      }

    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to parse the incoming message",
          e
      );
    }
  }
}
