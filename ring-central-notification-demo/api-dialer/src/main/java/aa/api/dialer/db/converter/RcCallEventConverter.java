package aa.api.dialer.db.converter;

import aa.api.dialer.db.entity.RcCallEventEntity;
import aa.api.dialer.model.CallEvent;
import org.springframework.stereotype.Component;

@Component
public class RcCallEventConverter {

  public RcCallEventEntity fromModel(CallEvent model) {
    final var builder = RcCallEventEntity.builder()
        .externalEventId(model.getExternalEventId())
        .sessionId(model.getSessionId())
        .telephonyId(model.getTelephonyId())
        .sequence(model.getSequence())
        .status(model.getStatus())
        .reason(model.getReason())
        .direction(model.getDirection())
        .missedCall(model.getMissedCall())
        .eventTime(model.getEventTime())
        .hookExtensionId(model.getHookExtensionId())
        .partyExtensionId(model.getPartyExtensionId())
        .partyId(model.getPartyId())
        .event(model.getEvent());

    if (model.getFrom() != null )
      builder.fromDeviceId(model.getFrom().getDeviceId())
          .fromName(model.getFrom().getName())
          .fromExtensionId(model.getFrom().getExtensionId())
          .fromPhoneNumber(model.getFrom().getPhoneNumber());

    if (model.getTo() != null)
      builder.toDeviceId(model.getTo().getDeviceId())
          .toName(model.getTo().getName())
          .toExtensionId(model.getTo().getExtensionId())
          .toPhoneNumber(model.getTo().getPhoneNumber());

    return builder.build();
  }

  public CallEvent fromEntity(RcCallEventEntity entity) {
    final var to = CallEvent.CallParty.builder()
        .deviceId(entity.getToDeviceId())
        .extensionId(entity.getToExtensionId())
        .name(entity.getToName())
        .phoneNumber(entity.getToPhoneNumber())
        .build();

    final var from = CallEvent.CallParty.builder()
        .deviceId(entity.getFromDeviceId())
        .extensionId(entity.getFromExtensionId())
        .name(entity.getFromName())
        .phoneNumber(entity.getFromPhoneNumber())
        .build();

    return CallEvent.builder()
        .id(entity.getId())
        .externalEventId(entity.getExternalEventId())
        .sessionId(entity.getSessionId())
        .telephonyId(entity.getTelephonyId())
        .sequence(entity.getSequence())
        .status(entity.getStatus())
        .reason(entity.getReason())
        .direction(entity.getDirection())
        .missedCall(entity.getMissedCall())
        .eventTime(entity.getEventTime())
        .event(entity.getEvent())
        .hookExtensionId(entity.getHookExtensionId())
        .partyId(entity.getPartyId())
        .partyExtensionId(entity.getPartyExtensionId())
        .from(from)
        .to(to)
        .createdDate(entity.getCreatedDate())
        .build();
  }
}
