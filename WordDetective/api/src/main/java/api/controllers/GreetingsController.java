package api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class GreetingsController {
  /**
   *
   * @param name the name to greet.
   * @return greeting text.
   */
  @RequestMapping(value = "/{name}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public String greetingText(@PathVariable final String name) {
    return "Hello " + name + "!";
  }
}
