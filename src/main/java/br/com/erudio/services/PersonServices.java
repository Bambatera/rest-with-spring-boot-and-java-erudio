package br.com.erudio.services;

import br.com.erudio.model.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<Person> findAll() {
        logger.info("Finding people...");
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }

    public Person findById(String id) {
        logger.info("Finding one person...");
        Person person = new Person(counter.incrementAndGet(), "Leandro", "Silva", "Samambaia, Brasília, Brasil", "Male");
        return person;
    }

    private Person mockPerson(int i) {
        return new Person(counter.incrementAndGet(), "A Unknown", "Person " + i, "Some where in Brasil", "Male");
    }

    public Person create(Person person) {
        logger.info("Creating one person...");
        return person;
    }

    public Person update(Person person) {
        logger.info("Updating one person...");
        return person;
    }

    public void delete(String id) {
        logger.info("Deleting one person...");
    }
}
