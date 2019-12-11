import base64 from "react-native-base64";

export function createHeaderWithJwt(jwtToken) {
  let header = new Headers();
  header.set('Authorization', 'Bearer ' + jwtToken);
  return header;
}

export function createHeaderWithJwtAndJsonBody(jwtToken) {
  let header = createHeaderWithJwt(jwtToken);
  header.set('Content-Type', 'application/json');
  return header;
}

export function createHeaderWithClientSecret(clientId, clientSecret) {
  let header = new Headers();
  header.set('Authorization', 'Basic ' + base64.encode(clientId + ':' + clientSecret));
  header.set('content-type', 'application/x-www-form-urlencoded;UTF-8');
  return header;
}
