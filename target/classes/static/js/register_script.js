document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('registrationForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var name = document.getElementById('name').value;
        var password = document.getElementById('password').value;
        var confirmPassword = document.getElementById('confirmPassword').value;
        var passwordError = document.getElementById('passwordError');

        if (password !== confirmPassword) {
            passwordError.style.display = 'block';
        } else {
            passwordError.style.display = 'none';

            var formData = new URLSearchParams();
            formData.append('name', name);
            formData.append('password', password);

            fetch('/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData
            })
                .then(function(response) {
                    if (response.ok) {
                        alert('Регистрация прошла успешно');
                    } else {
                        alert('Произошла ошибка при регистрации');
                    }
                })
                .then(function(data) {
                    // Сохраняем токен в локальное хранилище
                    alert('Вход выполнен успешно');
                })
                .catch(function(error) {
                    console.log('Произошла ошибка: ' + error);
                });
        }
    });
});
