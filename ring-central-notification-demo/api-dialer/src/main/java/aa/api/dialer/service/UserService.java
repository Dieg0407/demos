package aa.api.dialer.service;

import aa.api.dialer.model.UserInfo;

public interface UserService {

  UserInfo whoAmI(String authToken);
}
