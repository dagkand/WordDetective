package api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import core.LoginAuthentication;
import types.LoginStatus;

@RestController
// @Scope("session")
public class LoginController {

  /**
   * LoginAuthentication instance to provide access to required persistently
   * stored user information.
   */
  private LoginAuthentication authentication;

  /**
   * API endpoint for check of valid login information.
   *
   * @param requestBody Requestbody containing the username and password.
   * @return SUCCESS, USERNAME_DOES_NOT_EXIST, INCORRECT_PASSWORD, or READ_ERROR,
   *         respectively.
   */
  @RequestMapping(value = "/LoginController/performLogin", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public LoginStatus performLogin(@RequestBody final String requestBody) {
    String[] components = requestBody.split("&");
    String username = components[0].split("=")[1];
    String password = components[1].split("=")[1];
    authentication = new LoginAuthentication(username);
    return authentication.authenticate(password);
  }

}
