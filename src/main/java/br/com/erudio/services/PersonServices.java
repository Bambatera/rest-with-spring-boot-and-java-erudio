package br.com.erudio.services;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository personRepository;

    public List<PersonVO> findAll() {
        logger.info("Finding people...");
        return DozerMapper.parseListObjects(this.personRepository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person...");
        Person person = this.personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return DozerMapper.parseObject(person, PersonVO.class);
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person...");
        Person entity = DozerMapper.parseObject(person, Person.class);
        PersonVO vo = DozerMapper.parseObject(this.personRepository.save(entity), PersonVO.class);
        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updating one person...");
        Person entity = this.personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setAddress(person.getAddress());
        entity.setFirstName(person.getFirstName());
        entity.setGender(person.getGender());
        entity.setLastName(person.getLastName());
        return DozerMapper.parseObject(this.personRepository.save(entity), PersonVO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person...");
        Person entity = this.personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        this.personRepository.delete(entity);
    }
}
