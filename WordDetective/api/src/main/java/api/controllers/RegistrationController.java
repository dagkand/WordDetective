package api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import core.RegistrationAuthentication;
import types.RegistrationStatus;

@RestController
public class RegistrationController {

  /**
   * RegistrationAuthentication instance to provide access to required
   * persistently information.
   */
  private RegistrationAuthentication authentication;

  /**
   * API endpoint for registration of User. Returns constant indicating result of
   * registration.
   *
   * @param username The provided username of the new user.
   * @param password The provided password of the new user.
   * @return SUCCESS, USERNAME_TAKEN, USERNAME_NOT_MATCH_REGEX,
   *         PASSWORD_NOT_MATCH_REGEX, or UPLOAD_ERROR, respectively.
   */
  @RequestMapping(value = "/RegistrationController/registrationResult", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public RegistrationStatus registrationResult(final @RequestParam("username") String username,
      final @RequestParam("password") String password) {
    if (authentication == null) {
      authentication = new RegistrationAuthentication();
    }
    return authentication.registrationResult(username, password);
  }

}
