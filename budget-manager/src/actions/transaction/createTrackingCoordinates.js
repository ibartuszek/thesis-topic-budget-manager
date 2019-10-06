export async function createTrackingCoordinates(transactionType, tracking) {
  let result = null;
  if (tracking === true && transactionType === 'OUTCOME') {
    let position = await getPosition();
    result = {
      latitude: position.coords.latitude,
      longitude: position.coords.longitude
    };
  }
  return result;
}

function getPosition() {
  return new Promise((res, rej) => {
    navigator.geolocation.getCurrentPosition(res, rej);
  });
}
