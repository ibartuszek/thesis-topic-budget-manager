export function removeElementFromArray(targetList, id) {
  let found = false;
  for (let index = 0; index < targetList.length && !found; index++) {
    let subCategory = targetList[index];
    if (id === subCategory.id) {
      found = true;
      targetList.splice(index, 1)
    }
  }
  return targetList;
}

export function addElementToArray(targetList, newObject) {
  let result = targetList.slice();
  result.push(newObject);
  return result;
}

export function createCategoryListForSelect(categoriesFromRepo, categoriesFromObject) {
  let result = [];
  result.push({id: null, name: {value: null}});
  for (let i = 0; i < categoriesFromRepo.length; i++) {
    let found = false;
    let category = categoriesFromRepo[i];
    for (let j = 0; j < categoriesFromObject.length && found === false; j++) {
      if (category.id === categoriesFromObject[j].id) {
        found = true;
      }
    }
    if (found === false) {
      result.push(category);
    }
  }
  return result;
}

export function replaceElementAtArray(targetList, newObject) {
  let newArrayWithoutElement = removeElementFromArray(targetList, newObject.id);
  return addElementToArray(newArrayWithoutElement, newObject);
}
