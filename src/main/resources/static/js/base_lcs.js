$.ajax({
    url: '/users/me',
    method: 'GET',
    success: function (user) {
    if (user && user.username) {
    $('#auth-buttons').hide();
    $('#welcome-message').text(user.username + '님 환영합니다');
    $('#user-info').show();
}
},
    error: function (xhr, status, error) {
    console.error("Error loading user info:", error); // 에러 메시지 콘솔에 출력
    $('#auth-buttons').show();
    $('#user-info').hide();
}
});
