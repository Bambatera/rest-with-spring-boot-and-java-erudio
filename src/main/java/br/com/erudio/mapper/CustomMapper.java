package br.com.erudio.mapper;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.model.Person;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

public class CustomMapper {

    private static ModelMapper mapper = new ModelMapper();

    static {
        mapper.createTypeMap(
                Person.class,
                PersonVO.class)
                .addMapping(Person::getId, PersonVO::setKey);
        mapper.createTypeMap(
                PersonVO.class,
                Person.class)
                .addMapping(PersonVO::getKey, Person::setId);
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = origin.stream().map(o -> mapper.map(o, destination)).collect(Collectors.toList());
        return destinationObjects;
    }
}
