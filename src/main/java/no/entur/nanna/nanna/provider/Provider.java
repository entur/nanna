package no.entur.nanna.nanna.provider;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provider_seq")
    @SequenceGenerator(name = "provider_seq", sequenceName = "provider_seq", allocationSize = 1)
    public Long id;
    public String name;
    @OneToOne(cascade = {CascadeType.ALL})
    public ChouetteInfo chouetteInfo;

    public Provider(){}

    public Provider(Long id, String name, ChouetteInfo chouetteInfo) {
        this.id = id;
        this.name = name;
        this.chouetteInfo = chouetteInfo;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chouetteInfo=" + chouetteInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Provider provider = (Provider) o;

        if (!Objects.equals(id, provider.id)) {
            return false;
        }
        if (!Objects.equals(name, provider.name)) {
            return false;
        }
        return Objects.equals(chouetteInfo, provider.chouetteInfo);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (chouetteInfo != null ? chouetteInfo.hashCode() : 0);
        return result;
    }

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

    public ChouetteInfo getChouetteInfo() {
        return chouetteInfo;
    }

    public void setChouetteInfo(ChouetteInfo chouetteInfo) {
        this.chouetteInfo = chouetteInfo;
    }
}
