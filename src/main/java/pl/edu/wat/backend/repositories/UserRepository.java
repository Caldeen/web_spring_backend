package pl.edu.wat.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.wat.backend.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity,Integer> {
}