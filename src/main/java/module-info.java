module miit.chuice.tour {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.bouncycastle.provider;
    requires org.slf4j;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;



    opens miit.chuice.tour to javafx.fxml;
    exports miit.chuice.tour;
    exports miit.chuice.tour.dto;
    opens miit.chuice.tour.dto to javafx.fxml;
    exports miit.chuice.tour.models;
    opens miit.chuice.tour.models to org.hibernate.orm.core;
    exports miit.chuice.tour.controllers;
    opens miit.chuice.tour.controllers to javafx.fxml;
}