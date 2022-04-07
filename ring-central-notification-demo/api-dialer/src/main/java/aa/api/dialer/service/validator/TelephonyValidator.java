package aa.api.dialer.service.validator;

import aa.api.dialer.model.event.RcTelephonyEvent;
import aa.api.dialer.model.event.RcTelephonyEvent.Origin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TelephonyValidator {
  public boolean isNotValid(RcTelephonyEvent telephonyEvent, String payload) {
    final var body = telephonyEvent.getBody();
    if (body == null ||
        body.getOrigin() == null ||
        body.getParties() == null) {
      log.warn("Payload was not valid {}", payload);
      return true;
    }

    if (body.getOrigin().getType() != Origin.Call && body.getOrigin().getType() != Origin.CallOut) {
      log.warn("Payload was not valid {}", payload);
      return true;
    }

    return false;
  }
}
