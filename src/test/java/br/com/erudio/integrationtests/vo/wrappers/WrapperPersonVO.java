package br.com.erudio.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperPersonVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("_embedded")
    private PersonEmbeddedVO embedded;

    public WrapperPersonVO() {
    }

    public PersonEmbeddedVO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PersonEmbeddedVO embedded) {
        this.embedded = embedded;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.embedded);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WrapperPersonVO other = (WrapperPersonVO) obj;
        return Objects.equals(this.embedded, other.embedded);
    }
    
}
