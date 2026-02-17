package edu.cnm.deepdive.codebreaker.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class HelloController {

  public static final String HELLO_BRAVE_NEW_WORLD = "Hello, brave new world!";
  @FXML
  private Text greeting;

  @FXML
  private void initialized() {
    greeting.setText(HELLO_BRAVE_NEW_WORLD);
  }
}
