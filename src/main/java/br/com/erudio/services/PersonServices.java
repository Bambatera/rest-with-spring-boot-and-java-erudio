package br.com.erudio.services;

import br.com.erudio.exceptions.ResourceNotFoundException;
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

    public List<Person> findAll() {
        logger.info("Finding people...");
        return this.personRepository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding one person...");
        return this.personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }

    public Person create(Person person) {
        logger.info("Creating one person...");
        return this.personRepository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating one person...");
        Person entity = this.personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setAddress(person.getAddress());
        entity.setFirstName(person.getFirstName());
        entity.setGender(person.getGender());
        entity.setLastName(person.getLastName());
        return this.personRepository.save(entity);
    }

    public void delete(Long id) {
        logger.info("Deleting one person...");
        Person entity = this.personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        this.personRepository.delete(entity);
    }
}
