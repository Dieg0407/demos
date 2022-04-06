package aa.api.dialer.service;

import aa.api.dialer.model.UserInfo;
import aa.api.dialer.model.UserInfo.UserPresence;

public interface UserService {
  UserInfo whoAmI(String authToken);

  void updatePresence(String authToken, UserPresence presence);
}
