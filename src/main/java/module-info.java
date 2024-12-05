module miit.chuice.tour {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.slf4j;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires spring.context;
    requires spring.beans;
    requires spring.boot;
    requires spring.data.jpa;
    requires spring.core;
    requires spring.boot.autoconfigure;
    requires spring.tx;
    requires spring.orm;
    requires java.base;
    requires spring.data.commons;

    opens miit.chuice.tour to javafx.fxml, spring.core, spring.beans;
    exports miit.chuice.tour;
    exports miit.chuice.tour.controllers.user;
    opens miit.chuice.tour.controllers.user to javafx.fxml, spring.core, spring.beans;
    exports miit.chuice.tour.controllers.admin;
    opens miit.chuice.tour.controllers.admin to javafx.fxml, spring.core, spring.beans;
    exports miit.chuice.tour.controllers.porter;
    opens miit.chuice.tour.controllers.porter to javafx.fxml, spring.core, spring.beans;
    exports miit.chuice.tour.controllers.security;
    opens miit.chuice.tour.controllers.security to javafx.fxml, spring.core, spring.beans;
    exports miit.chuice.tour.repositories to javafx.fxml, spring.core, spring.beans, spring.boot.autoconfigure;
    opens miit.chuice.tour.views to spring.core, spring.beans;
    exports miit.chuice.tour.models to org.hibernate.orm.core, spring.core, spring.beans;
    opens miit.chuice.tour.config to spring.core, spring.beans;
    exports miit.chuice.tour.services to spring.core, spring.beans;
    opens miit.chuice.tour.services to spring.core, spring.beans;
    exports miit.chuice.tour.security to org.hibernate.orm.core, spring.core, spring.beans;
    opens miit.chuice.tour.security.bcrypt to spring.core, spring.beans;
    opens miit.chuice.tour.models to org.hibernate.orm.core, spring.core, javafx.base;
    opens miit.chuice.tour.utils to spring.beans;
    exports miit.chuice.tour.config to spring.context;

}