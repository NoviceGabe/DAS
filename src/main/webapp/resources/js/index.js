function isEmailValid(email) {
  const re = /^([_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*(\.[a-zA-Z]{1,6}))?$/;
  return re.test(email);
}

function isPhoneNumberValid(number){
	const re = /^(\+639)\d{9}$/;
  return re.test(number);
}

function isNameValid(name){
	const re = /^[a-zA-Z ]{2,30}$/;
  return re.test(name);
}

function isEmpty(target) {
	return !(target && target.length > 0);
}

function isAgeValid(age){
	const re = /^[0-9]{1,3}$/;
	 return re.test(age); 
}