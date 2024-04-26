module esprit.monstergym.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires jbcrypt;
    requires mail;
    requires AnimateFX;
    requires de.jensd.fx.glyphs.fontawesome;
    requires itextpdf;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    //requires mail;
    //requires java.mail;

    opens esprit.lanforlife.demo.Controllers to javafx.fxml; // Add this line to open the Main package
    exports esprit.lanforlife.demo;
    exports esprit.lanforlife.demo.Controllers;
    exports esprit.lanforlife.demo.Entities; // Ajoutez cette ligne pour exporter le package Entities
    exports esprit.lanforlife.demo.Service;

}
