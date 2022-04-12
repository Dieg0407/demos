package aa.api.dialer.service.hook;

import aa.api.dialer.db.operations.RcCallEventOperations;
import aa.api.dialer.model.CallEvent;
import aa.api.dialer.model.event.AnsweredCallEvent;
import aa.api.dialer.model.event.RcTelephonyEvent;
import aa.api.dialer.model.event.RcTelephonyEvent.Direction;
import aa.api.dialer.model.event.RcTelephonyEvent.Status;
import aa.api.dialer.service.validator.TelephonyValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingCallHookServiceImpl implements TrackingCallHookService {
  private final RcCallEventOperations operations;
  private final ObjectMapper mapper;
  private final TelephonyValidator validator;

  @Override
  public void handle(String payload) {
    try {
      final var telephonyEvent = mapper.readValue(payload, RcTelephonyEvent.class);
      if (validator.isNotValid(telephonyEvent, payload))
        return;

      final var callEvent = CallEvent.fromTelephony(telephonyEvent, payload, null);
      operations.create(callEvent);

      // disconnected event
      if (callEvent.getPartyExtensionId() != null &&
          callEvent.getStatus() == Status.Disconnected &&
          (callEvent.getReason() == null || "voicemail".equalsIgnoreCase(callEvent.getReason()))
      ) {
        log.info(
            "This call has been concluded. [User ext: {} | Direction: {} | Lead number {}]",
            callEvent.getPartyExtensionId(),
            callEvent.getDirection(),
            callEvent.getDirection() == Direction.Inbound ? callEvent.getFrom().getPhoneNumber() : callEvent.getTo().getPhoneNumber()
        );
      }

      // answered event
      if (callEvent.getPartyExtensionId() != null &&
          callEvent.getStatus() == Status.Answered &&
          callEvent.getRecordingIds().isEmpty()) {
        log.info("This call has been answered. [User ext: {} | Direction: {} | Lead number {}]",
            callEvent.getPartyExtensionId(),
            callEvent.getDirection(),
            callEvent.getDirection() == Direction.Inbound ? callEvent.getFrom().getPhoneNumber() : callEvent.getTo().getPhoneNumber()
        );
      }
    }
    catch (JsonProcessingException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to parse the incoming message",
          e
      );
    }
  }
}
