var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
    },
    save : function () {
        var data= {
            email: $('#email').val(),
            password: $('#password').val(),
            city: $('#city').val(),
            street: $('#street').val(),
            zipcode: $('#zipcode').val()
        };
        $.ajax({
            type: 'POST',
            url: '/api/member',
            data: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('회원가입이 되었습니다.');
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();