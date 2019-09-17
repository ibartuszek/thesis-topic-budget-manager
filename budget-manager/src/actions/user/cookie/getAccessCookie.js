import base64 from "react-native-base64";

export function getAccessCookie() {
  const cookieName = "CAT=";
  const tokenName = "t=";
  const userName = "u=";
  let cookieFromBrowser = decodeURIComponent(document.cookie);
  let result = null;
  if (cookieFromBrowser !== undefined && cookieFromBrowser !== null && cookieFromBrowser !== "" && cookieFromBrowser.includes(cookieName)) {
    cookieFromBrowser.replace(cookieName, "");
    cookieFromBrowser = base64.decode(cookieFromBrowser);
    let parts = cookieFromBrowser.split("&");
    result = {
      jwtToken: parts[0].substring(parts[0].indexOf(tokenName) + 2),
      userName: parts[1].substring(parts[1].indexOf(userName) + 2)
    }
  }
  return result;
}
