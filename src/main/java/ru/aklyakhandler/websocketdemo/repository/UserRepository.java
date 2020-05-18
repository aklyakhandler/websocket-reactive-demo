package ru.aklyakhandler.websocketdemo.repository;

import org.springframework.data.repository.CrudRepository;
import ru.aklyakhandler.websocketdemo.model.data.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
