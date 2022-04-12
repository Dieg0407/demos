package aa.api.dialer.db.entity;

import aa.api.dialer.model.event.RcTelephonyEvent;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rc_call_event", schema = "dialer")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
    @TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
})
public class RcCallEventEntity {
  @Id
  @Builder.Default
  private UUID id = UUID.randomUUID();

  @Column
  private String externalEventId;

  @Column
  private String sessionId;

  @Column
  private String telephonyId;

  @Column
  private short sequence;

  @Column
  private String hookExtensionId;

  @Column
  private String partyExtensionId;

  @Column
  @Enumerated(EnumType.STRING)
  private RcTelephonyEvent.Status status;

  @Column
  private String reason;

  @Column
  @Enumerated(EnumType.STRING)
  private RcTelephonyEvent.Direction direction;

  @Column
  private String partyId;

  @Column
  private OffsetDateTime eventTime;

  @Column
  private String toPhoneNumber;
  @Column
  private String toExtensionId;
  @Column
  private String toName;
  @Column
  private String toDeviceId;

  @Column
  private String fromPhoneNumber;
  @Column
  private String fromExtensionId;
  @Column
  private String fromName;
  @Column
  private String fromDeviceId;

  @Column
  private Boolean missedCall;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private String event;

  @Column
  @Builder.Default
  private OffsetDateTime createdDate = OffsetDateTime.now();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    RcCallEventEntity that = (RcCallEventEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
