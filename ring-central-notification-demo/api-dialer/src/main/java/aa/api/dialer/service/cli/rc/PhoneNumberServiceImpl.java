package aa.api.dialer.service.cli.rc;

import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.definitions.GetExtensionPhoneNumbersResponse;
import com.ringcentral.definitions.UserPhoneNumberExtensionInfo;
import com.ringcentral.definitions.UserPhoneNumberInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

  @Override
  public List<UserPhoneNumberInfo> getExtensionPhoneNumber(RestClient authenticatedClient) {
    try {
      return Arrays.stream(
          authenticatedClient
              .restapi()
              .account()
              .extension()
              .phoneNumber()
              .get()
              .records
      ).collect(Collectors.toList());
    } catch (RestException | IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Failed to fetch the user phone numbers",
          e
      );
    }
  }
}
