package aa.api.dialer.model;

import aa.api.dialer.model.event.Telephony;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import java.util.UUID;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CallEvent {
  private UUID id;

  private String externalEventId;

  private String sessionId;

  private String telephonyId;

  private short sequence;

  private Telephony.Status status;

  private String reason;

  private Telephony.Direction direction;

  private String partyId;

  private String extensionId;

  private CallParty from;

  private CallParty to;

  private Boolean missedCall;

  private OffsetDateTime eventTime;

  private String event;

  private OffsetDateTime createdDate;

  private String hookExtensionId;

  @Getter
  @ToString
  @Builder(toBuilder = true)
  @Jacksonized
  @EqualsAndHashCode
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class CallParty {
    String phoneNumber;
    String extensionId;
    String name;
    String deviceId;
  }

  public static CallEvent fromTelephony(Telephony event, String payload, String hookExtensionId) {
    final var body = event.getBody();
    final var callBuilder = CallEvent.builder();
    body.getParties()
        .stream()
        .findFirst()
        .ifPresent(party -> {
          callBuilder.direction(party.getDirection());
          callBuilder.status(party.getStatus() == null ? null : party.getStatus().getCode());
          callBuilder.reason(party.getStatus() == null ? null : party.getStatus().getReason());
          callBuilder.missedCall(party.isMissedCall());
          callBuilder.partyId(party.getId());
          callBuilder.extensionId(party.getExtensionId());

          if (party.getTo() != null) {
            final var partyInfo = party.getTo();
            final var to = CallParty.builder()
                .deviceId(partyInfo.getDeviceId())
                .extensionId(partyInfo.getExtensionId())
                .name(partyInfo.getName())
                .phoneNumber(partyInfo.getPhoneNumber())
                .build();

            callBuilder.to(to);
          }
          if (party.getFrom() != null) {
            final var partyInfo = party.getFrom();
            final var from = CallParty.builder()
                .deviceId(partyInfo.getDeviceId())
                .extensionId(partyInfo.getExtensionId())
                .name(partyInfo.getName())
                .phoneNumber(partyInfo.getPhoneNumber())
                .build();

            callBuilder.from(from);
          }
        });

    return callBuilder
        .externalEventId(event.getUuid())
        .telephonyId(body.getTelephonySessionId())
        .sequence(body.getSequence())
        .sessionId(body.getSessionId())
        .eventTime(body.getEventTime())
        .hookExtensionId(hookExtensionId)
        .event(payload)
        .build();
  }
}
