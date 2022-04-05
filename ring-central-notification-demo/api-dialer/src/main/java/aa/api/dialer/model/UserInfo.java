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

  private Device devices;
  private PhoneNumber numbers;

  @Getter
  @ToString
  @Builder(toBuilder = true)
  @Jacksonized
  @EqualsAndHashCode
  public static class Device {
    private long id;
    private String description;
  }

  @Getter
  @ToString
  @Builder(toBuilder = true)
  @Jacksonized
  @EqualsAndHashCode
  public static class PhoneNumber {
    private long id;
    private String phoneNumber;
    private List<String> features;
  }
}
