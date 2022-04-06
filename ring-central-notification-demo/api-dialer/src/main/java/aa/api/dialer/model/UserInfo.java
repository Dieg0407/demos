package aa.api.dialer.model;

import java.util.List;
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
public class UserInfo {
  private String name;
  private String email;

  private UserPresence presence;
  private List<Device> devices;
  private List<PhoneNumber> numbers;

  @Getter
  @ToString
  @Builder(toBuilder = true)
  @Jacksonized
  @EqualsAndHashCode
  public static class Device {
    private String id;
    private String description;
    private Boolean isOnline;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  @Jacksonized
  @EqualsAndHashCode
  public static class PhoneNumber {
    private long id;
    private String phoneNumber;
    private String type;
    private List<String> features;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  @Jacksonized
  @EqualsAndHashCode
  public static class UserPresence {
    private Boolean allowQueueCalls;
  }
}
