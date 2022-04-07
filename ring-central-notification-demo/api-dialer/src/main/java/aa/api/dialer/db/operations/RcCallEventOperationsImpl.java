package aa.api.dialer.db.operations;

import aa.api.dialer.db.RcCallEventRepository;
import aa.api.dialer.db.converter.RcCallEventConverter;
import aa.api.dialer.model.CallEvent;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RcCallEventOperationsImpl implements RcCallEventOperations {
  private final RcCallEventConverter converter;
  private final RcCallEventRepository repository;

  @Override
  @Transactional
  public UUID create(CallEvent model) {
    return repository.save(converter.fromModel(model)).getId();
  }

  @Override
  public List<CallEvent> findAllBySessionId(String sessionId) {
    return repository.findBySessionIdOrderBySequence(sessionId)
        .stream()
        .map(converter::fromEntity)
        .collect(Collectors.toList());
  }
}
