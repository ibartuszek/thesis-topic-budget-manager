export function removeElementFromArray(targetList, elementToRemove) {
  let found = false;
  for (let index = 0; index < targetList.length && !found; index++) {
    let currentObject = targetList[index];
    if (currentObject.id === elementToRemove.id) {
      found = true;
      targetList.splice(index, 1);
    }
  }
  return targetList;
}

export function addElementToArray(targetList, newObject) {
  let result = targetList.slice();
  result.push(newObject);
  return result;
}

export function replaceElementAtArray(targetList, newObject) {
  let newArrayWithoutElement = removeElementFromArray(targetList, newObject);
  return addElementToArray(newArrayWithoutElement, newObject);
}

export function findElementByName(listFromRepo, name) {
  let result = null;
  for (let index = 0; index < listFromRepo.length && result === null; index++) {
    let currentObject = listFromRepo[index];
    result = currentObject.name.value === name ? currentObject : null;
  }
  return result;
}

export function findElementById(listFromRepo, id) {
  let result = null;
  for (let index = 0; index < listFromRepo.length && result === null; index++) {
    let currentObject = listFromRepo[index];
    result = currentObject.id === id ? currentObject : null;
  }
  return result;
}

export function createCategoryListForSelect(categoriesFromRepo, categoriesFromObject) {
  let result = [];
  populateResultList(result, categoriesFromRepo, categoriesFromObject);
  return result;
}

export function createCategoryListWithNullForSelect(categoriesFromRepo, categoriesFromObject) {
  let result = [];
  result.push(null);
  populateResultList(result, categoriesFromRepo, categoriesFromObject);
  return result;
}

function populateResultList(result, categoriesFromRepo, categoriesFromObject) {
  for (let i = 0; i < categoriesFromRepo.length; i++) {
    let found = false;
    let category = categoriesFromRepo[i];
    if (categoriesFromObject !== undefined) {
      for (let j = 0; j < categoriesFromObject.length && found === false; j++) {
        if (category.id === categoriesFromObject[j].id) {
          found = true;
        }
      }
    }
    if (found === false) {
      result.push(category.name.value);
    }
  }
}
