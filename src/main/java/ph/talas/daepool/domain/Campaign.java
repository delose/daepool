package ph.talas.daepool.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ph.talas.daepool.domain.enumeration.Status;

/**
 * A Campaign.
 */
@Entity
@Table(name = "campaign")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "campaign")
public class Campaign implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 120)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Column(name = "description", length = 120, nullable = false)
    private String description;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "starttime", nullable = false)
    private LocalDate starttime;

    @NotNull
    @Column(name = "endtime", nullable = false)
    private LocalDate endtime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalDate starttime) {
        this.starttime = starttime;
    }

    public LocalDate getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalDate endtime) {
        this.endtime = endtime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Campaign campaign = (Campaign) o;
        return Objects.equals(id, campaign.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", type='" + type + "'" +
            ", starttime='" + starttime + "'" +
            ", endtime='" + endtime + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
