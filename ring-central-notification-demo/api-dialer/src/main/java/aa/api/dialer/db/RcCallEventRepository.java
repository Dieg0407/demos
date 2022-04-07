package aa.api.dialer.db;

import aa.api.dialer.db.entity.RcCallEventEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RcCallEventRepository extends JpaRepository<RcCallEventEntity, UUID> {
  List<RcCallEventEntity> findBySessionIdOrderBySequence(String sessionId);
}
