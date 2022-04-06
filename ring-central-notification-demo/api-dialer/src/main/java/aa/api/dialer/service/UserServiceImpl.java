package aa.api.dialer.service;

import static java.lang.String.format;

import aa.api.dialer.model.UserInfo;
import aa.api.dialer.model.UserInfo.Device;
import aa.api.dialer.model.UserInfo.PhoneNumber;
import aa.api.dialer.model.UserInfo.UserPresence;
import aa.api.dialer.service.cli.rc.DeviceService;
import aa.api.dialer.service.cli.rc.ExtensionService;
import aa.api.dialer.service.cli.rc.PhoneNumberService;
import aa.api.dialer.service.cli.rc.PresenceService;
import aa.api.dialer.service.factory.RestClientFactory;
import com.ringcentral.definitions.ExtensionDeviceResponse;
import com.ringcentral.definitions.PresenceInfoRequest;
import com.ringcentral.definitions.UserPhoneNumberInfo;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final DeviceService deviceService;
  private final PhoneNumberService phoneNumberService;
  private final ExtensionService extensionService;
  private final PresenceService presenceService;
  private final RestClientFactory factory;

  @Override
  public UserInfo whoAmI(String authToken) {
    final var client = factory.createClient(authToken);
    final var ext = extensionService.findLoggedInUserExtension(client);
    final var devices = deviceService.getExtensionDevices(client);
    final var phones = phoneNumberService.getExtensionPhoneNumber(client);
    final var presence = presenceService.getPresence(client);

    return UserInfo.builder()
        .email(ext.contact.email)
        .name(ext.name)
        .devices(
            devices.stream().map(this::mapDevice).collect(Collectors.toList())
        )
        .numbers(
            phones.stream().map(this::mapPhone).collect(Collectors.toList())
        )
        .presence(
            UserPresence.builder()
                .allowQueueCalls(
                    "TakeAllCalls".equals(presence.dndStatus) ||
                    "TakeDepartmentCallsOnly".equals(presence.dndStatus)
                ).build()
        )
        .build();
  }

  @Override
  public void updatePresence(String authToken, UserPresence presenceToUpdate) {
    if (presenceToUpdate.getAllowQueueCalls() == null)
      return;

    final var client = factory.createClient(authToken);
    final var presence = presenceService.getPresence(client);

    final var request = new PresenceInfoRequest()
        .allowSeeMyPresence(presence.allowSeeMyPresence)
        .ringOnMonitoredCall(presence.ringOnMonitoredCall)
        .pickUpCallsOnHold(presence.pickUpCallsOnHold)
        .userStatus(presence.userStatus);

    if (presenceToUpdate.getAllowQueueCalls())
      request.dndStatus("TakeAllCalls");
    else
      request.dndStatus("DoNotAcceptDepartmentCalls");

    presenceService.updatePresence(client, request);
  }

  private PhoneNumber mapPhone(UserPhoneNumberInfo userPhoneNumberInfo) {
    return PhoneNumber.builder()
        .id(userPhoneNumberInfo.id)
        .phoneNumber(userPhoneNumberInfo.phoneNumber)
        .type(userPhoneNumberInfo.usageType)
        .features(Arrays.asList(userPhoneNumberInfo.features))
        .build();
  }

  private Device mapDevice(ExtensionDeviceResponse extensionDeviceResponse) {
    return Device.builder()
        .id(extensionDeviceResponse.id)
        .description(format("[%s] %s", extensionDeviceResponse.type, extensionDeviceResponse.name))
        .isOnline("Online".equals(extensionDeviceResponse.status))
        .build();
  }
}
