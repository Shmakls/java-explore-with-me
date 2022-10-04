package ru.andrianov.emw.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andrianov.emw.stat.model.Apps;

import java.util.Optional;

public interface AppRepository extends JpaRepository<Apps, Long> {

    Optional<Apps> getAppsByAppIgnoreCase(String app);

    Apps getAppsById(Long id);

}
