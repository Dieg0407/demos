package aa.api.dialer.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@ToString
@Builder(toBuilder = true)
@Jacksonized
@EqualsAndHashCode
public class InboundCallNotification {
  private Lead lead;
  private String userEmail;
  private String leadPhoneNumber;
}
