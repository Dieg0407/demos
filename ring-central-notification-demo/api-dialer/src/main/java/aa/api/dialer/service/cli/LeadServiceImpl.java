package aa.api.dialer.service.cli;

import aa.api.dialer.model.Lead;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LeadServiceImpl implements LeadService {
  private final Map<String, Lead> leads;

  public LeadServiceImpl() {
    leads = new ConcurrentHashMap<>();
    leads.put("+17202786767",
        Lead.builder()
            .id(UUID.randomUUID())
            .name("Ben")
            .lastName("Tagtow")
        .build());
  }

  @Override
  public Optional<Lead> findLeadByPhoneNumber(String phoneNumber) {
    return Optional.ofNullable(leads.getOrDefault(phoneNumber, null));
  }
}
