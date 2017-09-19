


var clockChanged = function (time) {
    console.log(time)
    $('#clock').html(time.replace(/(\d)/g, '<spsn>$1</span>'))
}