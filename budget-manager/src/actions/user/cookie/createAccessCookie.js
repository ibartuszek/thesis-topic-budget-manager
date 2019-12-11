import base64 from "react-native-base64";

export function createAccessCookie(userName, token) {
  let d = new Date();
  d.setTime(d.getTime() + (24 * 60 * 60 * 1000));
  let expires = "expires=" + d.toUTCString();
  let body = base64.encode("t=" + token + "&u=" + userName);
  document.cookie = "CAT=" + body + ";" + expires;
}
