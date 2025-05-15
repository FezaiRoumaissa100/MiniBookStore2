document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const submitButton = form.querySelector('button[type="submit"]');

    function validatePasswords() {
        if (password.value !== confirmPassword.value) {
            confirmPassword.setCustomValidity("Passwords don't match");
            submitButton.disabled = true;
        } else {
            confirmPassword.setCustomValidity('');
            submitButton.disabled = false;
        }
    }

    password.addEventListener('change', validatePasswords);
    confirmPassword.addEventListener('keyup', validatePasswords);

    form.addEventListener('submit', function (event) {
        if (password.value !== confirmPassword.value) {
            event.preventDefault();
            alert("Passwords don't match!");
        }
    });
}); 