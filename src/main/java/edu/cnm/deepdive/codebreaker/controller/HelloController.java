package edu.cnm.deepdive.codebreaker.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class HelloController {

  @FXML
  private Text greeting;

  @FXML
  private void initialized() {
    greeting.setText("Hello, brave new world!");
  }
}
