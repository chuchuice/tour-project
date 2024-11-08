package miit.chuice.tour.repositories;

import miit.chuice.tour.models.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {
    Human findHumanByLogin(String login);
}