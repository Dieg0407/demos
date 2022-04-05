package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import com.ringcentral.definitions.UserPhoneNumberInfo;
import java.util.List;

public interface PhoneNumberService {
  List<UserPhoneNumberInfo> getExtensionPhoneNumber(RestClient authenticatedClient);
}
