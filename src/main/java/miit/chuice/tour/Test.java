package miit.chuice.tour;

import miit.chuice.tour.models.Human;
import miit.chuice.tour.services.HumanService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Test {

    private static HumanService humanService;

    @Autowired
    public Test(HumanService humanService) {
        Test.humanService = humanService;
    }

    public static void main(String[] args) {
        System.out.println(humanService.findHumanByLogin("pavel").toString());
    }
}
