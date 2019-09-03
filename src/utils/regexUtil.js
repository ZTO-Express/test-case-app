function test(value, regex) {
  if (value && value.length > 0) {
    return regex.test(value);
  }
}

export default {
  test,
};
