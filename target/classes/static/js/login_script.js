document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('loginForm').addEventListener('submit', function(event) {
        event.preventDefault();
        var username = document.getElementById('username').value;
        var password = document.getElementById('password').value;

        // Формируем данные для отправки на сервер в формате x-www-form-urlencoded
        var formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);

        // Отправка данных на сервер
        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        })
            .then(function(response) {
                if (response.ok) {
                    // Если ответ от сервера успешный, выполняем вход
                    alert('Вход выполнен успешно');
                } else {
                    // Если ответ от сервера содержит ошибку, выводим сообщение об ошибке
                    alert('Неверный логин или пароль');
                }
            })
            .then(function(data) {
                // Сохраняем токен в локальное хранилище
                alert('Вход выполнен успешно');
            })
            .catch(function(error) {
                // Обработка ошибок при отправке запроса
                console.log('Произошла ошибка: ' + error);
            });
    });
});
