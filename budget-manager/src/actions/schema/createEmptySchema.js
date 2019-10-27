export function createEmptySchema() {
  return {
    id: null,
    title: {
      value: '',
      errorMessage: null,
      minimumLength: 2,
      maximumLength: 20,
    },
    type: {
      value: "STANDARD",
      errorMessage: null,
      possibleEnumValues: [
        "STANDARD",
        "SCALE",
        "SUM"
      ]
    },
    currency: {
      value: "HUF",
      errorMessage: null,
      possibleEnumValues: [
        "EUR",
        "USD",
        "HUF"
      ]
    },
    chartType: {
      value: "RADIAL",
      errorMessage: null,
      possibleEnumValues: [
        "RADIAL",
        "BAR",
        "LINEAR"
      ]
    },
    mainCategory: undefined,
    subCategory: undefined
  };
}
