package br.com.erudio.integrationtests.vo.wrappers;

import br.com.erudio.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PersonEmbeddedVO  implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("personVOList")
    private List<PersonVO> persons;

    public PersonEmbeddedVO() {
    }

    public List<PersonVO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonVO> persons) {
        this.persons = persons;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.persons);
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
        final PersonEmbeddedVO other = (PersonEmbeddedVO) obj;
        return Objects.equals(this.persons, other.persons);
    }
    
}
