export function createErrorBody(response) {
  const reader = response.body.getReader();
  return reader.read().then(({done, value}) => {
    return new TextDecoder("utf-8").decode(value);
  });
}
