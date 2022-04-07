package aa.api.dialer.model;

import aa.api.dialer.model.event.RcTelephonyEvent;
import aa.api.dialer.model.event.RcTelephonyEvent.Recording;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
public class CallEvent {
  private UUID id;

  private String externalEventId;

  private String sessionId;

  private String telephonyId;

  private short sequence;

  private RcTelephonyEvent.Status status;

  private String reason;

  private RcTelephonyEvent.Direction direction;

  private String partyId;

  private String partyExtensionId;

  private CallParty from;

  private CallParty to;

  private Boolean missedCall;

  private OffsetDateTime eventTime;

  private String event;

  private OffsetDateTime createdDate;

  private String hookExtensionId;

  @JsonIgnore
  private List<String> recordingIds;

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

  public static CallEvent fromTelephony(RcTelephonyEvent event, String payload, String hookExtensionId) {
    if (event.getBody() == null) {
      return CallEvent.builder()
          .externalEventId(event.getUuid())
          .hookExtensionId(hookExtensionId)
          .event(payload)
          .build();
    }

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
          callBuilder.partyExtensionId(party.getExtensionId());
          callBuilder.recordingIds(party.getRecordings() != null ?
              party.getRecordings().stream().map(Recording::getId).collect(Collectors.toList()) :
              new ArrayList<>()
          );

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
