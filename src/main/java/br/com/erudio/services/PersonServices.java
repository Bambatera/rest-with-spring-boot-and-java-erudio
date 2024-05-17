package br.com.erudio.services;

import br.com.erudio.model.Person;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());
    
    public Person findById(String id) {
        logger.info("Finding one person...");
        Person person = new Person(counter.incrementAndGet(), "Leandro", "Silva", "Samambaia, Brasília, Brasil", "Male");
        return person;
    }
}
