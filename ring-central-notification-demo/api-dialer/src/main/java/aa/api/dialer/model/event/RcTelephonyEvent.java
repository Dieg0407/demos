package aa.api.dialer.model.event;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RcTelephonyEvent {
  // the event doesn't have the uuid format, but it's called uuid nonetheless
  String uuid;
  String event;
  OffsetDateTime timestamp;
  UUID subscriptionId;
  String ownerId;
  Body body;

  @Value
  @Builder
  @Jacksonized
  public static class Body {
    short sequence;
    String sessionId;
    String telephonySessionId;
    String serverId;
    OffsetDateTime eventTime;
    CallOrigin origin;
    List<Party> parties;
  }

  @Value
  @Builder
  @Jacksonized
  public static class Party {
    String accountId;
    String extensionId;
    String id;
    Direction direction;
    boolean missedCall;
    boolean standAlone;
    boolean muted;
    PartyNumber from;
    PartyNumber to;
    CallStatus status;
    List<Recording> recordings;
  }

  @Value
  @Builder
  @Jacksonized
  public static class PartyNumber {
    String phoneNumber;
    String extensionId;
    String deviceId;
    String name;
  }

  @Value
  @Builder
  @Jacksonized
  public static class CallStatus {
    Status code;
    boolean rcc;
    String reason;
    Peer peerId;
  }
  @Value
  @Builder
  @Jacksonized
  public static class Peer {
    String partyId;
    String sessionId;
    String telephonySessionId;
  }

  @Value
  @Builder
  @Jacksonized
  public static class CallOrigin {
    Origin type;
  }

  @Value
  @Builder
  @Jacksonized
  public static class Recording {
    String id;
  }

  public enum Origin {
    Call,
    CallOut,
    RingOut,
    RingMe,
    Conference,
    GreetingsRecording,
    VerificationCall,
    TestCall
  }

  public enum Direction {
    Outbound,
    Inbound
  }

  public enum Status {
    Setup,
    Proceeding,
    Answered,
    Disconnected,
    Gone,
    Parked,
    Hold,
    VoiceMail,
    Voicemail,
    FaxReceive,
    VoiceMailScreening
  }
}
