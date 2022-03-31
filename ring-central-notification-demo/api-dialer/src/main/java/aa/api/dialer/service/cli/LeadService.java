package aa.api.dialer.service.cli;

import aa.api.dialer.model.Lead;
import java.util.Optional;

public interface LeadService {
  Optional<Lead> findLeadByPhoneNumber(String phoneNumber);
}
