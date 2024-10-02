module io.github.cakilgan.cconsole {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.fxmisc.richtext;
    requires java.desktop;
    requires org.joml;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires org.lwjgl.opencl;
    requires org.lwjgl.stb;
    requires org.joml.primitives;
    requires jbox2d.library;
    requires org.lwjgl.freetype;
    requires junit;


    opens io.github.cakilgan.cconsole to javafx.fxml;
    exports io.github.cakilgan.cconsole;
}