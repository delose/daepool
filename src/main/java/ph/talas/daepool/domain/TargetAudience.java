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

/**
 * A TargetAudience.
 */
@Entity
@Table(name = "target_audience")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "targetaudience")
public class TargetAudience implements Serializable {

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
    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "createtime")
    private LocalDate createtime;

    @Column(name = "refreshtime")
    private LocalDate refreshtime;

    @NotNull
    @Column(name = "criteria", nullable = false)
    private String criteria;

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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public LocalDate getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDate createtime) {
        this.createtime = createtime;
    }

    public LocalDate getRefreshtime() {
        return refreshtime;
    }

    public void setRefreshtime(LocalDate refreshtime) {
        this.refreshtime = refreshtime;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
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
        TargetAudience targetAudience = (TargetAudience) o;
        return Objects.equals(id, targetAudience.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TargetAudience{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", size='" + size + "'" +
            ", createtime='" + createtime + "'" +
            ", refreshtime='" + refreshtime + "'" +
            ", criteria='" + criteria + "'" +
            '}';
    }
}
