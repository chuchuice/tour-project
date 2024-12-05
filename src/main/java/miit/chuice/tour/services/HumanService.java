package miit.chuice.tour.services;

import miit.chuice.tour.models.Human;
import miit.chuice.tour.repositories.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanService {

    private final HumanRepository humanRepository;

    @Autowired
    public HumanService(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    public Human findHumanByLogin(String login) {
        return humanRepository.findHumanByLogin(login);
    }

    public Human findHumanByEmail(String email) {
        return humanRepository.findHumanByEmail(email);
    }

    public void save(Human human) {
        humanRepository.save(human);
    }
}